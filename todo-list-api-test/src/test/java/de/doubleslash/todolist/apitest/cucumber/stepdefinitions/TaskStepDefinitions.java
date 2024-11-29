package de.doubleslash.todolist.apitest.cucumber.stepdefinitions;

import de.doubleslash.todolist.apitest.cucumber.CucumberSpringConfiguration;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
public class TaskStepDefinitions extends CucumberSpringConfiguration {

    private ResponseEntity<List> response;

    @Given("{int} tasks are open")
    public void tasksAreOpen(int arg0) {
        response = testRestTemplate.getForEntity("/tasks", List.class);
    }

    @When("all tasks are requested")
    public void allTasksAreRequested() {
        response = testRestTemplate.getForEntity("/tasks", List.class);
    }

    @Then("the server should respond with {int}")
    public void theServerShouldRespondWith(int arg0) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}