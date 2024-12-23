package com.tracker.task;

import static com.tracker.task.TaskType.TASK;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Task implements Cloneable {

  protected int id;
  protected String name;
  protected String description;
  protected Status status;
  protected TaskType type = TASK;
  protected Duration duration;
  protected LocalDateTime startTime;

  public Task(
      String name, String description, Status status, Duration duration, LocalDateTime startTime) {
    this.name = name;
    this.description = description;
    this.status = status;
    this.duration = duration;
    this.startTime = startTime;
  }

  protected Task(String name, String description, Status status) {
    this.name = name;
    this.description = description;
    this.status = status;
  }

  public Task() {

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

  public TaskType getType() {
    return this.type;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "Task{"
        + "id="
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

  public void setStatusDone() {
    this.status = Status.DONE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Task task)) {
      return false;
    }
    return id == task.id
        && Objects.equals(name, task.name)
        && Objects.equals(description, task.description)
        && status == task.status
        && type == task.type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, description, type);
  }

  @Override
  public Task clone() {
    try {
      Task clone = (Task) super.clone();
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  public Map<String, String> getData() {
    HashMap<String, String> data = new HashMap<>();
    data.put("id", String.valueOf(id));
    data.put("name", name);
    data.put("description", description);
    data.put("status", status.toString());
    data.put("type", getType().toString());
    data.put("duration", getDuration() != null ? String.valueOf(getDuration().toMinutes()) : "");
    data.put("startTime", getStartTime() != null ? getStartTime().toString() : "");
    return data;
  }

  public LocalDateTime getStartTime() {
    if (startTime == null) {
      return null;
    }
    return startTime;
  }

  public LocalDateTime getEndTime() {
    return startTime.plus(getDuration());
  }

  public Duration getDuration() {
    return duration;
  }

  public boolean isOverlap(Task task) {
    return getEndTime().isAfter(task.getStartTime()) && getStartTime().isBefore(task.getEndTime());
  }
}
