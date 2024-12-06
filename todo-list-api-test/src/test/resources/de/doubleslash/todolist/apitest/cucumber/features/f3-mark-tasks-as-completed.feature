Feature: Test task completion
  Scenario: Mark created task as done
    Given 1 open tasks are present
    When created tasks should be marked complete
    Then there are no open tasks
    And there are 1 completed tasks