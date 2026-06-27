package org.example.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthApi {

    public Response register(String name, String email, String password) {
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("email", email);
        body.put("password", password);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
            .when()
                .post("/api/auth/register");
    }

    public Response login(String email, String password) {
        Map<String, Object> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
            .when()
                .post("/api/auth/login");
    }
}
