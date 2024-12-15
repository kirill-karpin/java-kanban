package com.tracker;

import com.tracker.interfaces.TaskManager;
import com.tracker.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.tracker.task.Status.DONE;
import static com.tracker.task.Status.NEW;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private static TaskManager taskManager;

    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }


    @Test
    void getTaskTest() {
        Task task = new Task(
                "Test addNewTask",
                "Test addNewTask description",
                NEW,
                Duration.ofMinutes(10),
                LocalDateTime.now()
        );
        int taskId = taskManager.add(task);
        final Task savedTask = taskManager.getTask(taskId);
        assertNotNull(
                savedTask,
                "Задача не найдена."
        );
    }

    @Test
    void addTaskTest() {
        Task task = new Task("Test addNewTask",
                "Test addNewTask description",
                NEW,
                Duration.ofMinutes(10),
                LocalDateTime.now()
        );
        int taskId = taskManager.add(task);

        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(
                savedTask,
                "Задача не найдена."
        );
        assertEquals(
                task,
                savedTask,
                "Задачи не совпадают."
        );

        final List<Task> tasks = taskManager.getTasks();

        assertNotNull(
                tasks,
                "Задачи не возвращаются."
        );
        assertEquals(
                1,
                tasks.size(),
                "Неверное количество задач."
        );
        assertEquals(
                task,
                tasks.getFirst(),
                "Задачи не совпадают."
        );
    }

    @Test
    void updateTaskTest() {
        Task task = new Task("Test addNewTask",
                "Test addNewTask description",
                NEW,
                Duration.ofMinutes(10),
                LocalDateTime.now()
        );
        int taskId = taskManager.add(task);
        Task taskInStore = taskManager.getTask(taskId);

        Task taskUpdate = new Task("Test updatedTask",
                "Test addNewTask description",
                DONE,
                Duration.ofMinutes(10),
                LocalDateTime.now()
        );
        final int taskUpdateId = taskManager.update(taskUpdate);
        Task taskInStoreUpdated = taskManager.getTask(taskUpdateId);
        assertNotEquals(
                taskInStore,
                taskInStoreUpdated,
                "Задачи совпадают."
        );
    }

    @Test
    void delete() {
        Task task = new Task("Test addNewTask",
                "Test addNewTask description",
                NEW,
                Duration.ofMinutes(10),
                LocalDateTime.now()
        );
        int taskId = taskManager.add(task);
        Task taskInStore = taskManager.getTask(taskId);
        taskManager.delete(taskInStore);
        assertNull(
                taskManager.getTask(taskId),
                "Задача не удалена."
        );
    }

    @Test
    void addTwoOverlappingTasks() {
        LocalDateTime now = LocalDateTime.now();
        int durationMinutes = 15;
        Task task1 = new Task(
                "Test addNewTask",
                "Test addNewTask description",
                NEW,
                Duration.ofMinutes(durationMinutes),
                now
        );

        Task task2 = new Task(
                "Test addNewTask",
                "Test addNewTask description",
                NEW,
                Duration.ofMinutes(durationMinutes),
                now
        );


        taskManager.add(task1);

        assertThrows(
                RuntimeException.class,
                () -> taskManager.add(task2)
        );

    }

}