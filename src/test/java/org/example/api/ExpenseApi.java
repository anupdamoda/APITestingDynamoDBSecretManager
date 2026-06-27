package org.example.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class ExpenseApi {

    private RequestSpecification authed(String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .contentType(ContentType.JSON);
    }

    public Response list(String token) {
        return authed(token)
            .when()
                .get("/api/expense");
    }

    public Response create(String token, String name, String category, Integer amount) {
        Map<String, Object> body = new LinkedHashMap<>();
        if (name != null) body.put("expenseName", name);
        if (category != null) body.put("expenseCategory", category);
        body.put("expenseAmount", amount);

        return authed(token)
                .body(body)
            .when()
                .post("/api/expense");
    }

}
