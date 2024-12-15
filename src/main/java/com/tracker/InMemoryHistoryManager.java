package com.tracker;

import com.tracker.interfaces.HistoryManager;
import com.tracker.task.Task;
import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

  protected Node<Task> head;
  protected Node<Task> tail;
  private final HashMap<Integer, Node<Task>> indexMap = new HashMap<>();

  @Override
  public void addTask(Task task) {
    Task cloneTask = task.clone();
    removeTask(task.getId());

    Node<Task> node = new Node<>(cloneTask);
    linkLast(node);
  }

  @Override
  public ArrayList<Task> getHistory() {
    return getTasks();
  }

  public void linkLast(Node<Task> node) {
    indexMap.put(node.data.getId(), node);

    if (head == null) {
      head = node;
      return;
    }
    if (tail == null) {
      node.prev = head;
      head.next = node;
      tail = node;
      return;
    }
    tail.next = node;
    node.prev = tail;
    tail = node;
  }

  public ArrayList<Task> getTasks() {
    Node<Task> node = head;
    ArrayList<Task> tasks = new ArrayList<>();
    while (node != null) {
      tasks.add(node.data);
      node = node.next;
    }

    return tasks;
  }

  public void removeTask(int id) {
    Node<Task> node = indexMap.get(id);
    if (node == null) {
      return;
    }

    Node<Task> prev = node.prev;
    Node<Task> next = node.next;

    if (prev != null) {
      prev.next = next;
    }
    if (next != null) {
      next.prev = prev;
    }

    if (node == head) {
      head = next;
      if (next == tail) {
        tail = null;
      }
    }
    if (node == tail) {
      tail = next;
    }

    indexMap.remove(id);
  }

  @Override
  public Node<Task> getHead() {
    return head;
  }

  @Override
  public Node<Task> getTail() {
    return tail;
  }
}
