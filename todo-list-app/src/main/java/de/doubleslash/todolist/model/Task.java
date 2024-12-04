package de.doubleslash.todolist.model;

public class Task {
    private Long id;
    private String title;
    private TaskStatus status;

    public Task() {}

    public Task(final Long id, final String title, final TaskStatus status) {
        this.id = id;
        this.title = title;
        this.status = status;
    }

    public Task(String title, TaskStatus status) {
        this.title = title;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(final TaskStatus status) {
        this.status = status;
    }
}