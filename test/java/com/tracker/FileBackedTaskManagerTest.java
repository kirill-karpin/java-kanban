package com.tracker;

import com.tracker.task.Epic;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest {
    private final String[] header = {"id", "type", "name", "status", "description", "epic"};

    @Test
    void readFileEmpty() throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(null);
        File file = File.createTempFile(FileBackedTaskManager.getFileName(), null);
        List<String[]> result = fileBackedTaskManager.parseFile(file);
        assertEquals(0, result.size());
    }

    @Test
    void saveEmptyFile() throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(null);
        File file = File.createTempFile(FileBackedTaskManager.getFileName(), ".csv");
        Boolean result = fileBackedTaskManager.saveToFile(file, "");
        assertTrue(result);
    }

    @Test
    void saveFileToFile() throws IOException {
        Epic epic = new Epic("Первая задача", "Описание");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(null);
        File file = File.createTempFile(FileBackedTaskManager.getFileName(), ".csv");

        Boolean result = fileBackedTaskManager.saveToFile(file, fileBackedTaskManager.serializeTask(epic, header));
        assertTrue(result);
    }

    @Test
    void saveFileAndParseFIle() throws IOException {
        Epic epic = new Epic("Первая задача", "Описание");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(null);
        File file = File.createTempFile(FileBackedTaskManager.getFileName(), ".csv");
        Boolean result = fileBackedTaskManager.saveToFile(file, fileBackedTaskManager.serializeTask(epic, header));
        assertTrue(result);
        List<String[]> result2 = fileBackedTaskManager.parseFile(file);
        assertEquals(1, result2.size());
    }

    @Test
    void loadFromFile() throws IOException {
        Epic epic = new Epic("Первая задача", "Описание");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(null);
        File file = File.createTempFile(FileBackedTaskManager.getFileName(), ".csv");
        Boolean result = fileBackedTaskManager.saveToFile(file, fileBackedTaskManager.serializeTask(epic, header));
        assertTrue(result);
        fileBackedTaskManager.loadFromFile(file);
        assertEquals(1, fileBackedTaskManager.getEpics().size());
    }


}