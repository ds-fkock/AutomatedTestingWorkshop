package de.doubleslash.todolist.apitest.cucumber;

import de.doubleslash.todolist.model.Task;
import de.doubleslash.todolist.model.TaskStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

import static org.assertj.core.api.Assertions.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CucumberSpringConfiguration {

   private static final Logger logger = LoggerFactory.getLogger(CucumberSpringConfiguration.class);

   private static final String TASKS_API = "/tasks";

   @Autowired
   protected TestRestTemplate testRestTemplate;

   @Value(("${test.target}"))
   protected String testTarget;

   protected ResponseEntity<List<Task>> getResponse;
   protected ResponseEntity<Task> postResponse;
   protected ResponseEntity<Void> patchResponse;
   protected List<Task> tasks;
   protected List<Long> createdTaskIds;

   protected ResponseEntity<Task> createTask(final String title, final TaskStatus status) {
      final Task task = new Task();
      task.setTitle(title);
      task.setStatus(status);
      return testRestTemplate.postForEntity(testTarget + TASKS_API, task, Task.class);
   }

   protected void addCreatedTaskToTaskList() {
      if (postResponse.getStatusCode() == HttpStatus.OK && postResponse.getBody() != null) {
         createdTaskIds.add(postResponse.getBody().getId());
      } else {
         logger.error("Unexpected status code: {}", postResponse.getStatusCode());
         fail("Unexpected status code: " + postResponse.getStatusCode());
      }
   }

   protected void getAllTasks() {
      getResponse = testRestTemplate.exchange(testTarget + TASKS_API, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Task>>() {
            });
      tasks = getResponse.getBody();
   }

   protected long getCountByTaskStatus(final TaskStatus taskStatus) {
      return tasks.stream()
                  .filter(task -> task.getStatus() == taskStatus && createdTaskIds.contains(
                        task.getId()))
                  .count();
   }

   protected void markTaskAsDone(final Long taskId) {
      final HttpEntity<Void> entity = new HttpEntity<>(null, new HttpHeaders());
      patchResponse = testRestTemplate.exchange(testTarget + TASKS_API + "/" + taskId.toString(), HttpMethod.PATCH,
            entity, Void.class);
   }

   protected void markAllTasksAsDone() {
      patchResponse = testRestTemplate.exchange(testTarget + TASKS_API, HttpMethod.PATCH, null, Void.class);
   }
}
