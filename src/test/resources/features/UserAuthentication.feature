@UserAuthenticationFeature
Feature: Validate registration and authentication functionality for the bookstore system

  @Sanity @Positive @SignUpNewUser
  Scenario: Register a new user with valid email and password
    Given the user prepares valid signup credentials
    When the user sends a signup request with new credentials
    Then the response code should be 200 and the message should be "User created successfully"

  @Sanity @Positive @SignUpAndLogin
  Scenario: Register a new user and login with the same credentials
    Given the user prepares valid signup credentials
    When the user sends a signup request with new credentials
    Then the response code should be 200 and the message should be "User created successfully"
    When the user logs in using the same credentials
    Then the response code should be 200 and the message should contain "access_token" with "token_type"

  @Regression @Negative @SignUpWithExistingEmail
  Scenario: Attempt to register with an already existing email
    Given the user prepares valid signup credentials
    When the user sends a signup request using an already registered email
    Then the response code should be 400 and the message should be "Email already registered"

  @Regression @Positive @SignUpWithOnlyEmail
  Scenario: Attempt to sign up using only an email without a password
    Given the user prepares valid signup credentials
    When the user sends a signup request with only an email
    Then the response code should be 500 and the message should be "HTTP/1.1 500 Internal Server Error"

  @Regression @Negative @SignUpWithOnlyPassword
  Scenario: Attempt to sign up using only a password without an email
    Given the user prepares valid signup credentials
    When the user sends a signup request with only a password
    Then the response code should be 500 and the message should be "HTTP/1.1 500 Internal Server Error"

  @Regression @Negative @LoginWithoutRegistration
  Scenario: Attempt to login without registering first
    When the user logs in using unregistered credentials
    Then the login response code should be 400 and the message should be "Incorrect email or password"

  @Regression @Negative @LoginWithMissingParameters
  Scenario: Attempt to login with missing email or password fields
    When the user logs in with missing parameters
    Then the login response code should be 422 and the message should be "Missing required fields"
