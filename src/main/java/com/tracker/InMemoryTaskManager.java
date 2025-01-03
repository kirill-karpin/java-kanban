package com.tracker;

import com.tracker.exception.TaskAddException;
import com.tracker.interfaces.HistoryManager;
import com.tracker.interfaces.TaskManager;
import com.tracker.result.Result;
import com.tracker.task.Epic;
import com.tracker.task.Status;
import com.tracker.task.SubTask;
import com.tracker.task.Task;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class InMemoryTaskManager implements TaskManager {

  private int taskIdCounter = 1;
  private final HistoryManager historyManager;
  private final Map<Integer, Task> tasks = new HashMap<>();
  private final Map<Integer, Epic> epics = new HashMap<>();
  private final Map<Integer, SubTask> subTasks = new HashMap<>();
  private final Set<Task> allTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

  public InMemoryTaskManager(HistoryManager historyManager) {
    this.historyManager = historyManager;
  }

  private int getNewId() {
    return taskIdCounter++;
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
    if (epic != null) {
      historyManager.addTask(epic);
      return epic;
    }
    return null;
  }

  @Override
  public SubTask getSubtask(int id) {
    SubTask task = subTasks.get(id);
    if (task != null) {
      historyManager.addTask(task);
      return task;
    }
    return null;
  }

  @Override
  public int add(Task task) throws TaskAddException {
    switch (task.getType()) {
      case EPIC:
        task.setId(getNewId());
        Epic epic = (Epic) task;
        epic.setStatus(getEpicStatus(epic));
        epic.setDuration(getEpicDuration(epic));
        epics.put(epic.getId(), epic);
        break;
      case SUBTASK:
        Result validateSubtaskResult = validateOverlapTask(task);
        if (!validateSubtaskResult.isSuccess()) {
          throw new TaskAddException(validateSubtaskResult.getErrorMessage());
        }

        SubTask subTask = (SubTask) task;
        subTask.setId(getNewId());
        subTasks.put(task.getId(), subTask);
        if (task.getStartTime() != null) {
          allTasks.add(task);
        }
        break;
      case TASK:
        Result validateResult = validateOverlapTask(task);
        if (!validateResult.isSuccess()) {
          throw new TaskAddException(validateResult.getErrorMessage());
        }
        task.setId(getNewId());
        tasks.put(task.getId(), task);
        if (task.getStartTime() != null) {
          allTasks.add(task);
        }
        break;
      default:
        throw new IllegalArgumentException("Unknown task type");
    }

    return task.getId();
  }

  private Result validateOverlapTask(Task task) {
    Set<Task> prioritizedTasks = getPrioritizedTasks();
    Result result = new Result();
    if (task.getStartTime() == null) {
      return result;
    }

    Optional<Task> overlappingTask =
        prioritizedTasks.stream().filter(t -> t.isOverlap(task)).findFirst();
    if (overlappingTask.isPresent()) {
      overlappingTask.ifPresent(
          t -> result.addError("Task overlap with task: id: " + t.getId() + " Name: " + t.getName())
      );
    }
    return result;
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
        if (epicUpdate != null) {
          epicUpdate.setStatus(getEpicStatus(epicUpdate));
        }
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
    Collection<Integer> tasksId = epic.getSubTasks();
    if (tasksId.isEmpty()) {
      return Status.NEW;
    }

    if (tasksId.stream()
        .map(subTasks::get)
        .anyMatch(
            task -> task.getStatus() == Status.IN_PROGRESS || task.getStatus() == Status.NEW)) {
      return Status.IN_PROGRESS;
    }

    return Status.DONE;
  }

  private Optional<LocalDateTime> getEpicStartTime(Epic epic) {
    Collection<Integer> tasksId = epic.getSubTasks();
    if (tasksId.isEmpty()) {
      return Optional.empty();
    }

    LocalDateTime result =
        subTasks.values().stream()
            .filter(
                task ->
                    task.getEpicId() != null
                        && task.getEpicId() == epic.getId()
                        && task.getDuration() != null)
            .map(Task::getStartTime)
            .min(LocalDateTime::compareTo)
            .orElse(LocalDateTime.now());

    return Optional.of(result);
  }

  private Duration getEpicDuration(Epic epic) {
    Collection<Integer> tasksId = epic.getSubTasks();
    if (tasksId.isEmpty()) {
      return Duration.ZERO;
    }

    Optional<Duration> result =
        subTasks.values().stream()
            .filter(
                task ->
                    task.getEpicId() != null
                        && task.getEpicId() == epic.getId()
                        && task.getDuration() != null)
            .map(SubTask::getDuration)
            .reduce(Duration::plus);

    return result.orElse(Duration.ZERO);
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
    return subTasks.values().stream()
        .filter(task -> task.getEpicId() != null && task.getEpicId() == id)
        .toList();
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
  public void addEpicSubTask(int epicId, SubTask subTask) throws TaskAddException {
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
      epic.setDuration(getEpicDuration(epic));

      Optional<LocalDateTime> startTime = getEpicStartTime(epic);
      startTime.ifPresent(
          localDateTime -> epic.setStartTime(localDateTime.plus(epic.getDuration())));
    }
  }

  public Set<Task> getPrioritizedTasks() {
    return allTasks;
  }
}
