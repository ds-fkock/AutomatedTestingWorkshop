package de.doubleslash.todolist.apitest.cucumber.service;

import de.doubleslash.todolist.apitest.cucumber.CucumberSpringConfiguration;
import de.doubleslash.todolist.model.Task;
import de.doubleslash.todolist.model.TaskStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TaskApiService {

   private static final Logger logger = LoggerFactory.getLogger(CucumberSpringConfiguration.class);

   @Value("${test.target}")
   private String testTarget;

   @Value("${api.tasks.endpoint:/tasks}")
   private String tasksEndpoint;

   @Autowired
   private TestRestTemplate testRestTemplate;

   private ResponseEntity<List<Task>> getResponse;
   private ResponseEntity<Task> postResponse;
   private ResponseEntity<Void> patchResponse;
   private List<Long> createdTaskIds = new ArrayList<>();

   /**
    * Create a task with the given title and status.
    */
   public ResponseEntity<Task> createTask(final String title, final TaskStatus status) {
      final Task task = new Task();
      task.setTitle(title);
      task.setStatus(status);

      try {
         postResponse = testRestTemplate.postForEntity(getFullApiUrl(), task, Task.class);
         if (postResponse.getStatusCode() == HttpStatus.OK && postResponse.getBody() != null) {
            createdTaskIds.add(postResponse.getBody().getId());
         } else {
            logger.info("Failed to create task: {}", postResponse);
         }
      } catch (final Exception e) {
         logger.error("Exception during task creation: {}", e.getMessage(), e);
      }

      return postResponse;
   }

   /**
    * Get all tasks from the API.
    */
   public List<Task> getAllTasks() {
      try {
         getResponse = testRestTemplate.exchange(getFullApiUrl(), HttpMethod.GET, null,
               new ParameterizedTypeReference<List<Task>>() {
               });
         return Optional.ofNullable(getResponse.getBody()).orElse(Collections.emptyList());
      } catch (final Exception e) {
         logger.error("Exception during fetching tasks: {}", e.getMessage(), e);
         return Collections.emptyList();
      }
   }

   /**
    * Get the count of tasks by status.
    */
   public long getCountByTaskStatus(final TaskStatus taskStatus) {
      final List<Task> tasks = getAllTasks();
      return tasks.stream()
                  .filter(task -> task.getStatus() == taskStatus && createdTaskIds.contains(task.getId()))
                  .count();
   }

   /**
    * Mark a task as done by task ID.
    */
   public void markTaskAsDone(final Long taskId) {
      try {
         final HttpEntity<Void> entity = new HttpEntity<>(null, new HttpHeaders());
         patchResponse = testRestTemplate.exchange(getFullApiUrl() + "/" + taskId, HttpMethod.PATCH, entity,
               Void.class);
         if (patchResponse.getStatusCode() != HttpStatus.OK) {
            logger.error("Failed to mark task {} as done: {}", taskId, patchResponse);
         }
      } catch (final Exception e) {
         logger.error("Exception during marking task as done: {}", e.getMessage(), e);
      }
   }

   /**
    * Mark all tasks as done.
    */
   public void markAllTasksAsDone() {
      try {
         patchResponse = testRestTemplate.exchange(getFullApiUrl(), HttpMethod.PATCH, null, Void.class);
         if (patchResponse.getStatusCode() != HttpStatus.OK) {
            logger.error("Failed to mark all tasks as done: {}", patchResponse);
         }
      } catch (final Exception e) {
         logger.error("Exception during marking all tasks as done: {}", e.getMessage(), e);
      }
   }

   /**
    * Get the full API URL.
    */
   private String getFullApiUrl() {
      return testTarget + tasksEndpoint;
   }

   public List<Long> getCreatedTaskIds() {
      return createdTaskIds;
   }

   public ResponseEntity<Void> getPatchResponse() {
      return patchResponse;
   }

   public ResponseEntity<Task> getPostResponse() {
      return postResponse;
   }

   public ResponseEntity<List<Task>> getGetResponse() {
      return getResponse;
   }

   public void setGetResponse(
         final ResponseEntity<List<Task>> getResponse) {
      this.getResponse = getResponse;
   }

   public void setPostResponse(final ResponseEntity<Task> postResponse) {
      this.postResponse = postResponse;
   }

   public void setPatchResponse(final ResponseEntity<Void> patchResponse) {
      this.patchResponse = patchResponse;
   }

   public void setCreatedTaskIds(final List<Long> createdTaskIds) {
      this.createdTaskIds = createdTaskIds;
   }
}
