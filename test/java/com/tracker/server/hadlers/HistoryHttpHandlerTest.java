package com.tracker.server.hadlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.tracker.exception.TaskAddException;
import com.tracker.server.JsonBuilder;
import com.tracker.task.Epic;
import com.tracker.task.SubTask;
import com.tracker.task.Task;
import java.io.IOException;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.List;
import org.junit.jupiter.api.Test;

class HistoryHttpHandlerTest extends AbstractRestTest {

  @Test
  void getHistory() throws TaskAddException, IOException, InterruptedException {
    Task task = createTaskInManager();
    SubTask subTask = createSubTaskInManager();
    Epic epic = createEpicTaskInManager();

    clientRequest("/tasks/" + task.getId(), Builder::GET);
    clientRequest("/subtasks/" + subTask.getId(), Builder::GET);
    clientRequest("/epics/" + epic.getId(), Builder::GET);

    HttpResponse<String> response = clientRequest("/history", Builder::GET);

    String responseBody = response.body();

    List<Task> tasks = JsonBuilder.build().fromJson(responseBody, List.class);

    assertEquals(3, tasks.size());
  }
}
