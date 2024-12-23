package com.tracker.server.hadlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.tracker.exception.TaskAddException;
import com.tracker.server.JsonBuilder;
import com.tracker.task.Epic;
import com.tracker.task.SubTask;
import com.tracker.task.Task;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.List;
import org.junit.jupiter.api.Test;

class EpicsHttpHandlerTest extends AbstractRestTest {

  @Test
  void getEpics() throws TaskAddException, IOException, InterruptedException {
    createEpicTaskInManager();
    HttpResponse<String> response = clientRequest("/epics", Builder::GET);

    assertEquals(200, response.statusCode());
    String responseBody = response.body();

    List<Epic> tasks = JsonBuilder.build().fromJson(responseBody, List.class);

    // проверяем, что создалась одна задача с корректным именем
    List<Epic> tasksFromManager = manager.getEpics();

    assertNotNull(tasksFromManager, "Задачи не возвращаются");
    assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    assertEquals(1, tasks.size(), "Некорректное количество задач");
  }

  @Test
  void getEpicById() throws TaskAddException, IOException, InterruptedException {
    Epic epic = createEpicTaskInManager();

    HttpResponse<String> response = clientRequest("/epics/" + epic.getId(), Builder::GET);

    // проверяем код ответа
    assertEquals(200, response.statusCode());
    String responseBody = response.body();

    Task publicEpic = JsonBuilder.build().fromJson(responseBody, Task.class);
    // проверяем, что создалась одна задача с корректным именем
    assertNotNull(publicEpic, "Эпик не возвращается");
  }

  @Test
  void getEpicSubTasks() throws TaskAddException, IOException, InterruptedException {
    Epic epic = createEpicTaskInManager();
    SubTask subTask = createSubTaskInManager();
    manager.addEpicSubTask(epic.getId(), subTask);

    HttpResponse<String> response = clientRequest("/epics/" + epic.getId() + "/subtasks",
        Builder::GET);
    String responseBody = response.body();

    List<SubTask> tasks = JsonBuilder.build().fromJson(responseBody, List.class);
    assertEquals(1, tasks.size(), "Задача не обновлена");
  }

  @Test
  void updateEpic() throws TaskAddException, IOException, InterruptedException {
    Epic epic = createEpicTaskInManager();
    manager.add(epic);
    String innerEpicJson = JsonBuilder.build().toJson(epic);
    Task updateEpic = JsonBuilder.build().fromJson(innerEpicJson, Epic.class);
    updateEpic.setName("UPDATE");
    String updateTaskJson = JsonBuilder.build().toJson(updateEpic);

    HttpResponse<String> response = clientRequest("/epics",
        (builder) -> builder.POST(HttpRequest.BodyPublishers.ofString(updateTaskJson)));

    // проверяем код ответа
    assertEquals(200, response.statusCode());
    String responseBody = response.body();

    Epic publicTask = JsonBuilder.build().fromJson(responseBody, Epic.class);
    assertEquals(publicTask.getName(), updateEpic.getName(), "Задача не обновлена");

  }

  @Test
  void createEpic() throws IOException, InterruptedException, TaskAddException {
    Epic epic = getNewEpic();
    String jsonString = JsonBuilder.build().toJson(epic);

    HttpResponse<String> response = clientRequest("/epics",
        (builder) -> builder.POST(HttpRequest.BodyPublishers.ofString(jsonString)));

    // проверяем код ответа
    assertEquals(200, response.statusCode());

    // проверяем, что создалась одна задача с корректным именем
    List<Epic> tasksFromManager = manager.getEpics();

    assertNotNull(tasksFromManager, "Задачи не возвращаются");
    assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
    assertEquals(epic.getName(), tasksFromManager.get(0).getName(), "Некорректное имя задачи");
  }

  @Test
  void deleteEpicById() throws IOException, InterruptedException, TaskAddException {
    Epic epic = createEpicTaskInManager();

    HttpResponse<String> response = clientRequest("/epics/" + epic.getId(), Builder::DELETE);

    // проверяем код ответа
    assertEquals(200, response.statusCode());
    // проверяем, что создалась одна задача с корректным именем
    Epic taskInStore = manager.getEpic(epic.getId());
    assertNull(taskInStore, "Задача не удалена");
  }
}
