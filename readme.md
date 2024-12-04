# Repository for Automated Testing Workshop Tractus-X Days

This repository can be used as a starting point for implementing automated tests.

## Table of Contents

- [Purpose](#purpose)
- [Getting Started](#getting-started)
- [Technologies Used](#technologies-used)

## Purpose

The purpose of this repository is to provide a comprehensive example of automated testing for a Spring Boot application.
The repository consists of two main projects:

1. A simple Spring Boot application for managing tasks. This application serves as the target for the API tests.
2. An API test project that contains several API tests for the given application. The tests are written using Cucumber,
   a popular behavior-driven development (BDD) framework for testing web applications.

The repository aims to demonstrate the following:

- How to set up a Spring Boot application for testing.
- How to write API tests using Cucumber.
- How to structure and organize API tests for better maintainability and readability.
- How to handle different test scenarios, such as positive and negative test cases.
- How to handle assertions and verify the expected results.

The repository also includes detailed instructions and readmes in the projects' subfolders to guide you through the
setup and execution of the tests. This will help you understand the code, configurations, and best practices for
automated testing in a Spring Boot application using Cucumber.

Feel free to explore the repository and use it as a starting point for your own automated testing projects. Happy
testing!

## Getting Started

For detailed instructions on how to get the project running take a look into the readmes
in the projects sub folders.

## Technologies Used

- Java 21
- Spring Boot 3.3
- Cucumber 7.20

## Workshop Tasks
1. Write a feature file which tests the Patch Endpoint for a given ID which is non-existant. The tested scenario should be that the task ID is not found. Reuse as much steps as possible.
2. There is an untested filter endpoint in the application for retrieving todos based on their title. Write a cucumber test which validates the functionality of the endpoint.
