package com.tracker;

import com.tracker.task.Epic;
import com.tracker.task.Status;
import com.tracker.task.SubTask;
import com.tracker.task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int taskIdCounter = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subTasks = new HashMap<>();

    private int getNewId() {
        return taskIdCounter++;
    }

    public Task createNewTask(String name, String description) {
        int id = getNewId();
        addTask(new Task(id, name, description, Status.NEW));
        return tasks.get(id);
    }

    public Epic createNewEpic(String name, String description) {
        int id = getNewId();
        addTask(new Epic(id, name, description, Status.NEW));
        return epics.get(id);
    }

    public SubTask createNewSubtask(Integer epicId, String name, String description) {
        int id = getNewId();
        addTask(new SubTask(id, epicId, name, description, Status.NEW));
        return subTasks.get(id);
    }

    public void addTask(Task task) {
        switch (task.getType()) {
            case EPIC:
                Epic epic = (Epic) task;
                epic.setStatus(getStatusForEpic(epic));
                epics.put(epic.getId(), epic);
                break;
            case SUBTASK:
                SubTask subTask = (SubTask) task;
                subTasks.put(task.getId(), subTask);

                Epic epicUpdate = epics.get(subTask.getEpicId());
                epicUpdate.addSubTask(subTask.getId());
                epicUpdate.setStatus( getEpicStatus(epicUpdate));

                break;
            case TASK:
                tasks.put(task.getId(), task);
                break;
            default:
        }
    }

    public void updateTask(Task updateTask) {
        int id = updateTask.getId();
        switch (updateTask.getType()) {
            case EPIC:
                Epic epic = (Epic) updateTask;
                epic.setStatus( getEpicStatus(epic));
                epics.put(id, epic);

                break;
            case SUBTASK:
                SubTask updateSubTask = (SubTask) updateTask;
                subTasks.put(id, updateSubTask);

                Epic epicUpdate = epics.get(updateSubTask.getEpicId());
                epicUpdate.setStatus( getEpicStatus(epicUpdate));

                break;
            case TASK:
                tasks.put(id, updateTask);
                break;
            default:
        }

    }

    public void deleteTask(Task task) {
        int id = task.getId();

        switch (task.getType()) {
            case EPIC:
                if(epics.containsKey(id)) {
                    Epic epic = (Epic) task;
                    epic.setSubTasks(epic.getSubTasks());
                    epics.remove(id);
                }
                break;
            case SUBTASK:
                SubTask subTask = (SubTask) task;
                subTasks.remove(id);

                if(epics.containsKey(subTask.getEpicId())) {
                    Epic epic = epics.get(subTask.getEpicId());
                    epic.removeTask(id);
                    epic.setStatus( getEpicStatus(epic));
                }

                break;
            case TASK:
                tasks.remove(id);
                break;
            default:
        }

    }

    private Status getEpicStatus(Epic  epic) {
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

    public Status getStatusForEpic(Epic epic) {

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
    public String toString() {
        String epicsList = "Список эпиков: \n";
        String tasksList = "Список задач: \n";
        String subTasksList = "Список подзадач: \n";

        for (Epic epic : epics.values()) {
            epicsList += epic.toString() + "\n";

            for (Integer taskId : epic.getSubTasks()) {
                epicsList += subTasks.get(taskId).toString() + "\n";
            }
        }

        for (Task task : tasks.values()) {
            tasksList += task.toString() + "\n";
        }

        return epicsList + tasksList;
    }
}
