package com.tracker.task;

public class SubTask extends Task {
    public SubTask(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    @Override
    public String toString() {
        return "    Подзадача #" + id + "\n" +
                "    Статус: " + status + "\n" +
                "    Название: " + name + "\n" +
                "    Описание: " + description + "\n";
    }
}
