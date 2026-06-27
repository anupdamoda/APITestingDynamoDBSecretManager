Feature: User login

  @LoginTest
  Scenario: Login with valid credentials returns a JWT
    When I login with the configured test user credentials
    Then the response status code should be 200
    And the response Content-Type should be JSON
    And the response should contain a JWT token
#
#  Scenario: Login with an invalid password is rejected
#    When I login with the configured test user email and an invalid password
#    Then the response status code should be one of 400, 401 or 403
#    And the response Content-Type should be JSON
#    And the response body should have field "error"
