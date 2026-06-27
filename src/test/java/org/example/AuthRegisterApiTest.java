package org.example;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class AuthRegisterApiTest {

    @BeforeAll
    static void setUp() {
        baseURI = "https://c0bd29gtl5.execute-api.us-east-1.amazonaws.com";
    }

    private static String uniqueGmail(String prefix) {
        return prefix + UUID.randomUUID().toString().replace("-", "").substring(0, 12) + "@gmail.com";
    }

    @Test
    @DisplayName("POST /api/auth/register creates a new user")
    void registerNewUser() {
        String uniqueEmail = uniqueGmail("ashtek");

        String payload = "{\n" +
                "  \"name\": \"ashtek\",\n" +
                "  \"email\": \"" + uniqueEmail + "\",\n" +
                "  \"password\": \"Password123\"\n" +
                "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(payload)
            .when()
                .post("/api/auth/register")
            .then()
                .extract().response();

        System.out.println("Status: " + response.statusCode());
        System.out.println("Body:   " + response.asString());

        response.then().statusCode(anyOf(is(200), is(201)));
    }

    @Test
    @DisplayName("POST /api/auth/register with duplicate email is rejected")
    void registerDuplicateEmailRejected() {
        String email = uniqueGmail("dup");
        String payload = "{\n" +
                "  \"name\": \"ashtek\",\n" +
                "  \"email\": \"" + email + "\",\n" +
                "  \"password\": \"Password123\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(payload)
            .when()
                .post("/api/auth/register")
            .then()
                .statusCode(anyOf(is(200), is(201)));

        given()
                .contentType(ContentType.JSON)
                .body(payload)
            .when()
                .post("/api/auth/register")
            .then()
                .statusCode(anyOf(is(400), is(409), is(422)));
    }
}
