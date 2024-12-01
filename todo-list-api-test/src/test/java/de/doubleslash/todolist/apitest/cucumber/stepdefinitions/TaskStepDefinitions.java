package de.doubleslash.todolist.apitest.cucumber.stepdefinitions;

import de.doubleslash.todolist.apitest.cucumber.CucumberSpringConfiguration;
import de.doubleslash.todolist.model.Task;
import de.doubleslash.todolist.model.TaskStatus;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@CucumberContextConfiguration
public class TaskStepDefinitions extends CucumberSpringConfiguration {

   private ResponseEntity<List<Task>> getResponse;
   private ResponseEntity<Task> postResponse;
   private ResponseEntity<Void> patchResponse;
   private List<Task> tasks;
   private List<Long> createdTaskIds;

   @Given("{int} open tasks are present")
   public void tasksAreOpen(final int arg0) {
      createdTaskIds = new ArrayList<>();
      for (int i = 0; i < arg0; i++) {
         final String title = "Task " + i;
         createTaskWithTitle(title);
      }
   }

   private void createTaskWithTitle(final String title) {
      final Task task = new Task();
      task.setTitle(title);
      task.setStatus(TaskStatus.OPEN);
      postResponse = testRestTemplate.postForEntity("http://localhost:8080/tasks", task, Task.class);
      if (postResponse.getStatusCode() == HttpStatus.OK && postResponse.getBody() != null) {
         createdTaskIds.add(postResponse.getBody().getId());
      } else {
         fail("Unexpected status code: " + postResponse.getStatusCode());
      }
   }

   @Given("api endpoint for adding users is present")
   public void apiEndpointForAddingUsers() {
      final Task task = new Task();
      task.setTitle("Validation Task");
      task.setStatus(TaskStatus.OPEN);
      postResponse = testRestTemplate.postForEntity("http://localhost:8080/tasks", task, Task.class);
      assertThat(postResponse.getStatusCode()).isNotEqualTo(HttpStatus.NOT_FOUND);
   }

   @When("a task with no title is added")
   public void aTaskWithNoTitleIsAdded() {
      final Task task = new Task();
      task.setTitle(null);
      task.setStatus(TaskStatus.OPEN);
      postResponse = testRestTemplate.postForEntity("http://localhost:8080/tasks", task, Task.class);
   }

   @When("all tasks are requested")
   public void allTasksAreRequestedAndAreOpen() {
      getResponse = testRestTemplate.exchange(
            "http://localhost:8080/tasks",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Task>>() {
            }
      );
      tasks = getResponse.getBody();
      assertThat(tasks).isNotNull();
   }

   @Then("there are at least {int} open tasks")
   public void thereAreAtLeastOpenTasks(final int arg0) {
      final long openTasksCount = tasks.stream()
                                       .filter(task -> task.getStatus().equals(TaskStatus.OPEN))
                                       .count();
      //TODO does not really make sense since there could already be tasks if it is a prod application
      assertThat(openTasksCount).isGreaterThanOrEqualTo(arg0);
   }

   @Then("the server should respond with {int} on POST endpoint")
   public void theServerShouldRespondWithOnPostEndpoint(final int arg0) {
      assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(arg0));
   }

   @Then("the server should respond with {int} on GET endpoint")
   public void theServerShouldRespondWith(final int arg0) {
      assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(arg0));
   }

   @When("task should be marked complete")
   public void taskShouldBeMarkedComplete() {
      for (final Long taskId : createdTaskIds) {
         final HttpEntity<Void> entity = new HttpEntity<>(null, new HttpHeaders());
         patchResponse = testRestTemplate.exchange(
               "http://localhost:8080/tasks/" + taskId.toString(),
               HttpMethod.PATCH,
               entity,
               Void.class
         );
         assertThat(patchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
      }
   }

   @Then("the server should respond with {int} on PATCH endpoint")
   public void theServerShouldRespondWithOnPATCHEndpoint(final int arg0) {
      assertThat(patchResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(arg0));
   }

   @And("there are no open tasks")
   public void thereAreNoOpenTasks() {
      getResponse = testRestTemplate.exchange(
            "http://localhost:8080/tasks",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Task>>() {
            }
      );
      tasks = getResponse.getBody();
      assertThat(tasks).isNotNull();
      final long countOpenTasks = tasks.stream().filter(task -> task.getStatus() == TaskStatus.OPEN).count();
      assertThat(countOpenTasks).isZero();
   }

   @And("there are completed tasks")
   public void thereAreCompletedTasks() {
      getResponse = testRestTemplate.exchange(
            "http://localhost:8080/tasks",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Task>>() {
            }
      );
      tasks = getResponse.getBody();
      assertThat(tasks).isNotNull();
      final long countDoneTasks = tasks.stream().filter(task -> task.getStatus() == TaskStatus.DONE).count();
      assertThat(countDoneTasks).isZero();
   }
}