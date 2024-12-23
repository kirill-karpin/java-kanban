package com.tracker.server.hadlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.tracker.exception.RequestException;
import com.tracker.interfaces.TaskManager;
import com.tracker.server.HandlerInterface;
import com.tracker.server.HandlerResult;
import com.tracker.server.JsonBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class BaseHttpHandler implements HttpHandler, HandlerInterface {

  protected static TaskManager taskManager;

  public BaseHttpHandler(TaskManager taskManager) {
    BaseHttpHandler.taskManager = taskManager;
  }

  protected void send(HttpExchange h, HandlerResult result) throws IOException {
    String text;

    if (result.isSuccess()) {
      text = toJson(result.getData());
    } else {
      text = toJson(result.getErrorMessage());
      System.out.println(text);
    }
    h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
    byte[] resp = text.getBytes(StandardCharsets.UTF_8);
    h.sendResponseHeaders(result.getCode(), resp.length);
    if (!text.isEmpty()) {
      h.getResponseBody().write(resp);
    }
    h.close();
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    HandlerResult result = new HandlerResult();
    try {

      result = doRequest(exchange);

    } catch (RequestException e) {

      result.setCode(e.getCode());
      result.addError(e.getMessage());
    }
    send(exchange, result);
  }

  public String toJson(Object obj) {
    return JsonBuilder.build().toJson(obj);
  }

  public <T> Optional<T> parseBody(HttpExchange exchange, Class<T> clazz) throws IOException {
    String bodyString = new String(exchange.getRequestBody().readAllBytes(),
        StandardCharsets.UTF_8);
    return Optional.ofNullable(JsonBuilder.build().fromJson(bodyString, clazz));
  }

  public Map<String, String> parsePath(String string) {
    Map<String, String> result = new HashMap<>();
    String[] split = string.split("/");
    if (split.length > 3) {

      result.put("path", split[1]);
      result.put("id", split[2]);
      result.put("sub", split[3]);

    } else if (split.length > 2) {
      result.put("path", split[1]);
      result.put("id", split[2]);
    }
    return result;
  }
}
