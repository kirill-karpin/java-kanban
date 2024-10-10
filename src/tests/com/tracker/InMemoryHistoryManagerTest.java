package tests.com.tracker;

import com.tracker.InMemoryTaskManager;
import com.tracker.Managers;
import com.tracker.interfaces.HistoryManager;
import com.tracker.interfaces.TaskManager;
import com.tracker.task.Status;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.tracker.InMemoryHistoryManager;
import com.tracker.task.Task;

import java.util.List;

public class InMemoryHistoryManagerTest {

    static HistoryManager historyManager;

    @BeforeEach
    void init() {
        historyManager = Managers.getDefaultHistory();
    }

    @Test
    public void testAddAndGet() {

        Task task = new Task("Test Task", "Test Description", Status.DONE);
        historyManager.addTask(task);

        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    public void testGetEmpty() {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        assertTrue(manager.getHistory().isEmpty());
    }

    @Test
    public void testGetLimitedHistory() {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        for (int i = 0; i < 15; i++) {
            manager.addTask( new Task("Test Task" + i, "Test Description", Status.DONE));
        }
        assertEquals(10, manager.getHistory().size());
    }

    @Test
    public void testGetLimitedHistoryLessTen() {
        InMemoryHistoryManager manager = new InMemoryHistoryManager();
        for (int i = 0; i < 8; i++) {
            manager.addTask( new Task("Test Task" + i, "Test Description", Status.DONE));
        }
        assertEquals(8, manager.getHistory().size());
    }
}

