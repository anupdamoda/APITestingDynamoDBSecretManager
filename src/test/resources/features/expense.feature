Feature: Expense management

  Background:
    Given I am logged in as the test user

#  Scenario: List expenses for the logged-in user
#    When I request the list of expenses
#    Then the response status code should be 200
#    And the response Content-Type should be JSON
#    And the response body should be a JSON array
#
#  Scenario: Create expense fails when expenseCategory is missing
#    When I create an expense with name "Bus" category <none> and amount <null>
#    Then the response status code should be 400
#    And the response Content-Type should be JSON
#    And the response body field "message" should equal "Expense category is required"
#
#  Scenario: Create expense fails when expenseAmount is null
#    When I create an expense with name "Bus" category "Travel" and amount <null>
#    Then the response status code should be 400
#    And the response Content-Type should be JSON
#    And the response body field "message" should equal "Expense Amount is mandatory and must be greater than 0"

    @DatabaseTest
  Scenario: Create expense succeeds with a complete payload
    When I create an expense with details coming from Database
    Then the response status code should be 201
    And the response body should be empty
