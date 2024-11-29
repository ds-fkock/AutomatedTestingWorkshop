package de.doubleslash.todolist.apitest.cucumber.stepdefinitions;

import de.doubleslash.todolist.apitest.cucumber.CucumberSpringConfiguration;
import de.doubleslash.todolist.model.Task;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@CucumberContextConfiguration
public class TaskStepDefinitions extends CucumberSpringConfiguration {

   private ResponseEntity<List<Task>> response;

   @Given("{int} tasks are open")
   public void tasksAreOpen(final int arg0) {
      Task task;
      for (int i = 0; i < arg0; i++) {
         task = new Task();
         task.setId(i + 1L);
         task.setTitle("Task " + (i + 1));
         task.setStatus("OPEN");
         testRestTemplate.postForEntity("/tasks", task, Task.class);
      }
   }

   @When("all tasks are requested and {int} are open")
   public void allTasksAreRequestedAndAreOpen(final int arg0) {
      response = testRestTemplate.exchange(
            "/tasks",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<Task>>() {}
      );
      final List<Task> tasks = response.getBody();
      assertThat(tasks).isNotNull();
      final long openTasksCount = tasks.stream()
                                       .filter(task -> task.getStatus().equalsIgnoreCase("OPEN"))
                                       .count();
      assertThat(openTasksCount).isEqualTo(arg0);
   }

   @Then("the server should respond with {int}")
   public void theServerShouldRespondWith(final int arg0) {
      assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(arg0));
   }
}