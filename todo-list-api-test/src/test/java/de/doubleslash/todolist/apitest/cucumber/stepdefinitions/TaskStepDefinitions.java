package de.doubleslash.todolist.apitest.cucumber.stepdefinitions;

import de.doubleslash.todolist.apitest.cucumber.CucumberSpringConfiguration;
import de.doubleslash.todolist.model.Task;
import de.doubleslash.todolist.model.TaskStatus;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@CucumberContextConfiguration
public class TaskStepDefinitions extends CucumberSpringConfiguration {

   private static final Logger logger = LoggerFactory.getLogger(TaskStepDefinitions.class);

   private ResponseEntity<List<Task>> getResponse;
   private ResponseEntity<Task> postResponse;
   private ResponseEntity<Void> patchResponse;
   private List<Task> tasks;
   private List<Long> createdTaskIds;

   private static final String TASKS_API = "/tasks";

   @Before
   public void setUp() {
      tasks = new ArrayList<>();
      createdTaskIds = new ArrayList<>();
   }

   @Given("{int} open tasks are present")
   public void tasksAreOpen(final int arg0) {
      for (int i = 0; i < arg0; i++) {
         final String title = "Task " + i;
         postResponse = createTask(title, TaskStatus.OPEN);
         addCreatedTaskToTaskList();
      }
   }

   @Given("api endpoint for adding todos is present")
   public void apiEndpointForAddingTodos() {
      postResponse = createTask("apiEndpointForAddingTodos", TaskStatus.OPEN);
      addCreatedTaskToTaskList();
   }

   @When("a task with no title is added")
   public void aTaskWithNoTitleIsAdded() {
      postResponse = createTask(null, TaskStatus.OPEN);
   }

   @When("all tasks are requested")
   public void allTasksAreRequestedAndAreOpen() {
      getAllTasks();
      assertThat(tasks).isNotNull();
   }

   @When("task should be marked complete")
   public void taskShouldBeMarkedComplete() {
      for (final Long taskId : createdTaskIds) {
         markTaskAsDone(taskId);
         assertThat(patchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
      }
   }

   @Then("there are {int} open tasks")
   public void thereAreAtLeastOpenTasks(final int arg0) {
      final long openTasksCount = getCountByTaskStatus(TaskStatus.OPEN);
      assertThat(openTasksCount).isEqualTo(arg0);
   }

   @Then("the server should respond with {int} on {string} endpoint")
   public void theServerShouldRespondWithOnPostEndpoint(final int arg0, final String arg1) {
      switch (arg1) {
         case "POST":
            assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(arg0));
            break;
         case "GET":
            assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(arg0));
            break;
         case "PATCH":
            assertThat(patchResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(arg0));
            break;
         default:
            fail("Invalid endpoint: " + arg1);
      }
   }

   @Then("the server should respond with {int} on GET endpoint")
   public void theServerShouldRespondWith(final int arg0) {
      assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(arg0));
   }

   @Then("the server should respond with {int} on PATCH endpoint")
   public void theServerShouldRespondWithOnPATCHEndpoint(final int arg0) {
      assertThat(patchResponse.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(arg0));
   }

   @Then("there are no open tasks")
   public void thereAreNoOpenTasks() {
      getAllTasks();
      assertThat(tasks).isNotNull();
      final long countOpenTasks = getCountByTaskStatus(TaskStatus.OPEN);
      assertThat(countOpenTasks).isZero();
   }

   @Then("there are {int} completed tasks")
   public void thereAreCompletedTasks(final int arg0) {
      getAllTasks();
      assertThat(tasks).isNotNull();
      final long countDoneTasks = getCountByTaskStatus(TaskStatus.DONE);
      assertThat(countDoneTasks).isEqualTo(arg0);
   }

   @After
   private void cleanup() {
      patchResponse = testRestTemplate.exchange(testTarget + TASKS_API, HttpMethod.PATCH, null, Void.class);
      createdTaskIds.clear();
      tasks.clear();
   }

   private ResponseEntity<Task> createTask(final String title, final TaskStatus status) {
      final Task task = new Task();
      task.setTitle(title);
      task.setStatus(status);
      return testRestTemplate.postForEntity(testTarget + TASKS_API, task, Task.class);
   }

   private void addCreatedTaskToTaskList() {
      if (postResponse.getStatusCode() == HttpStatus.OK && postResponse.getBody() != null) {
         createdTaskIds.add(postResponse.getBody().getId());
      } else {
         logger.error("Unexpected status code: {}", postResponse.getStatusCode());
         fail("Unexpected status code: " + postResponse.getStatusCode());
      }
   }

   private void getAllTasks() {
      getResponse = testRestTemplate.exchange(testTarget + TASKS_API, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Task>>() {
            });
      tasks = getResponse.getBody();
   }

   private long getCountByTaskStatus(final TaskStatus taskStatus) {
      return tasks.stream()
                  .filter(task -> task.getStatus() == taskStatus && createdTaskIds.contains(
                        task.getId()))
                  .count();
   }

   private void markTaskAsDone(final Long taskId) {
      final HttpEntity<Void> entity = new HttpEntity<>(null, new HttpHeaders());
      patchResponse = testRestTemplate.exchange(testTarget + TASKS_API + "/" + taskId.toString(), HttpMethod.PATCH,
            entity, Void.class);
   }
}