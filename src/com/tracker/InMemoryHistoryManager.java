package com.tracker;

import com.tracker.interfaces.HistoryManager;
import com.tracker.task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        history.add(task.clone());
    }

    @Override
    public ArrayList<Task> getHistory() {
        if (!history.isEmpty()) {
            if (history.size() > 10) {
                int start = history.size() - 11;
                int end = history.size() - 1;
                return new ArrayList<>(history.subList(start, end)) ;
            }

        }
        return history;
    }

}
