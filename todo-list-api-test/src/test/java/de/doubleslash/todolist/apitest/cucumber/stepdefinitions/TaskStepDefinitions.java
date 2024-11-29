package de.doubleslash.todolist.apitest.cucumber.stepdefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;

@CucumberContextConfiguration
public class TaskStepDefinitions {

    @Given("{int} tasks are open")
    public void tasksAreOpen(int arg0) {
    }

    @When("all tasks are requested")
    public void allTasksAreRequested() {
    }

    @Then("the server should respond with {int}")
    public void theServerShouldRespondWith(int arg0) {
    }
}