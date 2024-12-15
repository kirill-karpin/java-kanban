package com.tracker;

import com.tracker.task.Epic;
import com.tracker.task.Status;
import com.tracker.task.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTaskManagerTest {
    private final String[] header = FileBackedTaskManager.getDefaultHeader();

    @Test
    void readFileEmpty() throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(null);
        File file = File.createTempFile(
                FileBackedTaskManager.getFileName(),
                null
        );
        List<String[]> result = fileBackedTaskManager.parseFile(file);
        assertEquals(
                0,
                result.size()
        );
    }

    @Test
    void saveEmptyFile() throws IOException {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(null);
        File file = File.createTempFile(
                FileBackedTaskManager.getFileName(),
                ".csv"
        );
        Boolean result = fileBackedTaskManager.saveToFile(
                file,
                ""
        );
        assertTrue(result);
    }

    @Test
    void saveFileToFile() throws IOException {
        Epic epic = new Epic(
                "Первая задача",
                "Описание"
        );
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(null);
        File file = File.createTempFile(
                FileBackedTaskManager.getFileName(),
                ".csv"
        );

        Boolean result = fileBackedTaskManager.saveToFile(
                file,
                fileBackedTaskManager.serializeTask(
                        epic,
                        header
                )
        );
        assertTrue(result);
    }

    @Test
    void saveFileAndParseFIle() throws IOException {
        Epic epic = new Epic(
                "Первая задача",
                "Описание"
        );
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(null);
        File file = File.createTempFile(
                FileBackedTaskManager.getFileName(),
                ".csv"
        );
        Boolean result = fileBackedTaskManager.saveToFile(
                file,
                fileBackedTaskManager.serializeTask(
                        epic,
                        header
                )
        );
        assertTrue(result);
        List<String[]> result2 = fileBackedTaskManager.parseFile(file);
        assertEquals(
                1,
                result2.size()
        );
    }

    @Test
    void saveAndLoadEpicFromFile() throws IOException {
        Epic epic = new Epic(
                "Первая задача",
                "Описание"
        );
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(null);
        File file = File.createTempFile(
                FileBackedTaskManager.getFileName(),
                ".csv"
        );
        Boolean result = fileBackedTaskManager.saveToFile(
                file,
                fileBackedTaskManager.serializeTask(
                        epic,
                        header
                )
        );
        assertTrue(result);
        fileBackedTaskManager.loadFromFile(
                file,
                false
        );
        assertEquals(
                1,
                fileBackedTaskManager.getEpics().size()
        );
        assertEquals(
                0,
                fileBackedTaskManager.getEpics().getFirst().getDuration().toSeconds()
        );
    }

    @Test
    void saveAndLoadEpicTaskFile() throws IOException {
        Duration duration = Duration.ZERO;
        duration = duration.plusMinutes(1);
        Task task = new Task(
                "Первая задача",
                "Описание",
                Status.IN_PROGRESS,
                duration,
                LocalDateTime.now()
        );
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(null);
        File file = File.createTempFile(
                FileBackedTaskManager.getFileName(),
                ".csv"
        );
        Boolean result = fileBackedTaskManager.saveToFile(
                file,
                fileBackedTaskManager.serializeTask(
                        task,
                        header
                )
        );
        assertTrue(result);
        fileBackedTaskManager.loadFromFile(
                file,
                false
        );
        assertEquals(
                1,
                fileBackedTaskManager.getTasks().size()
        );
        assertEquals(
                1,
                fileBackedTaskManager.getTasks().getFirst().getDuration().toMinutes()
        );
    }
}