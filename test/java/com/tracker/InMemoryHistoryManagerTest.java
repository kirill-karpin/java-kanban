package com.tracker;

import com.tracker.interfaces.HistoryManager;
import com.tracker.task.Status;
import com.tracker.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

  private static HistoryManager historyManager;

  @BeforeEach
  void init() {
    historyManager = Managers.getDefaultHistory();
  }

  @Test
  public void testAddAndGet() {

    Task task =
        new Task(
            "Test Task",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    historyManager.addTask(task);

    final List<Task> history = historyManager.getHistory();
    assertNotNull(history, "История не пустая.");
    assertEquals(1, history.size(), "История не пустая.");
  }

  @Test
  public void testGetEmpty() {
    InMemoryHistoryManager manager = new InMemoryHistoryManager();
    assertTrue(manager.getHistory().isEmpty());
  }

  @Test
  void addTask() {
    Task task =
        new Task(
            "Test Task",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    historyManager.addTask(task);
    assertEquals(1, historyManager.getHistory().size());
  }

  @Test
  void getHistory() {}

  @Test
  void testHead() {
    Task task1 =
        new Task(
            "Test Task1",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    task1.setId(1);
    Node<Task> node1 = new Node<>(task1);
    historyManager.linkLast(node1);
    assertEquals(historyManager.getHead(), node1);
    assertNull(historyManager.getTail());
  }

  @Test
  void testLinkHeadTaskAndRemove() {
    Task task1 =
        new Task(
            "Test Task1",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    task1.setId(1);
    Node<Task> node1 = new Node<>(task1);
    historyManager.linkLast(node1);
    historyManager.removeTask(1);

    assertNull(historyManager.getHead());
    assertNull(historyManager.getTail());
  }

  @Test
  void testLinkHeadAndTaskAndRemoveHead() {
    Task task1 =
        new Task(
            "Test Task1",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    task1.setId(1);
    Node<Task> node1 = new Node<>(task1);
    historyManager.linkLast(node1);

    Task task2 =
        new Task(
            "Test Task2",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    task2.setId(2);
    Node<Task> node2 = new Node<>(task2);
    historyManager.linkLast(node2);
    historyManager.removeTask(1);

    assertEquals(node2, historyManager.getHead());
    assertNull(historyManager.getTail());
  }

  @Test
  void testLinkHeadAndTaskAndRemoveTail() {
    Task task1 =
        new Task(
            "Test Task1",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    task1.setId(1);
    Node<Task> node1 = new Node<>(task1);
    historyManager.linkLast(node1);

    Task task2 =
        new Task(
            "Test Task2",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    task2.setId(2);
    Node<Task> node2 = new Node<>(task2);
    historyManager.linkLast(node2);
    historyManager.removeTask(2);

    assertEquals(node1, historyManager.getHead());
    assertNull(historyManager.getTail());
  }

  @Test
  void testNodeHeadAndTail() {
    Task task1 =
        new Task(
            "Test Task1",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    task1.setId(1);
    Node<Task> node1 = new Node<>(task1);
    historyManager.linkLast(node1);
    assertEquals(historyManager.getHead(), node1);

    Task task2 =
        new Task(
            "Test Task2",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    task1.setId(2);
    Node<Task> node2 = new Node<>(task2);
    historyManager.linkLast(node2);

    assertEquals(node1.getNext(), node2);
    assertEquals(node2.getPrev(), node1);
    assertEquals(historyManager.getTail(), node2);
  }

  @Test
  void testLinkSize() {
    Task task =
        new Task(
            "Test Task1",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    historyManager.linkLast(new Node<>(task));
    Task task2 =
        new Task(
            "Test Task2",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    historyManager.linkLast(new Node<>(task2));
    Task task3 =
        new Task(
            "Test Task3",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    historyManager.linkLast(new Node<>(task3));
    assertEquals(3, historyManager.getHistory().size());
  }

  @Test
  void testRemoveTasks() {
    Task task1 =
        new Task(
            "Test Task1",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    task1.setId(1);
    historyManager.linkLast(new Node<>(task1));

    Task task2 =
        new Task(
            "Test Task2",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    task2.setId(2);
    historyManager.linkLast(new Node<>(task2));

    Task task3 =
        new Task(
            "Test Task3",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    task3.setId(3);
    historyManager.linkLast(new Node<>(task3));

    historyManager.removeTask(task2.getId());
    assertEquals(2, historyManager.getHistory().size());

    historyManager.removeTask(task1.getId());
    assertEquals(1, historyManager.getHistory().size());

    historyManager.removeTask(task3.getId());

    assertEquals(0, historyManager.getHistory().size());
  }

  @Test
  void testTaskQueue() {
    Task task1 =
        new Task(
            "Test Task1",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    task1.setId(1);
    Task task2 =
        new Task(
            "Test Task2",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    task2.setId(2);
    Task task3 =
        new Task(
            "Test Task3",
            "Test Description",
            Status.DONE,
            Duration.ofMinutes(10),
            LocalDateTime.now());
    task3.setId(3);
    historyManager.addTask(task1);
    historyManager.addTask(task2);
    historyManager.addTask(task3);

    historyManager.addTask(task1);
    assertEquals(historyManager.getHistory().getLast(), task1);

    historyManager.addTask(task2);
    assertEquals(historyManager.getHistory().getLast(), task2);
  }
}
