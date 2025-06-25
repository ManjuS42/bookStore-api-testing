package steps;

import baseController.AuthenticationController;
import data.BookStoreInteractionData;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;
import utils.BookStoreConfig;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.Assert.assertNotNull;

public class AuthenticationSteps {

  private final BookStoreInteractionData bookStoreInteractionData;

  public AuthenticationSteps(BookStoreInteractionData bookStoreInteractionData) {
    this.bookStoreInteractionData = bookStoreInteractionData;
  }

  @Given("the user prepares valid signup credentials")
  public void theUserPreparesValidSignupCredentials() {
    String email = AuthenticationController.randomAlphanumeric(5) + BookStoreConfig.get("userEmailDomain");
    String password = AuthenticationController.randomAlphanumeric(15);

    assertThat("Email must not be blank", isNotBlank(email), is(true));
    assertThat("Password must not be blank", isNotBlank(password), is(true));

    System.out.println("Email: " + email + " and " + "Password: " + password);
  }

  @When("the user sends a signup request with new credentials")
  public void theUserSendsASignupRequestWithNewCredentials() {
    String email = AuthenticationController.randomAlphanumeric(5) + BookStoreConfig.get("userEmailDomain");
    String password = AuthenticationController.randomAlphanumeric(15);

    bookStoreInteractionData.setUserEmail(email);
    bookStoreInteractionData.setUserPassword(password);
    Response signUpResponse = AuthenticationController.newUserSignUp(bookStoreInteractionData.getUserEmail(),
        bookStoreInteractionData.getUserPassword());
    bookStoreInteractionData.setRegistrationResult(signUpResponse);
  }

  @Then("the response code should be {int} and the message should be {string}")
  public void theResponseCodeShouldBeAndTheMessageShouldBe(int responseCode, String message) {
    Response signUpResponse = bookStoreInteractionData.getRegistrationResult();
    int actualStatusCode = signUpResponse.getStatusCode();
    String actualMessage;

    Assert.assertEquals("Unexpected Error Occurred during signup", actualStatusCode, responseCode);

    if (responseCode == 200) {
      actualMessage = signUpResponse.getBody().jsonPath().getString("message");
      assertThat("User created successfully", actualMessage, equalTo(message));
    } else if (responseCode == 400) {
      actualMessage = signUpResponse.getBody().jsonPath().getString("detail");
      assertThat("Email already registered message mismatch", actualMessage, equalTo(message));
    } else {
      actualMessage = signUpResponse.getStatusLine();
      assertThat("Email already registered message mismatch", actualMessage, equalTo(message));
    }
  }


  @When("the user logs in using the same credentials")
  public void theUserLogsInUsingTheSameCredentials() {
    Response signUpResponse = AuthenticationController.userLogin(bookStoreInteractionData.getUserEmail(),
        bookStoreInteractionData.getUserPassword());
    bookStoreInteractionData.setAuthenticationResult(signUpResponse);
  }

  @When("the user sends a signup request using an already registered email")
  public void theUserSendsASignupRequestUsingAnAlreadyRegisteredEmail() {

    String email = BookStoreConfig.get("registeredUserEmail");
    String password = AuthenticationController.randomAlphanumeric(15);
    bookStoreInteractionData.setUserEmail(email);
    bookStoreInteractionData.setUserPassword(password);
    Response signUpResponse = AuthenticationController.newUserSignUp(bookStoreInteractionData.getUserEmail(),
        bookStoreInteractionData.getUserPassword());
    bookStoreInteractionData.setRegistrationResult(signUpResponse);

  }

  @When("the user sends a signup request with only a password")
  public void theUserSendsASignupRequestWithOnlyAPassword() {
    String password = AuthenticationController.randomAlphanumeric(15);

    bookStoreInteractionData.setUserPassword(password);
    Response signUpResponse = AuthenticationController.newUserSignUp(bookStoreInteractionData.getUserEmail(),
        bookStoreInteractionData.getUserPassword());
    bookStoreInteractionData.setRegistrationResult(signUpResponse);
  }

