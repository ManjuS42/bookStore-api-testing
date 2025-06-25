@BookStoreOperationFeature
Feature:  Perform all CRUD operations on books store API via authenticated user

  Background:
    Given the user prepares valid signup credentials
    When the user sends a signup request with new credentials
    Then the response code should be 200 and the message should be "User created successfully"
    When the user logs in using the same credentials
    Then the response code should be 200 and the message should contain "access_token" with "token_type"

  @Positive @Sanity @CRUD_TEST
  Scenario: Create a book, update it, fetch it, then delete it

    Given system prepares the request payload to create a new book
    When the client creates a new book
    Then the response status should be 200 and the body must contain "id"

    When the client updates that book’s "name" field
    Then the response status should be 200 and the payload should reflect the new "name"

    When the client retrieves the book by its id
    Then the response status should be 200 and the book data should match the latest update

    When the client deletes the book
    Then the response status should be 200 and the message should be "Deleted successfully"

    When the client deletes the same book again
    Then the response status should be 404 and the message should be "Not Found"

  @Regression @Positive @UpdateBook
  Scenario: Update a single field and verify if value persists
    Given system prepares the request payload to create a new book
    When the client creates a new book
    Then the response status should be 200 and the body must contain "id"

    When  the client updates the "name" to "Updating the Book Name"
    Then  the response status should be 200
    And   a follow-up GET should return "name" for "Updating the Book Name"

    When  the client updates the "author" to "Martin Author"
    Then  the response status should be 200
    And   a follow-up GET should return "author" for "Martin Author"

    When  the client updates the "book_summary" to "Book Store API EDIT Operation"
    Then  the response status should be 200
    And   a follow-up GET should return "book_summary" for "Book Store API EDIT Operation"

    When  the client updates the "published_year" to "2022"
    Then  the response status should be 200
    And   a follow-up GET should return "published_year" for "2022"

  @Regression @Positive @BooksList
  Scenario: List endpoint returns all recently-added books
    Given the client has added 5 new books
    When  the client fetches the complete book list
    Then  the response should include the 5 newly-created book IDs

  @Regression @Negative @GETBookByInvalidID
  Scenario: Attempt to retrieve a book with a non-existent id
    Given system prepares the request payload to create a new book
    When the client creates a new book
    Then the response status should be 200 and the body must contain "id"
    When  the client requests GET book by invalid ID
    Then  the response status should be 422 and the error message should mention "HTTP/1.1 422 Unprocessable Entity"
