package com.tracker.server;

import com.sun.net.httpserver.HttpServer;
import com.tracker.interfaces.TaskManager;
import com.tracker.server.hadlers.EpicsHttpHandler;
import com.tracker.server.hadlers.HistoryHttpHandler;
import com.tracker.server.hadlers.PrioritizedHttpHandler;
import com.tracker.server.hadlers.SubTasksHttpHandler;
import com.tracker.server.hadlers.TasksHandler;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

  private static final int PORT = 8080;
  private static HttpServer httpServer;
  private final TaskManager taskManager;

  public HttpTaskServer(TaskManager taskManager) {
    this.taskManager = taskManager;
  }

  public void start() throws IOException {

    httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

    httpServer.createContext("/tasks", new TasksHandler(taskManager));
    httpServer.createContext("/subtasks", new SubTasksHttpHandler(taskManager));
    httpServer.createContext("/epics", new EpicsHttpHandler(taskManager));
    httpServer.createContext("/history", new HistoryHttpHandler(taskManager));
    httpServer.createContext("/prioritized", new PrioritizedHttpHandler(taskManager));

    httpServer.start(); // запускаем сервер
  }

  public void stop() {
    httpServer.stop(0);
  }
}
