package data;

import io.restassured.response.Response;
import lombok.Data;

@Data
public class BookStoreInteractionData {

  private String userEmail;
  private String userPassword;
  private String authToken;

  private Response registrationResult;
  private Response authenticationResult;
  private Response bookAdditionResult;
  private Response bookUpdateResult;
  private Response bookLookupByIdResult;
  private Response bookRemovalResult;

  private Response allBooksResponseList;
}
