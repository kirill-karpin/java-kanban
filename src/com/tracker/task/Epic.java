package com.tracker.task;

import java.util.ArrayList;

public class Epic extends Task {

    protected TaskType type = TaskType.EPIC;

    protected ArrayList<Integer> subTasksIds = new ArrayList<>();

    public Epic(int id, String name, String description, Status status) {
        super(id, name, description, Status.NEW);
    }

    public void addSubTask(Integer id) {
        subTasksIds.add(id);
    }

    public void removeTask(Integer id) {
        subTasksIds.remove(id);
    }

    public ArrayList<Integer> getSubTasks() {
        return subTasksIds;
    }

    public void setSubTasks(ArrayList<Integer> subTasks) {
        this.subTasksIds = subTasks;
    }

    public TaskType getType() {
        return type;
    }

    @Override
    public String toString() {
        return  " Эпик  #" + id + "\n" +
                "  Статус: " + getStatus() + "\n" +
                "  Название: " + name + "\n" +
                "  Описание: " + description + "\n" +
                "  Подзадачи : " + "\n";
    }
}
