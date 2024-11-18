package com.tracker.task;

import java.util.HashMap;

public class SubTask extends Task {

    protected TaskType type = TaskType.SUBTASK;
    private Integer epicId;

    public SubTask(String name, String description, Status status) {
        super(name, description, status);
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
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public HashMap<String, String> getData() {
        HashMap<String, String> data = super.getData();
        data.put("epic", String.valueOf(epicId));
        return data;
    }
}
