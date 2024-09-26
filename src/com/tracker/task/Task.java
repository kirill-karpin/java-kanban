package com.tracker.task;

import java.util.HashMap;

public class Task {
    int id;
    String name;
    String description;
    Status status;
    HashMap<Integer, Task> tasks = new HashMap<>();

    public Task(int id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
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

    @Override
    public String toString() {
        return " Задача #" + id + "\n" +
                " Статус: " + status + "\n" +
                " Название: " + name + "\n" +
                " Описание: " + description + "\n";
    }

    public void setStatusDone() {
        this.status = Status.DONE;
    }

    public void removeTask(Integer id) {
        tasks.remove(id);
    }
}
