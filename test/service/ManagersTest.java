package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManagersTest {

    @Test
    public void testGetDefaultReturnsInitializedTaskManager() {
        TaskManager taskManager = Managers.getDefault();
        
        assertNotNull(taskManager);
        assertTrue(taskManager instanceof InMemoryTaskManager);
        
        assertNotNull(taskManager.getAllTask());
        assertNotNull(taskManager.getAllEpics());
        assertNotNull(taskManager.getAllSubtask());
        assertNotNull(taskManager.getHistory());
    }

    @Test
    public void testGetDefaultHistoryReturnsInitializedHistoryManager() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        
        assertNotNull(historyManager);
        assertTrue(historyManager instanceof InMemoryHistoryManager);
        
        assertNotNull(historyManager.getHistory());
        assertTrue(historyManager.getHistory().isEmpty());
    }
}