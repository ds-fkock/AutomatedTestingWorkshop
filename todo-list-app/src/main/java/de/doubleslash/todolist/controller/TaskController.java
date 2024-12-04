package de.doubleslash.todolist.controller;

import de.doubleslash.todolist.model.Task;
import de.doubleslash.todolist.model.TaskStatus;
import de.doubleslash.todolist.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TaskController {

   private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

   private final TaskService taskService;

   public TaskController(final TaskService taskService) {
      this.taskService = taskService;
   }

   @GetMapping
   public ResponseEntity<List<Task>> getAllTasks() {
      logger.info("GET request received to retrieve all tasks");
      return ResponseEntity.ok(taskService.getAllTasks());
   }

   @GetMapping("/{title}")
   public ResponseEntity<List<Task>> getTasksByTitle(@PathVariable final String title) {
      logger.info("GET request received to retrieve task with title: {}", title);
      final List<Task> tasks = taskService.getTasksByTitle(title);
      return ResponseEntity.ok(tasks);
   }

   @PostMapping
   public ResponseEntity<Task> createTask(@RequestBody final Task task) {
      logger.info("POST request received to create a new task: {}", task.getTitle());
      if (task.getTitle() == null || task.getTitle().isEmpty()) {
         logger.warn("Bad request received - task title null or empty: {}", task);
         return ResponseEntity.badRequest().build();
      }
      return ResponseEntity.ok(
            taskService.createTask(task.getTitle(), task.getStatus() == null ? TaskStatus.OPEN : task.getStatus()));
   }

   @PatchMapping
   public ResponseEntity<Void> markAllTasksAsCompleted() {
      logger.info("PATCH request received to mark all tasks as completed");
      taskService.markTasksAsCompleted();
      return ResponseEntity.ok().build();
   }

   @PatchMapping("/{id}")
   public ResponseEntity<Void> markTaskAsCompleted(@PathVariable final Long id) {
      logger.info("PATCH request received to mark task with ID {} as completed", id);
      final Optional<Task> completedTask = taskService.markTaskAsCompleted(id);

      if (completedTask.isPresent()) {
         logger.info("Task with ID {} marked as completed", id);
         return ResponseEntity.ok().build();
      }
      else {
         logger.warn("Task with ID {} not found", id);
         return ResponseEntity.notFound().build();
      }
   }
}