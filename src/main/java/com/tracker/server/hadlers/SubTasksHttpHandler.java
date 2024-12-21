package com.tracker.server.hadlers;

import com.sun.net.httpserver.HttpExchange;
import com.tracker.exception.RequestException;
import com.tracker.exception.TaskAddException;
import com.tracker.interfaces.TaskManager;
import com.tracker.server.HandlerResult;
import com.tracker.task.SubTask;
import com.tracker.task.Task;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class SubTasksHttpHandler extends BaseHttpHandler {

  public SubTasksHttpHandler(TaskManager taskManager) {
    super(taskManager);
  }

  @Override
  public HandlerResult doRequest(HttpExchange exchange) throws RequestException {
    HandlerResult result = new HandlerResult();
    Map<String, String> pathParams = parsePath(exchange.getRequestURI().getPath());
    switch (exchange.getRequestMethod()) {
      case "GET":
        if (pathParams.containsKey("id")) {
          result.setData(getSubTaskById(Integer.parseInt(pathParams.get("id"))));
        } else {
          result.setData(getTasks());
        }

        break;
      case "POST":

        try {
          Optional<SubTask> taskOptional = parseBody(exchange, SubTask.class);

          if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            if (task.getId() > 0) {
              result.setData(getSubTaskById(updateSubTask(task)));
            } else {
              result.setData(getSubTaskById(createSubTask(task)));
            }

          }
        } catch (IOException e) {
          throw new RequestException("SubTask parse error");
        } catch (TaskAddException e) {
          throw new RequestException("SubTask create error: " + e.getMessage(), 406);
        }
        break;
      case "DELETE":
        deleteSubTask(Integer.parseInt(pathParams.get("id")));
        break;
      default:
        throw new RequestException("Unsupported method");
    }
    return result;
  }

  public Collection<SubTask> getTasks() {
    return taskManager.getSubtasks();
  }

  public SubTask getSubTaskById(Integer id) throws RequestException {
    SubTask task = taskManager.getSubtask(id);
    if (task != null) {
      return task;
    }
    throw new RequestException("Task not found", 404);
  }

  public Integer updateSubTask(Task task) {
    return taskManager.update(task);
  }

  public Integer createSubTask(Task task) throws TaskAddException {
    return taskManager.add(task);
  }

  public void deleteSubTask(Integer id) {

    Task subtask = taskManager.getSubtask(id);
    taskManager.delete(subtask);
  }


}
