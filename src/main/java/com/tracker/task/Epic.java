package com.tracker.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

  protected TaskType type = TaskType.EPIC;

  protected ArrayList<Integer> subTasksIds = new ArrayList<>();

  public Epic(String name, String description) {
    super(name, description, Status.NEW);
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
        return "Epic{" +
                "type=" + type +
                ", subTasksIds=" + subTasksIds +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

  public void setDuration(Duration epicDuration) {
    this.duration = epicDuration;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }
}
