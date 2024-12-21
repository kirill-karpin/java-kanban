package com.tracker.server.hadlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tracker.exception.TaskAddException;
import com.tracker.server.JsonBuilder;
import com.tracker.task.SubTask;
import com.tracker.task.Task;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.List;
import org.junit.jupiter.api.Test;

class SubTasksHttpHandlerTest extends AbstractRestTest {

  @Test
  void getSubTasks() throws TaskAddException, IOException, InterruptedException {

    createSubTaskInManager();

    HttpResponse<String> response = clientRequest("/subtasks", Builder::GET);

    assertEquals(200, response.statusCode());
    String responseBody = response.body();

    List<SubTask> tasks = JsonBuilder.build().fromJson(responseBody, List.class);

    // проверяем, что создалась одна задача с корректным именем
    List<SubTask> tasksFromManager = manager.getSubtasks();

    assertNotNull(tasksFromManager, "Задачи не возвращаются");
    assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    assertEquals(1, tasks.size(), "Некорректное количество задач");
  }

  @Test
  void getSubTaskById() throws TaskAddException, IOException, InterruptedException {
    SubTask innerTask = createSubTaskInManager();
    HttpResponse<String> response = clientRequest("/subtasks/" + innerTask.getId(), Builder::GET);

    // проверяем код ответа
    assertEquals(200, response.statusCode());
    String responseBody = response.body();

    Task publicTask = JsonBuilder.build().fromJson(responseBody, SubTask.class);
    assertNotNull(publicTask, "Подзадача не возвращается");
  }

  @Test
  void updateSubTask() throws TaskAddException, IOException, InterruptedException {
    SubTask innerTask = createSubTaskInManager();
    String taskJson = JsonBuilder.build().toJson(innerTask);
    Task updateTask = JsonBuilder.build().fromJson(taskJson, SubTask.class);
    updateTask.setName("UPDATE");

    String updateTaskJson = JsonBuilder.build().toJson(updateTask);

    HttpResponse<String> response = clientRequest("/subtasks",
        (builder) -> builder.POST(HttpRequest.BodyPublishers.ofString(updateTaskJson)));

    // проверяем код ответа
    assertEquals(200, response.statusCode());
    String responseBody = response.body();

    SubTask publicTask = JsonBuilder.build().fromJson(responseBody, SubTask.class);
    assertEquals(publicTask.getName(), updateTask.getName(), "Задача не обновлена");
  }

  @Test
  void createSubTask() throws IOException, InterruptedException {
    // создаём задачу
    SubTask task = getNewSubTask();
    // конвертируем её в JSON
    String taskJson = JsonBuilder.build().toJson(task);

    HttpResponse<String> response = clientRequest("/subtasks",
        (builder) -> builder.POST(HttpRequest.BodyPublishers.ofString(taskJson)));

    // проверяем код ответа
    assertEquals(200, response.statusCode());

    // проверяем, что создалась одна задача с корректным именем
    List<SubTask> tasksFromManager = manager.getSubtasks();

    assertNotNull(tasksFromManager, "Задачи не возвращаются");
    assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    assertEquals(task.getName(), tasksFromManager.get(0).getName(), "Некорректное имя задачи");
  }

  @Test
  void deleteSubTask() throws TaskAddException, IOException, InterruptedException {

    SubTask task = createSubTaskInManager();
    int id = task.getId();
    HttpResponse<String> response = clientRequest("/subtasks/" + id, Builder::DELETE);

    // проверяем код ответа
    assertEquals(200, response.statusCode());
    // проверяем, что создалась одна задача с корректным именем
    Task taskInStore = manager.getSubtask(id);
    assertNull(taskInStore, "Подзадача не удалена");
  }

  @Test
  void getTaskByIdError404() throws TaskAddException, IOException, InterruptedException {
    HttpResponse<String> response = clientRequest("/subtasks/" + 1, Builder::GET);
    // проверяем код ответа
    assertEquals(404, response.statusCode());
  }

  @Test
  void getTaskByIdError406() throws TaskAddException, IOException, InterruptedException {
    // создаём задачу
    createSubTaskInManager();
    SubTask task = getNewSubTask();
    String taskJson = JsonBuilder.build().toJson(task);

    HttpResponse<String> response = clientRequest("/subtasks",
        (builder) -> builder.POST(HttpRequest.BodyPublishers.ofString(taskJson)));

    // проверяем код ответа
    assertEquals(406, response.statusCode());
  }
}
