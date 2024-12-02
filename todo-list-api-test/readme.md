# Todo List API Test

This project contains automated tests for the Todo List API using Cucumber and Spring Boot.

## Table of Contents

- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Running the Tests](#running-the-tests)
- [Technologies Used](#technologies-used)

## Getting Started

1. Clone the repository:
   git clone https://github.com/your-username/todo-list-api-test.git
2. Navigate to the project directory:
   cd todo-list-api-test
3. Build the project:
   mvn clean install
4. Run the Tests
   mvn clean verify / mvn test
5. If you want to test without a running application you can remove the value of test.target from
   application-test.properties. Then the controller is loaded on demand by spring boot. Otherwise requests are sent to
   the running application.

## Technologies Used

- Java 21
- Spring Boot 3.3
- Cucumber 7.20