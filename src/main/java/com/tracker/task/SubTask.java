package com.tracker.task;

import java.util.Map;

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
    public Map<String, String> getData() {
        Map<String, String> data = super.getData();
        data.put("epic", String.valueOf(epicId));
        return data;
    }
}
