package com.tracker.task;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskTest {

  private final int durationMinutes = 15;

  @Test
  void isOverlapTwoTasks() {
    Duration duration = Duration.ZERO.plusMinutes(durationMinutes);
    Task task = new Task("Первая задача", "Описание", Status.NEW, duration, LocalDateTime.now());

    Duration duration2 = Duration.ZERO.plusMinutes(durationMinutes);
    Task task2 = new Task("Первая задача", "Описание", Status.NEW, duration2, LocalDateTime.now());

    assertTrue(task.isOverlap(task2));
    assertTrue(task2.isOverlap(task));
  }

  @Test
  void isNotOverlapTwoTasks() {
    Duration duration = Duration.ZERO.plusMinutes(durationMinutes);
    LocalDateTime now = LocalDateTime.now();
    Task task = new Task("Первая задача", "Описание", Status.NEW, duration, now);

    Task task2 =
        new Task(
            "Первая задача", "Описание", Status.NEW, duration, now.plusMinutes(durationMinutes));

    assertFalse(task.isOverlap(task2));
    assertFalse(task2.isOverlap(task));
  }
}
