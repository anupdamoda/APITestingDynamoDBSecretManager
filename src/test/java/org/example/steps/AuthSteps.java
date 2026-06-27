package org.example.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.example.api.AuthApi;
import org.example.config.Config;
import org.example.context.ScenarioContext;
import org.example.utility.SecretManagerUtil;

import java.util.Map;
import java.util.UUID;

import static org.example.utility.SecretManagerUtil.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.oneOf;

public class AuthSteps {

    private final AuthApi authApi = new AuthApi();
    private final ScenarioContext context;

    public AuthSteps(ScenarioContext context) {
        this.context = context;
    }

    @Given("a unique gmail email is generated")
    public void generateUniqueGmail() {
        String email = "ashtek"
                + UUID.randomUUID().toString().replace("-", "").substring(0, 12)
                + "@gmail.com";
        context.put("currentEmail", email);
    }

    @When("I register with name {string} and password {string}")
    public void registerWithGeneratedEmail(String name, String password) {
        String email = context.get("currentEmail");
        Response response = authApi.register(name, email, password);
        context.setLastResponse(response);
    }

    @When("I register with name {string} email {string} and password {string}")
    public void registerExplicit(String name, String email, String password) {
        context.put("currentEmail", email);
        Response response = authApi.register(name, email, password);
        context.setLastResponse(response);
    }

    @And("I register again with the same email")
    public void registerAgain() {
        String email = context.get("currentEmail");
        Response response = authApi.register("ashtek", email, "Password123");
        context.setLastResponse(response);
    }

    @Given("I am logged in as the test user")
    public void loginAsTestUser() {
        // This method is intended to demonstrate fetching credentials from AWS Secrets Manager instead of config.properties.
        Map<String, String> data = SecretManagerUtil.getCredentialsFromSecretsManager();
        System.out.println("Credentials from secret manager: " + data.get("username") + " / " + data.get("password"));
        Response response = authApi.login(data.get("username"), data.get("password"));
        System.out.println("Login response status: " + response.statusCode());
        context.setLastResponse(response);


        assertThat("Login must succeed for the configured test user",
                response.statusCode(), is(200));
        String token = extractToken(response);
        assertThat("Login response must contain a token", token, matchesPattern("^[^.]+\\.[^.]+\\.[^.]+$"));
        context.setAuthToken(token);
    }

    @When("I login with the configured test user credentials")
    public void loginWithValidCreds() {
     //   This method is intended to demonstrate fetching credentials from AWS Secrets Manager instead of config.properties.
       Map<String, String> data = SecretManagerUtil.getCredentialsFromSecretsManager();
       System.out.println("Credentials from secret manager: " + data.get("username") + " / " + data.get("password"));
       Response response = authApi.login(data.get("username"), data.get("password"));
       System.out.println("Login response status: " + response.statusCode());
       context.setLastResponse(response);
    }

    @When("I login with the configured test user email and an invalid password")
    public void loginWithInvalidCreds() {
        Response response = authApi.login(Config.testUserEmail(), Config.invalidPassword());
        context.setLastResponse(response);
    }

    @Then("the response should contain a JWT token")
    public void responseShouldContainJwt() {
        String token = extractToken(context.getLastResponse());
        assertThat(token, matchesPattern("^[^.]+\\.[^.]+\\.[^.]+$"));
    }

    @Then("the response status code should be {int}")
    public void statusCodeShouldBe(int expected) {
        assertThat(context.getLastResponse().statusCode(), equalTo(expected));
    }

    @Then("the response status code should be one of {int}, {int} or {int}")
    public void statusCodeShouldBeOneOfThree(int a, int b, int c) {
        assertThat(context.getLastResponse().statusCode(), is(oneOf(a, b, c)));
    }

    @Then("the response status code should be one of {int} or {int}")
    public void statusCodeShouldBeOneOfTwo(int a, int b) {
        assertThat(context.getLastResponse().statusCode(), is(oneOf(a, b)));
    }

    @Then("the response body should contain {string}")
    public void responseBodyShouldContain(String fragment) {
        assertThat(context.getLastResponse().asString(), containsString(fragment));
    }

    @Then("the response body should have field {string}")
    public void responseBodyShouldHaveField(String field) {
        assertThat("Field '" + field + "' is missing from response",
                context.getLastResponse().jsonPath().get(field), notNullValue());
    }

    @Then("the response body field {string} should equal {string}")
    public void responseBodyFieldShouldEqual(String field, String expected) {
        assertThat(context.getLastResponse().jsonPath().getString(field), equalTo(expected));
    }

    @Then("the response body field {string} should equal {int}")
    public void responseBodyFieldShouldEqualInt(String field, int expected) {
        assertThat(context.getLastResponse().jsonPath().getInt(field), equalTo(expected));
    }

    @Then("the response body field {string} should contain {string}")
    public void responseBodyFieldShouldContain(String field, String fragment) {
        assertThat(context.getLastResponse().jsonPath().getString(field),
                containsString(fragment));
    }

    @Then("the response body should be a JSON array")
    public void responseBodyShouldBeJsonArray() {
        assertThat(context.getLastResponse().jsonPath().getList("$"), notNullValue());
    }

    @Then("the response Content-Type should be JSON")
    public void responseContentTypeShouldBeJson() {
        String contentType = context.getLastResponse().getContentType();
        assertThat(contentType, containsString("application/json"));
    }

    @Then("the response body should be empty")
    public void responseBodyShouldBeEmpty() {
        String body = context.getLastResponse().asString();
        assertThat("Expected empty body but got: " + body,
                body == null || body.isEmpty(), is(true));
    }

    private String extractToken(Response response) {
        for (String field : new String[]{"token", "accessToken", "access_token", "jwt", "idToken"}) {
            String value = response.jsonPath().getString(field);
            if (value != null && !value.isEmpty()) return value;
        }
        return null;
    }
}
