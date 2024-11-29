package de.doubleslash.todolist.controller;

import de.doubleslash.todolist.model.Task;
import de.doubleslash.todolist.service.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        logger.info("GET request received to retrieve all tasks");
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody final Task task) {
        logger.info("POST request received to create a new task: {}", task.getTitle());
        if (task.getTitle() == null || task.getTitle().isEmpty()) {
            logger.warn("Bad request received - task title null or empty: {}", task);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(taskService.createTask(task.getTitle(), task.getStatus() == null? "open" : task.getStatus()  ));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long id) {
        logger.info("PATCH request received to mark task with ID {} as completed", id);
        return taskService.markTaskAsCompleted(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}