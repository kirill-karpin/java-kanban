package com.tracker.server.hadlers;

import com.sun.net.httpserver.HttpExchange;
import com.tracker.exception.RequestException;
import com.tracker.exception.TaskAddException;
import com.tracker.interfaces.TaskManager;
import com.tracker.server.HandlerResult;
import com.tracker.server.MethodEnum;
import com.tracker.task.Task;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class TasksHandler extends BaseHttpHandler {

  public TasksHandler(TaskManager taskManager) {
    super(taskManager);
  }

  @Override
  public HandlerResult doRequest(HttpExchange exchange) throws RequestException {
    HandlerResult result = new HandlerResult();
    Map<String, String> pathParams = parsePath(exchange.getRequestURI().getPath());
    switch (MethodEnum.valueOf(exchange.getRequestMethod())) {
      case GET:
        if (pathParams.containsKey("id")) {
          result.setData(getTaskById(Integer.parseInt(pathParams.get("id"))));
        } else {
          result.setData(getTasks());
        }

        break;
      case POST:

        try {
          Optional<Task> taskOptional = parseBody(exchange, Task.class);

          if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            if (task.getId() > 0) {
              Integer id = updateTask(task);
              result.setData(getTaskById(id));
            } else {
              Integer id = createTask(task);
              result.setData(getTaskById(id));
            }

          }
        } catch (IOException e) {
          throw new RequestException("Task parse error");
        } catch (TaskAddException e) {
          throw new RequestException("Task create error: " + e.getMessage(), 406);
        }
        break;
      case DELETE:
        deleteTaskById(Integer.parseInt(pathParams.get("id")));
        break;
      default:
        throw new RequestException("Unsupported method");
    }
    return result;
  }

  public Collection<Task> getTasks() {
    return taskManager.getTasks();
  }

  public Task getTaskById(Integer id) throws RequestException {
    Task task = taskManager.getTask(id);
    if (task != null) {
      return task;
    }
    throw new RequestException("Task not found", 404);
  }

  public Integer updateTask(Task task) {
    return taskManager.update(task);
  }

  public Integer createTask(Task task) throws TaskAddException {
    return taskManager.add(task);
  }

  public void deleteTaskById(Integer id) {
    Task task = taskManager.getTask(id);
    taskManager.delete(task);
  }
}
