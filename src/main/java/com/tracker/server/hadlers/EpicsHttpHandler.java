package com.tracker.server.hadlers;

import com.sun.net.httpserver.HttpExchange;
import com.tracker.exception.RequestException;
import com.tracker.exception.TaskAddException;
import com.tracker.interfaces.TaskManager;
import com.tracker.server.HandlerResult;
import com.tracker.server.MethodEnum;
import com.tracker.task.Epic;
import com.tracker.task.SubTask;
import com.tracker.task.Task;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class EpicsHttpHandler extends BaseHttpHandler {

  public EpicsHttpHandler(TaskManager taskManager) {
    super(taskManager);
  }

  @Override
  public HandlerResult doRequest(HttpExchange exchange) throws RequestException {
    HandlerResult result = new HandlerResult();
    Map<String, String> pathParams = parsePath(exchange.getRequestURI().getPath());
    switch (MethodEnum.valueOf(exchange.getRequestMethod())) {
      case GET:

        if (pathParams.containsKey("id")) {
          int id = Integer.parseInt(pathParams.get("id"));
          Epic epic = getEpicById(id);

          if (pathParams.containsKey("sub")) {
            result.setData(getEpicSubTasks(epic.getId()));
          } else {
            if (epic != null) {
              result.setData(epic);
            } else {
              throw new RequestException("Epic not found", 404);
            }
          }
        } else {
          result.setData(getEpics());
        }

        break;
      case POST:

        try {
          Optional<Epic> epicOptional = parseBody(exchange, Epic.class);

          if (epicOptional.isPresent()) {
            Epic task = epicOptional.get();
            if (task.getId() > 0) {
              result.setData(getEpicById(updateEpic(task)));
            } else {
              result.setData(getEpicById(createEpic(task)));
            }

          }
        } catch (IOException e) {
          throw new RequestException("Epic parse error");
        } catch (TaskAddException e) {
          throw new RequestException("Epic create error", 406);
        }

        break;
      case DELETE:
        deleteEpicById(Integer.parseInt(pathParams.get("id")));
        break;
      default:
        throw new RequestException("Unsupported method");
    }
    return result;
  }

  public Collection<Epic> getEpics() {
    return taskManager.getEpics();

  }

  public Epic getEpicById(Integer id) throws RequestException {
    Epic epic = taskManager.getEpic(id);
    if (epic != null) {
      return epic;
    }
    throw new RequestException("Epic not found", 404);
  }

  public Collection<SubTask> getEpicSubTasks(Integer id) {
    return taskManager.getEpicSubtasks(id);
  }

  public Integer updateEpic(Task task) {
    return taskManager.update(task);
  }

  public Integer createEpic(Task task) throws TaskAddException {
    return taskManager.add(task);
  }

  public void deleteEpicById(Integer id) throws RequestException {
    Epic task = getEpicById(id);
    taskManager.delete(task);
  }
}
