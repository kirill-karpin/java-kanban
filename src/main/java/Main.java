import com.tracker.InMemoryTaskManager;
import com.tracker.Managers;
import com.tracker.exception.TaskAddException;
import com.tracker.interfaces.TaskManager;
import com.tracker.task.Epic;
import com.tracker.task.Status;
import com.tracker.task.SubTask;
import com.tracker.task.Task;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

  public static void main(String[] args) throws TaskAddException {

    System.out.println("Поехали!");
    InMemoryTaskManager manager = (InMemoryTaskManager) Managers.getDefaultPersist();

    LocalDateTime now = LocalDateTime.now();
    manager.add(new Task("Первая задача", "Описание", Status.NEW, Duration.ofMinutes(10), now));

    int epicId = manager.add(new Epic("Первый эпик", "Описание"));
    manager.getEpic(epicId);
    int subTaskId =
        manager.add(new SubTask("Первая подзадача", "Описание", Status.NEW, null, null));
    SubTask subTask = manager.getSubtask(subTaskId);
    manager.addEpicSubTask(epicId, subTask);
    subTask.setStatusDone();
    manager.addEpicSubTask(
        epicId,
        new SubTask(
            "Вторая подзадача",
            "Описание",
            Status.DONE,
            Duration.ofMinutes(10),
            now.plusMinutes(20)));
    manager.getSubtask(subTaskId);
    manager.getEpic(epicId);
    printAllTasks(manager);
  }

  private static void printAllTasks(TaskManager manager) {
    System.out.println("Задачи:");
    for (Task task : manager.getTasks()) {
      System.out.println(task);
    }
    System.out.println();
    System.out.println("Эпики:");
    for (Task epic : manager.getEpics()) {
      System.out.println(epic);

      for (Task task : manager.getEpicSubtasks(epic.getId())) {
        System.out.println("--> " + task);
      }
    }
    System.out.println();
    System.out.println("Подзадачи:");
    for (Task subtask : manager.getSubtasks()) {
      System.out.println(subtask);
    }
    System.out.println();
    System.out.println("История:");
    for (Task task : manager.getHistory()) {
      System.out.println(task);
    }
  }
}
