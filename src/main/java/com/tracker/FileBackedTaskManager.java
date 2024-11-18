package com.tracker;

import com.tracker.interfaces.HistoryManager;
import com.tracker.task.*;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    final protected static String FILE_NAME = "database.csv";
    final protected char CSV_DELIMITER = ';';
    private boolean fileInitialized = true;

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
    }

    public static String getFileName() {
        return FILE_NAME;
    }

    public List<String[]> parseFile(File file) {
        List<String[]> tasks = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            while (br.ready()) {
                String line = br.readLine();
                tasks.add(line.split(String.valueOf(CSV_DELIMITER)));
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


    public void loadFromFile(File file) {
        List<String[]> tasks = parseFile(file);
        for (String[] rowItem : tasks) {
            switch (TaskType.valueOf(rowItem[1])) {
                case TASK:
                    Task task = new Task(rowItem[1], rowItem[2], Status.valueOf(rowItem[3]));
                    task.setId(Integer.parseInt(rowItem[0]));
                    super.add(task);
                    break;
                case EPIC:
                    Epic epic = new Epic(rowItem[1], rowItem[2]);
                    epic.setId(Integer.parseInt(rowItem[0]));
                    super.add(epic);
                    break;
                case SUBTASK:
                    SubTask subTask = new SubTask(rowItem[1], rowItem[2], Status.valueOf(rowItem[3]));
                    subTask.setId(Integer.parseInt(rowItem[0]));
                    super.add(subTask);
                    break;
                default:
                    break;
            }
        }
    }

    public String serializeTask(Task task, String[] header) {
        HashMap<String, String> data = task.getData();
        ArrayList<String> item = new ArrayList<>();
        for (String key : header) {
            item.add(data.getOrDefault(key, ""));
        }

        return String.join(String.valueOf(CSV_DELIMITER), item);
    }

    public String serialize(String[] header) {
        ArrayList<String> result = new ArrayList<>();

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
        File file = new File(FILE_NAME);
        if (!fileInitialized) {
            if (file.exists()) {
                loadFromFile(file);
                fileInitialized = true;
                return file;
            }
        }
        return file;
    }

    public void save() {
        String[] header = {"id", "type", "name", "status", "description", "epic"};
        String csvData = serialize(header);
        String data = String.join(String.valueOf(CSV_DELIMITER), header) + "\n" +
                csvData;
        saveToFile(getFile(), data);
    }

    @Override
    public int add(Task task) {
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
