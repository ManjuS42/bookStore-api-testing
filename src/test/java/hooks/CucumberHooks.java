package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import utils.BookStoreConfig;


public class CucumberHooks {

  @Before
  public void setup(Scenario scenario) {
    System.out.println("Starting scenario: " + scenario.getName());

  }

  @BeforeAll
  public static boolean isServerUpAndRunning() {
    try {
      RestAssured.baseURI = BookStoreConfig.get("bookstore.service.url");
      io.restassured.response.Response response =
          RestAssured.given().header("Content-Type", "application/json").get("/signup");
      return response.getStatusCode() == 200;
    } catch (Exception e) {
      return false;
    }
  }

  @After
  public void tearDown(Scenario scenario) {
    if (scenario.isFailed()) {
      System.out.println("Scenario failed: " + scenario.getName());
    } else {
      System.out.println("Scenario passed: " + scenario.getName());
    }
  }

}
