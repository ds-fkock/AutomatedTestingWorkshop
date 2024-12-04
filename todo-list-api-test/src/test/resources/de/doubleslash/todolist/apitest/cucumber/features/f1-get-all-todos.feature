Feature: Test Get Endpoint
  Scenario: Get request for retrieving all todos
    Given 2 open tasks are present
    When all tasks are requested
    Then there are 2 open tasks