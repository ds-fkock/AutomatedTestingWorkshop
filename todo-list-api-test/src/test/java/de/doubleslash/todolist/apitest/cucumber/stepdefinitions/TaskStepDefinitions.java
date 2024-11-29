package de.doubleslash.todolist.apitest.cucumber.stepdefinitions;

import de.doubleslash.todolist.apitest.cucumber.CucumberSpringConfiguration;
import de.doubleslash.todolist.model.Task;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.apache.coyote.Response;
import org.apache.hc.core5.http.HttpResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
public class TaskStepDefinitions extends CucumberSpringConfiguration {

   private ResponseEntity<List<Task>> response;
   private ResponseEntity<Task> postResponse;
   private List<Task> tasks;

   @Given("{int} open tasks are added")
   public void tasksAreOpen(final int arg0) {
      Task task;
      for (int i = 0; i < arg0; i++) {
         task = new Task();
         task.setId(i + 1L);
         task.setTitle("Task " + (i + 1));
         task.setStatus("OPEN");
         postResponse = testRestTemplate.postForEntity("http://localhost:8080/tasks", task, Task.class);
      }
   }

   @Given("nothing")
   public void nothing() {
   }

   @When("a task with no title is added")
   public void aTaskWithNoTitleIsAdded() {
      final Task task = new Task();
      task.setTitle(null);
      task.setStatus("OPEN");
      postResponse = testRestTemplate.postForEntity("http://localhost:8080/tasks", task, Task.class);
   }

   @When("all tasks are requested")
   public void allTasksAreRequestedAndAreOpen() {
      response = testRestTemplate.exchange(
            "http://localhost:8080/tasks",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Task>>() {}
      );
      tasks = response.getBody();
      assertThat(tasks).isNotNull();
   }

   @Then("the server should respond with {int}")
   public void theServerShouldRespondWith(final int arg0) {
      //TODO refactor this - maybe rather integrate response code assertions in steps
      if (response != null) {
         assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(arg0));
      }
      else {
         assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(arg0));
      }
   }

   @Then("there are at least {int} open tasks")
   public void thereAreAtLeastOpenTasks(final int arg0) {
      final long openTasksCount = tasks.stream()
                                       .filter(task -> task.getStatus().equalsIgnoreCase("OPEN"))
                                       .count();
      //TODO does not really make sense since there could already be tasks if it is a prod application
      assertThat(openTasksCount).isGreaterThan(arg0);
   }
}