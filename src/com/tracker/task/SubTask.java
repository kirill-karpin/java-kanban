package com.tracker.task;

public class SubTask extends Task {

    protected TaskType type = TaskType.SUBTASK;
    private Integer epicId;

    public SubTask(int id, Integer epicId,  String name, String description, Status status) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    public TaskType getType() {
        return type;
    }

    @Override
    public String toString() {
        return  "   Подзадача #" + id + "\n" +
                "    Статус: " + status + "\n" +
                "    Название: " + name + "\n" +
                "    Описание: " + description + "\n";
    }
}
