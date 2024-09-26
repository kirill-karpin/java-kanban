package com.tracker.task;

public class Epic extends Task {

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public void addTask(Task task) {
        tasks.put(task.getId(), task);
    }


    @Override
    public Status getStatus() {
        if (tasks.isEmpty()) {
            return Status.NEW;
        }

        for (Task task : tasks.values()) {
            if (task.getStatus() == Status.IN_PROGRESS || task.getStatus() == Status.NEW) {
                return Status.IN_PROGRESS;
            }
        }

        return Status.DONE;
    }

    @Override
    public String toString() {
        String tasksList = "";
        for (Task task : tasks.values()) {
            tasksList += task.toString() + "\n";
        }
        return " Эпик  #" + id + "\n" +
                "  Статус: " + getStatus() + "\n" +
                "  Название: " + name + "\n" +
                "  Описание: " + description + "\n" +
                "  Подзадачи : " + "\n"
                + tasksList;

    }
}
