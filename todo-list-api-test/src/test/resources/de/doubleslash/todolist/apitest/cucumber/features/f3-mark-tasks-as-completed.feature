Feature: Test Endpoints
  Scenario: Mark created task as done
    Given 1 open tasks are present
    When task should be marked complete
    Then the server should respond with 200 on PATCH endpoint
    And there are no open tasks
    And there are completed tasks