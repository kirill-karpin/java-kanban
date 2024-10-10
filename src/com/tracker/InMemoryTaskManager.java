package com.tracker;

import com.tracker.interfaces.HistoryManager;
import com.tracker.interfaces.TaskManager;
import com.tracker.task.Epic;
import com.tracker.task.Status;
import com.tracker.task.SubTask;
import com.tracker.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int taskIdCounter = 1;
    private final HistoryManager historyManager;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }


    private int getNewId() {
        return taskIdCounter++;
    }

    public Task createNewTask(String name, String description) {
        int id = add(new Task(name, description, Status.NEW));
        return tasks.get(id);
    }

    public Epic createNewEpic(String name, String description) {
        int id = add(new Epic(name, description));
        return epics.get(id);
    }

    public SubTask createNewSubtask(String name, String description) {
        int id = add(new SubTask(name, description, Status.NEW));
        return subTasks.get(id);
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.addTask(task);
            return task;
        }
        return null;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        historyManager.addTask(epic);
        return epic;
    }

    @Override
    public SubTask getSubtask(int id) {
        SubTask task = subTasks.get(id);
        historyManager.addTask(task);
        return task;
    }

    @Override
    public int add(Task task) {
        int id = getNewId();
        task.setId(id);

        switch (task.getType()) {
            case EPIC:
                Epic epic = (Epic) task;
                epic.setStatus(getEpicStatus(epic));
                epics.put(epic.getId(), epic);
                break;
            case SUBTASK:
                SubTask subTask = (SubTask) task;
                subTasks.put(task.getId(), subTask);
                break;
            case TASK:
                tasks.put(task.getId(), task);
                break;
            default:
                throw new IllegalArgumentException("Unknown task type");
        }

        return id;
    }

    @Override
    public int update(Task updateTask) {
        int id = updateTask.getId();
        switch (updateTask.getType()) {
            case EPIC:
                Epic epic = (Epic) updateTask;
                epic.setStatus(getEpicStatus(epic));
                if (epics.containsKey(id)) {
                    epics.put(id, epic);
                }
                break;
            case SUBTASK:
                SubTask updateSubTask = (SubTask) updateTask;
                subTasks.put(id, updateSubTask);
                Epic epicUpdate = epics.get(updateSubTask.getEpicId());
                epicUpdate.setStatus(getEpicStatus(epicUpdate));
                break;
            case TASK:
                tasks.put(id, updateTask);
                break;
            default:
                throw new IllegalArgumentException("Unknown task type");
        }

        return id;
    }

    @Override
    public void delete(Task task) {
        int id = task.getId();

        switch (task.getType()) {
            case EPIC:
                if (epics.containsKey(id)) {
                    Epic epic = (Epic) task;
                    epic.setSubTasks(epic.getSubTasks());
                    epics.remove(id);
                }
                break;
            case SUBTASK:
                SubTask subTask = (SubTask) task;
                subTasks.remove(id);

                if (epics.containsKey(subTask.getEpicId())) {
                    Epic epic = epics.get(subTask.getEpicId());
                    epic.removeTask(id);
                    epic.setStatus(getEpicStatus(epic));
                }
                break;
            case TASK:
                tasks.remove(id);
                break;
            default:
                throw new IllegalArgumentException("Unknown task type");
        }

    }

    private Status getEpicStatus(Epic epic) {
        ArrayList<Integer> tasksId = epic.getSubTasks();
        if (tasksId.isEmpty()) {
            return Status.NEW;
        }

        for (Integer taskId : tasksId) {
            Task task = subTasks.get(taskId);
            if (task.getStatus() == Status.IN_PROGRESS || task.getStatus() == Status.NEW) {
                return Status.IN_PROGRESS;
            }
        }

        return Status.DONE;
    }

    @Override
    public List<Task> getTasks() {
        return tasks.values().stream().toList();
    }

    @Override
    public List<Epic> getEpics() {
        return epics.values().stream().toList();
    }

    @Override
    public List<SubTask> getEpicSubtasks(int id) {
        return subTasks.values().stream().filter(task -> task.getEpicId() == id).toList();
    }

    @Override
    public List<SubTask> getSubtasks() {
        return subTasks.values().stream().toList();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void addEpicSubTask(int epicId, SubTask subTask) {
        Epic epic = getEpic(epicId);
        if (epic != null) {
            subTask.setEpicId(epicId);
            int subTaskId;
            if (subTask.getId() > 0) {
                subTaskId = subTask.getId();
            } else {
                subTaskId = add(subTask);
            }
            epic.addSubTask(subTaskId);
            epic.setStatus(getEpicStatus(epic));
        }
    }
}