  @When("the user sends a signup request with only an email")
  public void theUserSendsASignupRequestWithOnlyAnEmail() {

    String email = AuthenticationController.randomAlphanumeric(15) + BookStoreConfig.get("userEmailDomain");

    bookStoreInteractionData.setUserEmail(email);
    Response signUpResponse = AuthenticationController.newUserSignUp(bookStoreInteractionData.getUserEmail(),
        bookStoreInteractionData.getUserPassword());
    bookStoreInteractionData.setRegistrationResult(signUpResponse);
  }

  @Then("the response code should be {int} and the message should contain {string} with {string}")
  public void theResponseCodeShouldBeAndTheMessageShouldContainWith(int responseCode,
      String accessToken,
      String tokenType) {

    Response signUpResponse = bookStoreInteractionData.getAuthenticationResult();
    int actualStatusCode = signUpResponse.getStatusCode();
    Assert.assertEquals("Unexpected Error Occurred during signup", actualStatusCode, responseCode);

    String actualAccessToken = signUpResponse.getBody().jsonPath().getString(accessToken);
    String actualTokenType = signUpResponse.getBody().jsonPath().getString(tokenType);

    bookStoreInteractionData.setAuthToken(actualAccessToken);
    System.out.println("TOKEN:" + bookStoreInteractionData.getAuthToken());
    if (responseCode == 200) {
      assertThat("Access Token was blank", actualAccessToken, not(blankOrNullString()));
      assertNotNull("Access Token was null", actualAccessToken);

      System.out.println("AccessToken " + actualAccessToken + " : " + "AccessType " + " : " + actualTokenType + " and "
          + " Expected Token Type:" + BookStoreConfig.get("token_type"));

      assertThat("Expected tokenType was blank", actualTokenType, equalTo(BookStoreConfig.get("token_type")));
      assertNotNull("Expected tokenType was null", actualTokenType);

    } else {
      Assert.fail("Response code is unhandled: " + responseCode);
    }
  }

  @When("the user logs in using unregistered credentials")
  public void theUserLogsInUsingUnregisteredCredentials() {
    String email = AuthenticationController.randomAlphanumeric(5) + BookStoreConfig.get("userEmailDomain");
    String password = AuthenticationController.randomAlphanumeric(15);
    bookStoreInteractionData.setUserEmail(email);
    bookStoreInteractionData.setUserPassword(password);
    Response signUpResponse = AuthenticationController.userLogin(bookStoreInteractionData.getUserEmail(),
        bookStoreInteractionData.getUserPassword());
    bookStoreInteractionData.setAuthenticationResult(signUpResponse);

  }

  @When("the user logs in with missing parameters")
  public void theUserLogsInWithMissingParameters() {
    Response signUpResponse = AuthenticationController.userLogin(null, null);
    bookStoreInteractionData.setAuthenticationResult(signUpResponse);
  }

  @Then("the login response code should be {int} and the message should be {string}")
  public void theLoginResponseCodeShouldBeAndTheMessageShouldBe(int responseCode, String message) {
    Assert.assertEquals("Unexpected Response code",
        bookStoreInteractionData.getAuthenticationResult().getStatusCode(),
        responseCode);
    switch (responseCode) {

      case 400:
        Assert.assertEquals("Found Unexpected StatusLine in Response",
            bookStoreInteractionData.getAuthenticationResult().getStatusLine(),
            BookStoreConfig.get("unregistered_credential_statusLine"));
        Assert.assertEquals("Found Unexpected Detail in Response",
            bookStoreInteractionData.getAuthenticationResult().jsonPath().get("detail"),
            BookStoreConfig.get("unregistered_credential_messageDetail"));
        break;

      case 422:
        Assert.assertEquals("Found Unexpected StatusLine in Response",
            bookStoreInteractionData.getAuthenticationResult().getStatusLine(),
            BookStoreConfig.get("missing_credential_statusLine"));
        Assert.assertEquals("Found Unexpected Detail in Response",
            bookStoreInteractionData.getAuthenticationResult().jsonPath().get("detail.get(0).type"),
            BookStoreConfig.get("missing_credential_messageDetail1"));
        Assert.assertEquals("Found Unexpected Detail in Response",
            bookStoreInteractionData.getAuthenticationResult().jsonPath().get("detail.get(0).msg"),
            BookStoreConfig.get("missing_credential_messageDetail2"));
        break;

    }
  }
}
