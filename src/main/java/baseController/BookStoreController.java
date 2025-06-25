package baseController;

import constants.ApiEndPoints;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.BookStoreConfig;

import java.util.HashMap;

public final class BookStoreController {


  private static RequestSpecification request(String token) {
    return RestAssured.given()
        .baseUri(BookStoreConfig.getBaseUri())
        .header("Authorization", token)
        .contentType(ContentType.JSON);
  }

  public static Response createNewBook(HashMap<String, Object> generateBookPayload, String accessToken) {

    return request(accessToken).body(generateBookPayload)
        .when()
        .post(ApiEndPoints.CREATE_BOOK_ENDPOINT)
        .then()
        .log()
        .all()
        .extract()
        .response();
  }

  public static Response updateOldBook(HashMap<String, Object> generateBookPayload, String accessToken) {

    return request(accessToken).body(generateBookPayload)
        .pathParam("book_id", generateBookPayload.get("createdBookId"))
        .when()
        .put(ApiEndPoints.FIND_BOOK_BY_ID_ENDPOINT)
        .then()
        .log()
        .all()
        .extract()
        .response();
  }

  public static Response fetchBookDetailsById(HashMap<String, Object> generateBookPayload, String accessToken) {


    return request(accessToken).body(generateBookPayload)
        .pathParam("book_id", generateBookPayload.get("createdBookId"))
        .header("Authorization", accessToken)
        .when()
        .get(ApiEndPoints.FIND_BOOK_BY_ID_ENDPOINT)
        .then()
        .log()
        .all()
        .extract()
        .response();
  }

  public static Response fetchAllBooksData(String accessToken) {

    return request(accessToken).header("Authorization", accessToken)
        .when()
        .get(ApiEndPoints.CREATE_BOOK_ENDPOINT)
        .then()
        .log()
        .all()
        .extract()
        .response();
  }


  public static Response deleteBookByBookID(String id, String accessToken) {

    return request(accessToken).pathParam("book_id", id)
        .header("Authorization", accessToken)
        .when()
        .delete(ApiEndPoints.FIND_BOOK_BY_ID_ENDPOINT)
        .then()
        .log()
        .all()
        .extract()
        .response();
  }

  public static Response fetchBookDetailsByInvalidBookId(HashMap<String, Object> generateBookPayload,
      String accessToken) {

    return request(accessToken).body(generateBookPayload)
        .pathParam("book_id", "randomid")
        .header("Authorization", accessToken)
        .when()
        .get(ApiEndPoints.FIND_BOOK_BY_ID_ENDPOINT)
        .then()
        .log()
        .all()
        .extract()
        .response();
  }
}





