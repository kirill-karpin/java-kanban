package com.tracker.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class SubTask extends Task {

    protected TaskType type = TaskType.SUBTASK;
    private Integer epicId;

    public SubTask(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        super(
                name,
                description,
                status,
                duration,
                startTime
        );
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
                "type=" + type +
                ", epicId=" + epicId +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    @Override
    public Map<String, String> getData() {
        Map<String, String> data = super.getData();
        data.put(
                "epic",
                String.valueOf(epicId)
        );
        return data;
    }
}
