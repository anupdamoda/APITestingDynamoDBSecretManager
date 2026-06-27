package org.example.steps;

import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.example.api.ExpenseApi;
import org.example.context.ScenarioContext;

import java.util.Map;

import static org.example.utility.DynamoDBUtil.getRandomExpense;

public class ExpenseSteps {

    private final ExpenseApi expenseApi = new ExpenseApi();
    private final ScenarioContext context;

    public ExpenseSteps(ScenarioContext context) {
        this.context = context;
    }

    @When("I request the list of expenses")
    public void listExpenses() {
        Response response = expenseApi.list(context.getAuthToken());
        context.setLastResponse(response);
    }

    @When("I create an expense with name {string} category <none> and amount <null>")
    public void createExpenseNoCategoryNullAmount(String name) {
        Response response = expenseApi.create(context.getAuthToken(), name, null, null);
        context.setLastResponse(response);
    }

    @When("I create an expense with name {string} category {string} and amount <null>")
    public void createExpenseNullAmount(String name, String category) {
        Response response = expenseApi.create(context.getAuthToken(), name, category, null);
        context.setLastResponse(response);
    }

    @When("I create an expense with details coming from Database")
    public void createExpense() {
        Map<String, String> expenseData = getRandomExpense();
        String name = expenseData.get("ExpenseName");
        String category = expenseData.get("ExpenseCategory");
        int amount = Integer.parseInt(expenseData.get("ExpenseAmount"));
        System.out.println("Creating expense with name: " + name + ", category: " + category + ", amount: " + amount);
        Response response = expenseApi.create(context.getAuthToken(), name, category, amount);
        context.setLastResponse(response);
    }
}
