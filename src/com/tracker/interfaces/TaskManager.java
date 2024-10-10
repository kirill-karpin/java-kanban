package com.tracker.interfaces;

import com.tracker.task.Epic;
import com.tracker.task.SubTask;
import com.tracker.task.Task;

import java.util.List;

public interface TaskManager {
    Task getTask(int id);

    Epic getEpic(int id);

    Task getSubtask(int id);

    int add(Task task);

    void addEpicSubTask(int epicId, SubTask subTask);

    int update(Task updateTask);

    void delete(Task task);

    List<Task> getTasks();

    List<Epic> getEpics();

    List<SubTask> getEpicSubtasks(int id);

    List<SubTask> getSubtasks();

    List<Task> getHistory();
}
