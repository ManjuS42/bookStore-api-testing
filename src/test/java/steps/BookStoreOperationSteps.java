package steps;

import baseController.BookStoreController;
import data.BookStoreInteractionData;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class BookStoreOperationSteps {

  @Before
  public void resetArchive() {
    listOfAllBooksCollection.clear();
  }

  private final BookStoreInteractionData bookStoreInteractionData;

  public BookStoreOperationSteps(BookStoreInteractionData bookStoreInteractionData) {
    this.bookStoreInteractionData = bookStoreInteractionData;
  }

  private static final List<HashMap<String, Object>> listOfAllBooksCollection = new ArrayList<>();
  HashMap<String, Object> generateBookPayload = new HashMap<>();

  @Given("system prepares the request payload to create a new book")
  public void systemPreparesTheRequestPayloadToCreateANewBook() {
    long uniqueId = System.nanoTime();
    generateBookPayload.put("name", "Title of the Book " + uniqueId);
    generateBookPayload.put("author", "Author of the Book " + uniqueId);
    generateBookPayload.put("published_year", uniqueId);
    generateBookPayload.put("book_summary", "Summary of the Book " + uniqueId);
    listOfAllBooksCollection.add(new HashMap<>(generateBookPayload));
  }

  @When("the client creates a new book")
  public void theClientCreatesANewBook() {
    bookStoreInteractionData.setBookAdditionResult(BookStoreController.createBook(generateBookPayload,
        "Bearer " + bookStoreInteractionData.getAuthToken()));
  }

  @Then("the response status should be {int} and the body must contain {string}")
  public void theResponseStatusShouldBeAndTheBodyMustContain(int statusCode, String id) {

    int actualStatusCode = bookStoreInteractionData.getBookAdditionResult().getStatusCode();
    assertThat("Unexpected Status code", actualStatusCode, equalTo(statusCode));

    Assert.assertNotNull("Not found unique ID",
        bookStoreInteractionData.getBookAdditionResult().getBody().jsonPath().get(id));

    generateBookPayload.put("createdBookId",
        bookStoreInteractionData.getBookAdditionResult().getBody().jsonPath().get(id));

    Assert.assertEquals("Unexpected Book name found",
        bookStoreInteractionData.getBookAdditionResult().getBody().jsonPath().get("name"),
        generateBookPayload.get("name"));
    Assert.assertEquals("Unexpected Author name found",
        bookStoreInteractionData.getBookAdditionResult().getBody().jsonPath().get("author"),
        generateBookPayload.get("author"));
    Assert.assertEquals("Unexpected Published year found",
        bookStoreInteractionData.getBookAdditionResult().getBody().jsonPath().get("published_year"),
        generateBookPayload.get("published_year"));
    Assert.assertEquals("Unexpected Book summary found",
        bookStoreInteractionData.getBookAdditionResult().getBody().jsonPath().get("book_summary"),
        generateBookPayload.get("book_summary"));


  }

  @When("the client updates that book’s {string} field")
  public void theClientUpdatesThatBookSField(String name) {
    switch (name.toLowerCase()) {
      case "name":
        generateBookPayload.put("name", "Updated Book name");
        break;

      case "author":
        generateBookPayload.put("author", "Updated Author name");
        break;

      case "book_summary":
        generateBookPayload.put("book_summary", "Updated Book summary");
        break;

      case "published_year":
        generateBookPayload.put("published_year", System.nanoTime());
        break;

      case "noaccesstoken":
        break;

      default:
        throw new IllegalArgumentException("Unsupported field: " + name);
    }

    bookStoreInteractionData.setBookUpdateResult(BookStoreController.editTheBook(generateBookPayload,
        "Bearer " + bookStoreInteractionData.getAuthToken()));

  }

  @Then("the response status should be {int} and the payload should reflect the new {string}")
  public void theResponseStatusShouldBeAndThePayloadShouldReflectTheNew(int statusCode, String name) {
    int actualStatusCode = bookStoreInteractionData.getBookUpdateResult().getStatusCode();
    assertThat("Unexpected Status code", actualStatusCode, equalTo(statusCode));

    Assert.assertEquals("Book name mismatch",
        bookStoreInteractionData.getBookUpdateResult().getBody().jsonPath().get(name),
        generateBookPayload.get("name"));

  }

  @When("the client retrieves the book by its id")
  public void theClientRetrievesTheBookByItsId() {
    bookStoreInteractionData.setBookLookupByIdResult(BookStoreController.getBookDetailsById(generateBookPayload,
        "Bearer " + bookStoreInteractionData.getAuthToken()));

  }

  @Then("the response status should be {int} and the book data should match the latest update")
  public void theResponseStatusShouldBeAndTheBookDataShouldMatchTheLatestUpdate(int statusCode) {
    int actualStatusCode = bookStoreInteractionData.getBookLookupByIdResult().getStatusCode();
    assertThat("Unexpected Status code", actualStatusCode, equalTo(statusCode));

    assertThat("Unexpected Book name found",
        bookStoreInteractionData.getBookLookupByIdResult().getBody().jsonPath().get("name"),
        equalTo(generateBookPayload.get("name")));
    assertThat("Unexpected Author name found",
        bookStoreInteractionData.getBookLookupByIdResult().getBody().jsonPath().get("author"),
        equalTo(generateBookPayload.get("author")));
    assertThat("Unexpected Published year found",
        bookStoreInteractionData.getBookLookupByIdResult().getBody().jsonPath().get("published_year"),
        equalTo(generateBookPayload.get("published_year")));
    assertThat("Book summary  mismatch",
        bookStoreInteractionData.getBookLookupByIdResult().getBody().jsonPath().get("book_summary"),
        equalTo(generateBookPayload.get("book_summary")));
    assertThat("Book id is different to the request",
        bookStoreInteractionData.getBookLookupByIdResult().getBody().jsonPath().get("id"),
        equalTo(generateBookPayload.get("createdBookId")));

  }

  @When("the client deletes the book")
  public void theClientDeletesTheBook() {
    bookStoreInteractionData.setBookRemovalResult(BookStoreController.deleteTheBookById(generateBookPayload.get(
        "createdBookId").toString(), "Bearer " + bookStoreInteractionData.getAuthToken()));

  }

  @Then("the response status should be {int} and the message should be {string}")
  public void theResponseStatusShouldBeAndTheMessageShouldBe(int statusCode, String message) {

    int actualStatusCode = bookStoreInteractionData.getBookRemovalResult().getStatusCode();
    assertThat("Unexpected Status code", actualStatusCode, equalTo(statusCode));
    if (message.equalsIgnoreCase("Deleted successfully")) {
      Assert.assertEquals(bookStoreInteractionData.getBookRemovalResult().getStatusCode(), statusCode);
      Assert.assertEquals(bookStoreInteractionData.getBookRemovalResult().getStatusLine(), "HTTP/1.1 200 OK");
      Assert.assertEquals(bookStoreInteractionData.getBookRemovalResult().getBody().jsonPath().get("message"),
          "Book deleted successfully");
    } else if (message.equalsIgnoreCase("Not Found")) {
      Assert.assertEquals(bookStoreInteractionData.getBookRemovalResult().getStatusCode(), 404);
      Assert.assertEquals(bookStoreInteractionData.getBookRemovalResult().getStatusLine(), "HTTP/1.1 404 Not Found");
      Assert.assertEquals(bookStoreInteractionData.getBookRemovalResult().getBody().jsonPath().get("detail"),
          "Book not found");
    }

  }

  @When("the client deletes the same book again")
  public void theClientDeletesTheSameBookAgain() {
    bookStoreInteractionData.setBookRemovalResult(BookStoreController.deleteTheBookById(generateBookPayload.get(
        "createdBookId").toString(), "Bearer " + bookStoreInteractionData.getAuthToken()));
  }

  @Given("the client has just created a book")
  public void theClientHasJustCreatedABook() {
  }

  @When("the client updates the {string} to {string}")
  public void theClientUpdatesTheTo(String field, String value) {
    switch (field.toLowerCase()) {
      case "name":
        generateBookPayload.put("name", value);
        break;
      case "author":
        generateBookPayload.put("author", value);
        break;
      case "book_summary":
        generateBookPayload.put("book_summary", value);
        break;
      case "published_year":
        generateBookPayload.put("published_year", Integer.parseInt(value));
        break;
      default:
        throw new IllegalArgumentException("Unsupported field: " + field);
    }
    bookStoreInteractionData.setBookUpdateResult(BookStoreController.editTheBook(generateBookPayload,
        "Bearer " + bookStoreInteractionData.getAuthToken()));
  }

  @Then("the response status should be {int}")
  public void theResponseStatusShouldBe(int statusCode) {
    int actualStatusCode = bookStoreInteractionData.getBookUpdateResult().getStatusCode();
    assertThat("Unexpected Status code", actualStatusCode, equalTo(statusCode));
  }

  @And("a follow-up GET should return {string} for {string}")
  public void aFollowUpGETShouldReturnFor(String field, String value) {
    switch (field) {
      case "name":
        System.out.println(
            "Field" + bookStoreInteractionData.getBookUpdateResult().getBody().jsonPath().get(field) + "value: "
                + generateBookPayload.get("name"));
        Assert.assertEquals("Not found Book name",
            bookStoreInteractionData.getBookUpdateResult().getBody().jsonPath().get(field),
            generateBookPayload.get("name"));
        Assert.assertEquals("Not found Book name",
            bookStoreInteractionData.getBookUpdateResult().getBody().jsonPath().get(field),
            value);
        break;
      case "author":
        Assert.assertEquals("Not found Book author",
            bookStoreInteractionData.getBookUpdateResult().getBody().jsonPath().get(field),
            generateBookPayload.get("author"));
        Assert.assertEquals("Not found Book author",
            bookStoreInteractionData.getBookUpdateResult().getBody().jsonPath().get(field),
            value);
        break;

      case "book_summary":
        Assert.assertEquals("Not found Book summary",
            bookStoreInteractionData.getBookUpdateResult().getBody().jsonPath().get(field),
            generateBookPayload.get("book_summary"));
        Assert.assertEquals("Not found Book summary",
            bookStoreInteractionData.getBookUpdateResult().getBody().jsonPath().get(field),
            value);
        break;

      case "published_year":
        Assert.assertEquals("Published year mismatch",
            bookStoreInteractionData.getBookUpdateResult().getBody().jsonPath().get(field),
            generateBookPayload.get("published_year"));
        Number actualYear = bookStoreInteractionData.getBookUpdateResult().getBody().jsonPath().get(field);
        int expectedYear = Integer.parseInt(value);
        Assert.assertEquals("Unexpected Published year found", actualYear.intValue(), expectedYear);
        break;
      default:
        Assert.fail("Unknown field: " + field);
    }
  }

  @Given("the client has added {int} new books")
  public void theClientHasAddedNewBooks(int noOfBooks) {
    for (int i = 0; i < noOfBooks; i++) {
      long uid = System.nanoTime();
      HashMap<String, Object> book = new HashMap<>();
      book.put("name", "Name of the Book " + uid);
      book.put("author", "Author of the Book " + uid);
      book.put("published_year", uid);
      book.put("book_summary", "Summary for the book " + uid);

      bookStoreInteractionData.setBookAdditionResult(BookStoreController.createBook(book,
          "Bearer " + bookStoreInteractionData.getAuthToken()));

      int id = bookStoreInteractionData.getBookAdditionResult().getBody().jsonPath().get("id");
      book.put("createdBookId", id);
      listOfAllBooksCollection.add(new HashMap<>(book));
    }
  }

  @When("the client fetches the complete book list")
  public void theClientFetchesTheCompleteBookList() {
    bookStoreInteractionData.setAllBooksResponseList(BookStoreController.getAllBooks(
        "Bearer " + bookStoreInteractionData.getAuthToken()));
  }

  @Then("the response should include the {int} newly-created book IDs")
  public void theResponseShouldIncludeTheNewlyCreatedBookIDs(int noOfBooks) {

    List<Integer> expectedBookIds = new ArrayList<>();
    for (Map<String, Object> book : listOfAllBooksCollection) {
      Integer id = (Integer) book.get("createdBookId");
      expectedBookIds.add(id);
    }
    List<Integer> actualBookId =
        new ArrayList<>(bookStoreInteractionData.getAllBooksResponseList().jsonPath().getList("id", Integer.class));

    Assert.assertEquals("Number of books expected not found", noOfBooks, expectedBookIds.size());

    for (Integer bookID : expectedBookIds) {
      Assert.assertTrue("Book ID missing from the List", actualBookId.contains(bookID));
    }
  }

  @Given("the client has created a book")
  public void theClientHasCreatedABook() {
  }

  @Then("the response status should be {int} and the error message should mention {string}")
  public void theResponseStatusShouldBeAndTheErrorMessageShouldMention(int statusCode, String message) {
    Assert.assertEquals(bookStoreInteractionData.getBookLookupByIdResult().getStatusCode(), statusCode);
    Assert.assertEquals(bookStoreInteractionData.getBookLookupByIdResult().getStatusLine(), message);

  }

  @When("the client requests GET book by invalid ID")
  public void theClientRequestsGETBookByInvalidID() {
    bookStoreInteractionData.setBookLookupByIdResult(BookStoreController.getBookDetailsByInvalidId(generateBookPayload,
        "Bearer " + bookStoreInteractionData.getAuthToken()));
  }
}
