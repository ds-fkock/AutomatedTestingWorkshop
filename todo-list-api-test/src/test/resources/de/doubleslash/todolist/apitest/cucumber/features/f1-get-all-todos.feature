Feature: Test Endpoints
  Scenario: Get request for retrieving all todos
    Given 2 open tasks are added
    When all tasks are requested
    Then there are at least 2 open tasks