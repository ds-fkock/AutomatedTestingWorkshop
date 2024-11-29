Feature: Test Endpoints
  Scenario: Test Bad Request when adding task without title
    Given nothing
    When a task with no title is added
    Then the server should respond with 400