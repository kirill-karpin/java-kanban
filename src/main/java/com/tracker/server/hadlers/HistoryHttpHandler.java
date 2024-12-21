package com.tracker.server.hadlers;

import com.sun.net.httpserver.HttpExchange;
import com.tracker.exception.RequestException;
import com.tracker.interfaces.TaskManager;
import com.tracker.server.HandlerResult;
import com.tracker.task.Task;
import java.util.Collection;

public class HistoryHttpHandler extends BaseHttpHandler {

  public HistoryHttpHandler(TaskManager taskManager) {
    super(taskManager);
  }

  @Override
  public HandlerResult doRequest(HttpExchange exchange) throws RequestException {
    HandlerResult result = new HandlerResult();
    switch (exchange.getRequestMethod()) {
      case "GET":
        result.setData(getHistory());
        break;
      default:
        throw new RequestException("Unsupported method");
    }
    return result;
  }

  public Collection<Task> getHistory() {
    return taskManager.getHistory();
  }
}
