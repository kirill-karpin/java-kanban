package com.tracker.server.hadlers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.tracker.exception.TaskAddException;
import com.tracker.server.JsonBuilder;
import com.tracker.task.Task;
import java.io.IOException;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.util.List;
import org.junit.jupiter.api.Test;


class PrioritizedHttpHandlerTest extends AbstractRestTest {

  @Test
  void getPrioritizedTasks() throws TaskAddException, IOException, InterruptedException {
    createTaskInManager();
    createSubTaskInManager();
    createEpicTaskInManager();

    HttpResponse<String> response = clientRequest("/prioritized", Builder::GET);

    String responseBody = response.body();

    List<Task> tasks = JsonBuilder.build().fromJson(responseBody, List.class);

    assertEquals(2, tasks.size());

  }
}
