package com.tracker.interfaces;

import com.tracker.task.Task;

import java.util.ArrayList;

public interface HistoryManager {
    void addTask(Task task);
    ArrayList<Task> getHistory();
}
