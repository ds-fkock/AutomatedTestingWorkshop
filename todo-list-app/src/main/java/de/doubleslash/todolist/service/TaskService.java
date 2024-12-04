package de.doubleslash.todolist.service;

import de.doubleslash.todolist.model.Task;
import de.doubleslash.todolist.model.TaskStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final List<Task> tasks = new ArrayList<>();
    private Long idCounter = 1L;

    public List<Task> getAllTasks() {
        return tasks;
    }

    public Task createTask(final String title, final TaskStatus status) {
        final Task task = new Task(idCounter++, title, status);
        tasks.add(task);
        return task;
    }

    public Optional<Task> markTaskAsCompleted(final Long id) {
        final Optional<Task> searchedTask = tasks.stream()
                                                 .filter(task -> task.getId().equals(id))
                                                 .findFirst();
       searchedTask.ifPresent(task -> task.setStatus(TaskStatus.DONE));
        return searchedTask;
    }

    public void markTasksAsCompleted() {
        tasks.forEach(task -> task.setStatus(TaskStatus.DONE));
    }

    public List<Task> getTasksByTitle(final String regex) {
        final Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        return tasks.stream()
                    .filter(task -> pattern.matcher(task.getTitle()).find())
                    .collect(Collectors.toList());
    }
}
