package com.tracker.interfaces;

import com.tracker.Node;
import com.tracker.task.Task;
import java.util.List;

public interface HistoryManager {

  void addTask(Task task);

  List<Task> getHistory();

  void linkLast(Node<Task> node);

  void removeTask(int index);

  Node<Task> getHead();

  Node<Task> getTail();
}
