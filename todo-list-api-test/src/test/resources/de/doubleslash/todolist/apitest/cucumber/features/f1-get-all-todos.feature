Feature: Test Endpoints
  Scenario: Get request for retrieving all todos
    Given 2 open tasks are present
    When all tasks are requested
    Then the server should respond with 200 on GET endpoint
    And there are at least 2 open tasks