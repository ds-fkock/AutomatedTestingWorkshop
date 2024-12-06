package de.doubleslash.todolist.apitest.cucumber.stepdefinitions;

import de.doubleslash.todolist.apitest.cucumber.CucumberSpringConfiguration;
import de.doubleslash.todolist.model.Task;
import de.doubleslash.todolist.model.TaskStatus;
import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@CucumberContextConfiguration
public class TaskApiStepDefinitions extends CucumberSpringConfiguration {

   private static final Logger logger = LoggerFactory.getLogger(TaskApiStepDefinitions.class);

   @Given("{int} open tasks are present")
   public void tasksAreOpen(final int taskCount) {
      for (int i = 0; i < taskCount; i++) {
         final String title = "Task " + i;
         taskApiService.createTask(title, TaskStatus.OPEN);
      }
   }

   @Given("api endpoint for adding todos is present")
   public void apiEndpointForAddingTodos() {
      taskApiService.createTask("apiEndpointForAddingTodos", TaskStatus.OPEN);
   }

   @When("a task with no title is added")
   public void aTaskWithNoTitleIsAdded() {
      taskApiService.createTask(null, TaskStatus.OPEN);
   }

   @When("all tasks are requested")
   public void allTasksAreRequested() {
      final List<Task> tasks = taskApiService.getAllTasks();
      assertThat(tasks).isNotNull();
   }

   @When("created tasks should be marked complete")
   public void taskShouldBeMarkedComplete() {
      for (final Long taskId : taskApiService.getCreatedTaskIds()) {
         taskApiService.markTaskAsDone(taskId);
         assertThat(taskApiService.getPatchResponse().getStatusCode()).isEqualTo(HttpStatus.OK);
      }
   }

   @Then("there are {int} open tasks")
   public void thereAreOpenTasks(final int taskCount) {
      final long openTasksCount = taskApiService.getCountByTaskStatus(TaskStatus.OPEN);
      assertThat(openTasksCount).isEqualTo(taskCount);
   }

   @Then("the server should respond with {int} on {string} endpoint")
   public void theServerShouldRespondWithOnPostEndpoint(final int responseCode, final String endpoint) {
      switch (endpoint) {
      case "POST":
         assertThat(taskApiService.getPostResponse().getStatusCode()).isEqualTo(HttpStatusCode.valueOf(responseCode));
         break;
      case "GET":
         assertThat(taskApiService.getGetResponse().getStatusCode()).isEqualTo(HttpStatusCode.valueOf(responseCode));
         break;
      case "PATCH":
         assertThat(taskApiService.getPatchResponse().getStatusCode()).isEqualTo(HttpStatusCode.valueOf(responseCode));
         break;
      default:
         logger.error("Invalid endpoint: {}", endpoint);
         fail("Invalid endpoint: " + endpoint);
      }
   }

   @Then("the server should respond with {int} on GET endpoint")
   public void theServerShouldRespondWithOnGETEndpoint(final int responseCode) {
      assertThat(taskApiService.getGetResponse().getStatusCode()).isEqualTo(HttpStatusCode.valueOf(responseCode));
   }

   @Then("the server should respond with {int} on PATCH endpoint")
   public void theServerShouldRespondWithOnPATCHEndpoint(final int responseCode) {
      assertThat(taskApiService.getPatchResponse().getStatusCode()).isEqualTo(HttpStatusCode.valueOf(responseCode));
   }

   @Then("there are no open tasks")
   public void thereAreNoOpenTasks() {
      final long countOpenTasks = taskApiService.getCountByTaskStatus(TaskStatus.OPEN);
      assertThat(countOpenTasks).isZero();
   }

   @Then("there are {int} completed tasks")
   public void thereAreCompletedTasks(final int taskCount) {
      final long countDoneTasks = taskApiService.getCountByTaskStatus(TaskStatus.DONE);
      assertThat(countDoneTasks).isEqualTo(taskCount);
   }

   @After
   public void cleanup() {
      taskApiService.markAllTasksAsDone();
      taskApiService.getCreatedTaskIds().clear();
   }
}