package com.tracker;

import com.tracker.exception.TaskAddException;
import com.tracker.interfaces.HistoryManager;
import com.tracker.task.Epic;
import com.tracker.task.Status;
import com.tracker.task.SubTask;
import com.tracker.task.Task;
import com.tracker.task.TaskType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

  private static boolean fileInitialized = false;

  public FileBackedTaskManager(HistoryManager historyManager) {
    super(historyManager);
    getFile();
  }

  public static String getFileName() {
    return "database.csv";
  }

  public static String getDelimiter() {
    return ";";
  }

  public static String[] getDefaultHeader() {
    return new String[]{
        "id", "type", "name", "status", "description", "epic", "startTime", "duration"
    };
  }

  public List<String[]> parseFile(File file) {
    List<String[]> tasks = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      while (br.ready()) {
        String line = br.readLine();
        var item = line.split(getDelimiter(), -1);
        tasks.add(item);
      }
      return tasks;
    } catch (IOException e) {
      System.out.println("Произошла ошибка во время чтения файла.");
      return tasks;
    }
  }

  public Boolean saveToFile(File file, String data) {
    try (PrintWriter writer = new PrintWriter(file)) {
      writer.println(data);
      return true;
    } catch (IOException e) {
      System.out.println("Произошла ошибка во время записи файла.");
    }
    return false;
  }

  public void loadFromFile(File file, boolean firstLineIsHeader) {
    List<String[]> tasks = parseFile(file);

    for (String[] rowItem : tasks) {
      if (firstLineIsHeader) {
        firstLineIsHeader = false;
        continue;
      }
      try {
        int durationMinutes = Integer.parseInt(!rowItem[7].isEmpty() ? rowItem[7] : "0");
        Duration duration = Duration.ZERO.plusMinutes(durationMinutes);
        LocalDateTime startAt = null;
        if (!rowItem[6].isEmpty()) {
          startAt = LocalDateTime.parse(rowItem[6]);
        }
        switch (TaskType.valueOf(rowItem[1])) {
          case TASK:
            Task task =
                new Task(rowItem[1], rowItem[2], Status.valueOf(rowItem[3]), duration, startAt);
            task.setId(Integer.parseInt(rowItem[0]));
            super.add(task);
            break;
          case EPIC:
            Epic epic = new Epic(rowItem[1], rowItem[2]);
            epic.setId(Integer.parseInt(rowItem[0]));
            super.add(epic);
            break;
          case SUBTASK:
            SubTask subTask =
                new SubTask(rowItem[1], rowItem[2], Status.valueOf(rowItem[3]), duration, startAt);
            subTask.setId(Integer.parseInt(rowItem[0]));
            super.add(subTask);
            break;
          default:
            break;
        }
      } catch (TaskAddException e) {
        throw new RuntimeException("Ошибка чтения задачи : " + e.getMessage());
      }
    }
  }

  public String serializeTask(Task task, String[] header) {
    Map<String, String> data = task.getData();
    List<String> item = new ArrayList<>();
    for (String key : header) {
      item.add(data.getOrDefault(key, ""));
    }

    return String.join(getDelimiter(), item);
  }

  public String serialize(String[] header) {
    List<String> result = new ArrayList<>();

    for (Epic epic : super.getEpics()) {
      result.add(serializeTask(epic, header));
    }

    for (Task task : super.getTasks()) {
      result.add(serializeTask(task, header));
    }

    for (SubTask subTask : super.getSubtasks()) {
      result.add(serializeTask(subTask, header));
    }

    return String.join("\n", result);
  }

  public File getFile() {
    File file = new File(getFileName());

    if (!fileInitialized) {
      fileInitialized = true;
      if (file.exists()) {
        loadFromFile(file, true);
        return file;
      }
    }
    return file;
  }

  public void save() {
    String[] header = getDefaultHeader();
    String csvData = serialize(header);
    String data = String.join(getDelimiter(), header) + "\n" + csvData;
    saveToFile(getFile(), data);
  }

  @Override
  public int add(Task task) throws TaskAddException {
    int id = super.add(task);
    save();
    return id;
  }

  @Override
  public void delete(Task task) {
    super.delete(task);
    save();
  }

  @Override
  public int update(Task updateTask) {
    int id = super.update(updateTask);
    save();
    return id;
  }
}
