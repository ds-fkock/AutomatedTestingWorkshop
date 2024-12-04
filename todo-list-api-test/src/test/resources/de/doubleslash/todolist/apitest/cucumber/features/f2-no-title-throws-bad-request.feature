Feature: Test Bad Request on creation
  Scenario: Test Bad Request when adding task without title
    Given api endpoint for adding todos is present
    When a task with no title is added
    Then the server should respond with 400 on "POST" endpoint