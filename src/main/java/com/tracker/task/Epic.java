package com.tracker.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class Epic extends Task {

  protected Collection<Integer> subTasksIds = new ArrayList<>();

  public Epic(String name, String description) {
    super(name, description, Status.NEW);
    type = TaskType.EPIC;
  }

  public void addSubTask(Integer id) {
    subTasksIds.add(id);
  }

  public void removeTask(Integer id) {
    subTasksIds.remove(id);
  }

  public Collection<Integer> getSubTasks() {
    return subTasksIds;
  }

  public void setSubTasks(Collection<Integer> subTasks) {
    this.subTasksIds = subTasks;
  }

  @Override
  public String toString() {
    return "Epic{"
        + "type="
        + type
        + ", subTasksIds="
        + subTasksIds
        + ", id="
        + id
        + ", name='"
        + name
        + '\''
        + ", description='"
        + description
        + '\''
        + ", status="
        + status
        + ", type="
        + type
        + ", duration="
        + duration
        + ", startTime="
        + startTime
        + '}';
  }

  public void setDuration(Duration epicDuration) {
    this.duration = epicDuration;
  }

  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }
}
