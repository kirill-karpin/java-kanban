package com.tracker;

import com.tracker.interfaces.HistoryManager;
import com.tracker.interfaces.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static TaskManager getDefaultPersist() {
        return new FileBackedTaskManager(getDefaultHistory());
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
