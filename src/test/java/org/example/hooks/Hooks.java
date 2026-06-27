package org.example.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import org.example.config.Config;
import org.example.context.ScenarioContext;

public class Hooks {

    private final ScenarioContext context;

    public Hooks(ScenarioContext context) {
        this.context = context;
    }

    @Before(order = 0)
    public void setBaseUri() {
        RestAssured.baseURI = Config.baseUrl();
    }

    @After(order = 100)
    public void attachResponseOnFailure(Scenario scenario) {
        if (scenario.isFailed() && context.getLastResponse() != null) {
            String body = context.getLastResponse().asString();
            int status = context.getLastResponse().statusCode();
            String contentType = context.getLastResponse().getContentType();
            scenario.attach(body, "application/json", "Last response body");
            scenario.log("Last response status: " + status);
            System.out.println("===== FAILED: " + scenario.getName() + " =====");
            System.out.println("Status:       " + status);
            System.out.println("Content-Type: " + contentType);
            System.out.println("Body:         " + body);
            System.out.println("===========================================");
        }
    }

    @After(order = 0)
    public void clearContext() {
        context.clear();
    }
}
