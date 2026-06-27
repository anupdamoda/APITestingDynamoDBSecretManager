Feature: User registration

#  Scenario: Register a new user with a valid Gmail address
#    Given a unique gmail email is generated
#    When I register with name "ashtek" and password "Password123"
#    Then the response status code should be 200
#    And the response Content-Type should be JSON
#    And the response body field "message" should contain "User registered successfully"
#    And the response body field "message" should contain "OTP"
#
#  Scenario: Register fails when email domain is not allowed
#    When I register with name "ashtek" email "ashtek-test@example.com" and password "Password123"
#    Then the response status code should be 400
#    And the response Content-Type should be JSON
#    And the response body field "error" should equal "Only Gmail, Yahoo, Outlook, Hotmail, AOL, and iCloud emails are allowed"
#
#  Scenario: Register with a duplicate email is rejected
#    Given a unique gmail email is generated
#    When I register with name "ashtek" and password "Password123"
#    And I register again with the same email
#    Then the response status code should be 409
#    And the response Content-Type should be JSON
#    And the response body should have field "error"
