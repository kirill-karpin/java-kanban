package com.tracker.interfaces;

import com.tracker.exception.TaskAddException;
import com.tracker.task.Epic;
import com.tracker.task.SubTask;
import com.tracker.task.Task;
import java.util.List;
import java.util.Set;

public interface TaskManager {

  Task getTask(int id);

  Epic getEpic(int id);

  SubTask getSubtask(int id);

  int add(Task task) throws TaskAddException;

  void addEpicSubTask(int epicId, SubTask subTask) throws TaskAddException;

  int update(Task updateTask);

  void delete(Task task);

  List<Task> getTasks();

  List<Epic> getEpics();

  List<SubTask> getEpicSubtasks(int id);

  List<SubTask> getSubtasks();

  List<Task> getHistory();

  Set<Task> getPrioritizedTasks();
}
