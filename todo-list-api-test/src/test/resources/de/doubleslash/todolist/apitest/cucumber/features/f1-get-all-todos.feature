Feature: API Endpoints Testen
  Scenario: Get request for retrieving all todos
    Given 2 tasks are open
    When all tasks are requested
    Then the server should respond with 200