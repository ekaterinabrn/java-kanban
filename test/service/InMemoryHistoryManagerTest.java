package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
    }

    @Test
    public void testAddTaskToHistory() {
        Task task = new Task(Status.NEW, "Задача", "Описание");
        task.setId(1);
        
        historyManager.add(task);
        var history = historyManager.getHistory();
        
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    public void testHistoryLimit() {
        for (int i = 0; i < 11; i++) {
            Task task = new Task(Status.NEW, "Задача " + i, "Описание " + i);
            task.setId(i);
            historyManager.add(task);
        }
        
        var history = historyManager.getHistory();
        assertEquals(10, history.size());
    }

    @Test
    public void testHistoryPreservesTaskData() {
        Task originalTask = new Task(Status.DONE, "Оригинальная задача", "Оригинальное описание");
        originalTask.setId(5);
        
        historyManager.add(originalTask);
        var history = historyManager.getHistory();
        
        Task taskFromHistory = history.get(0);
        
        assertEquals(originalTask.getId(), taskFromHistory.getId());
        assertEquals(originalTask.getName(), taskFromHistory.getName());
        assertEquals(originalTask.getDescription(), taskFromHistory.getDescription());
        assertEquals(originalTask.getStatus(), taskFromHistory.getStatus());
    }

    // Тест добавления в историю
     @Test
    void add() {
       Task task = new Task(Status.NEW, "Test Task", "Test Description");
        task.setId(1);

         historyManager.add(task);
        final List<Task> history = historyManager.getHistory();

     assertNotNull(history, "После добавления задачи, история не должна быть пустой.");
        assertEquals(1, history.size(), "После добавления задачи, история не должна быть пустой.");
   }
}