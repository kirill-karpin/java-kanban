package com.tracker.task;

import static com.tracker.task.TaskType.TASK;

public class Task {
    protected int id;
    protected String name;
    protected String description;
    protected Status status;
    protected TaskType type = TASK;

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public TaskType getType() {
        return this.type;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return  " Задача #" + id + "\n" +
                "  Статус: " + status + "\n" +
                "  Название: " + name + "\n" +
                "  Описание: " + description + "\n";
    }

    public void setStatusDone() {
        this.status = Status.DONE;
    }
}
