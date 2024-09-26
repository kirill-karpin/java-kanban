package com.tracker;

import com.tracker.task.Epic;
import com.tracker.task.Status;
import com.tracker.task.SubTask;
import com.tracker.task.Task;

import java.util.HashMap;

public class TaskManager {
    private int taskIdCounter = 1;
    private final HashMap<Integer, Task> store = new HashMap<>();


    public Task createNewTask(String name, String description) {
        int id = getNewId();
        store.put(id, new Task(id, name, description, Status.NEW));
        return store.get(id);
    }

    public Epic createNewEpic(String name, String description) {
        int id = getNewId();
        store.put(id, new Epic(id, name, description, Status.NEW));
        return (Epic) store.get(id);
    }

    public SubTask createNewSubtask(String name, String description) {
        int id = getNewId();
        store.put(id, new SubTask(id, name, description, Status.NEW));
        return (SubTask) store.get(id);
    }

    public void deleteTask(Task task) {
        store.remove(task.getId());
        for (Task task1 : store.values()) {
            task1.removeTask(task.getId());
        }
    }

    public void addTaskToEpic(SubTask task, Epic epic) {
        if (epic != null) {
            epic.addTask(task);
        }
    }


    private int getNewId() {
        return taskIdCounter++;
    }

    public void setTaskStatusDone(Integer taskId) {
        Task task = store.get(taskId);
        if (task != null) {
            task.setStatusDone();
        }
    }

    @Override
    public String toString() {
        String epicsList = "Список эпиков: \n";
        String tasksList = "Список задач: \n";
        String subTasksList = "Список подзадач: \n";

        for (Task task : store.values()) {
            if (task instanceof Epic) {
                epicsList += task.toString() + "\n";
            } else if (task instanceof SubTask) {
                subTasksList += task.toString() + "\n";
            } else {
                tasksList += task.toString() + "\n";
            }
        }

        return epicsList + tasksList;
    }
}
