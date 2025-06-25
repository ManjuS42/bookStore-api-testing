package baseController;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import constants.ApiEndPoints;
import io.restassured.specification.RequestSpecification;
import utils.BookStoreConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class AuthenticationController {


  private static final String ALPHA_NUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final Random RANDOM = new Random();

  private static Response callAuthenticationEndpoint(String endpoint, String email, String password) {
    RequestSpecification request =
        given().contentType(ContentType.JSON).baseUri(BookStoreConfig.getBaseUri()).log().all();

    Map<String, Object> payload = new HashMap<>();
    if (email != null)
      payload.put("email", email);
    if (password != null)
      payload.put("password", password);

    if (!payload.isEmpty()) {
      request.body(payload);
    }

    return request.when().post(endpoint).then().log().all().extract().response();
  }

  public static String randomAlphanumeric(int length) {
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      sb.append(ALPHA_NUMERIC.charAt(RANDOM.nextInt(ALPHA_NUMERIC.length())));
    }
    return sb.toString();
  }

  public static Response newUserSignUp(String email, String password) {
    return callAuthenticationEndpoint(ApiEndPoints.USER_SIGNUP, email, password);
  }

  public static Response userLogin(String email, String password) {
    return callAuthenticationEndpoint(ApiEndPoints.USER_LOGIN, email, password);
  }

}

