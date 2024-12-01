Feature: Test Get Endpoint multiple times
  Scenario Outline: Get request for retrieving all todos
    Given <amount_of_tasks> open tasks are present
    When all tasks are requested
    Then there are <amount_of_tasks> open tasks
  Examples:
    | amount_of_tasks |
    | 10              |
    | 5               |