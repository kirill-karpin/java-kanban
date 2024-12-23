package com.tracker.server.hadlers;

import com.tracker.Managers;
import com.tracker.exception.TaskAddException;
import com.tracker.interfaces.TaskManager;
import com.tracker.server.HttpTaskServer;
import com.tracker.task.Epic;
import com.tracker.task.Status;
import com.tracker.task.SubTask;
import com.tracker.task.Task;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class AbstractRestTest {

  private static final String BASE_URL = "http://localhost:8080";
  protected HttpTaskServer server;
  protected TaskManager manager;

  @BeforeEach
  void setUp() throws IOException {
    manager = Managers.getDefault();
    server = new HttpTaskServer(manager);
    server.start();
  }

  @AfterEach
  void shutDown() throws IOException {
    server.stop();
  }

  public HttpResponse<String> clientRequest(String path, Function<Builder, Builder> callback)
      throws IOException, InterruptedException {
    HttpClient client = HttpClient.newHttpClient();
    URI url = URI.create(BASE_URL + path);
    Builder builder = HttpRequest.newBuilder()
        .uri(url)
        .version(Version.HTTP_1_1);
    callback.apply(builder);

    HttpRequest request = builder.build();
    return client.send(request, HttpResponse.BodyHandlers.ofString());
  }

  public Task createTaskInManager() throws TaskAddException {
    Task innerTask = getNewTask();
    manager.add(innerTask);
    return innerTask;
  }

  public static Task getNewTask() {
    return new Task("Task Test 2", "Testing task 1",
        Status.NEW, Duration.ofMinutes(5), LocalDateTime.now());
  }

  public SubTask createSubTaskInManager() throws TaskAddException {
    SubTask innerTask = getNewSubTask();
    manager.add(innerTask);
    return innerTask;
  }

  public SubTask getNewSubTask() {
    return new SubTask("SubTsk Test 2", "Testing task 1",
        Status.NEW, Duration.ofMinutes(5), LocalDateTime.now().plusMinutes(60));
  }

  public Epic createEpicTaskInManager() throws TaskAddException {
    Epic innerTask = getNewEpic();
    manager.add(innerTask);
    return innerTask;
  }

  public static Epic getNewEpic() {
    return new Epic("SubTsk Test 2", "Testing task 1");
  }
}
