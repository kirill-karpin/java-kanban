package com.tracker.server.hadlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tracker.exception.TaskAddException;
import com.tracker.server.JsonBuilder;
import com.tracker.task.Task;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.List;
import org.junit.jupiter.api.Test;

class TasksHandlerTest extends AbstractRestTest {

  @Test
  void getTaskById() throws TaskAddException, IOException, InterruptedException {
    Task innerTask = createTaskInManager();

    HttpResponse<String> response = clientRequest("/tasks/" + innerTask.getId(), Builder::GET);
    // проверяем код ответа
    assertEquals(200, response.statusCode());
    String responseBody = response.body();

    Task publicTask = JsonBuilder.build().fromJson(responseBody, Task.class);
    assertNotNull(publicTask, "Задача не возвращается");
  }

  @Test
  void updateTask() throws TaskAddException, IOException, InterruptedException {
    Task innerTask = createTaskInManager();

    String taskJson = JsonBuilder.build().toJson(innerTask);
    Task updateTask = JsonBuilder.build().fromJson(taskJson, Task.class);
    updateTask.setName("UPDATE");
    String updateTaskJson = JsonBuilder.build().toJson(updateTask);

    HttpResponse<String> response = clientRequest("/tasks",
        (builder) -> builder.POST(HttpRequest.BodyPublishers.ofString(updateTaskJson)));

    // проверяем код ответа
    assertEquals(200, response.statusCode());
    String responseBody = response.body();

    Task publicTask = JsonBuilder.build().fromJson(responseBody, Task.class);
    assertEquals(publicTask.getName(), updateTask.getName(), "Задача не обновлена");

  }

  @Test
  void createTask() throws IOException, InterruptedException {
    // создаём задачу
    Task task = getNewTask();
    // конвертируем её в JSON
    String taskJson = JsonBuilder.build().toJson(task);

    HttpResponse<String> response = clientRequest("/tasks",
        (builder) -> builder.POST(HttpRequest.BodyPublishers.ofString(taskJson)));

    // проверяем код ответа
    assertEquals(200, response.statusCode());

    // проверяем, что создалась одна задача с корректным именем
    List<Task> tasksFromManager = manager.getTasks();

    assertNotNull(tasksFromManager, "Задачи не возвращаются");
    assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    assertEquals(task.getName(), tasksFromManager.get(0).getName(), "Некорректное имя задачи");
  }

  @Test
  void getTasks() throws IOException, InterruptedException, TaskAddException {
    createTaskInManager();
    HttpResponse<String> response = clientRequest("/tasks", Builder::GET);

    // проверяем код ответа
    assertEquals(200, response.statusCode());
    String responseBody = response.body();

    List<Task> tasks = JsonBuilder.build().fromJson(responseBody, List.class);

    // проверяем, что создалась одна задача с корректным именем
    List<Task> tasksFromManager = manager.getTasks();

    assertNotNull(tasksFromManager, "Задачи не возвращаются");
    assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    assertEquals(1, tasks.size(), "Некорректное количество задач");
  }

  @Test
  void deleteTaskById() throws IOException, InterruptedException, TaskAddException {

    Task task = createTaskInManager();

    HttpResponse<String> response = clientRequest("/tasks/" + task.getId(), Builder::DELETE);

    // проверяем код ответа
    assertEquals(200, response.statusCode());
    // проверяем, что создалась одна задача с корректным именем
    Task taskInStore = manager.getTask(task.getId());
    assertNull(taskInStore, "Задача не удалена");
  }

  @Test
  void getTaskByIdError404() throws TaskAddException, IOException, InterruptedException {
    HttpResponse<String> response = clientRequest("/tasks/" + 1, Builder::GET);
    // проверяем код ответа
    assertEquals(404, response.statusCode());
  }

  @Test
  void getTaskByIdError406() throws TaskAddException, IOException, InterruptedException {
    // создаём задачу
    createTaskInManager();
    Task task = getNewTask();
    String taskJson = JsonBuilder.build().toJson(task);

    HttpResponse<String> response = clientRequest("/tasks",
        (builder) -> builder.POST(HttpRequest.BodyPublishers.ofString(taskJson)));

    // проверяем код ответа
    assertEquals(406, response.statusCode());
  }
}

