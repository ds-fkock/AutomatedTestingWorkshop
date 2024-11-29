package de.doubleslash.todolist.service;

import de.doubleslash.todolist.model.Task;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private Long idCounter = 1L;

    public List<Task> getAllTasks() {
        return tasks;
    }

    public Task createTask(final String title) {
        final Task task = new Task(idCounter++, title, "open");
        tasks.add(task);
        return task;
    }

    public Optional<Task> markTaskAsCompleted(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .peek(task -> task.setStatus("done"))
                .findFirst();
    }
}
