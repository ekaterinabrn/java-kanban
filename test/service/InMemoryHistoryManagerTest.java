package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Тест сколько задач может быть в истории(убран лимит 10)")
    //для ревью : убираем  же,? есть void shouldAllowUnlimitedHistorySize() ????-не ответил в прошл раз
    public void testHistoryLimit() {
        for (int i = 0; i < 11; i++) {
            Task task = new Task(Status.NEW, "Задача " + i, "Описание " + i);
            task.setId(i);
            historyManager.add(task);
        }
        
        var history = historyManager.getHistory();
        assertEquals(11, history.size());
    }

    @Test
    @DisplayName("История сохраняет данные задач")
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
     @DisplayName("Добавление задачи в историю")
    void add() {
       Task task = new Task(Status.NEW, "Test Task", "Test Description");
        task.setId(1);

         historyManager.add(task);
        final List<Task> history = historyManager.getHistory();

     assertNotNull(history, "история не должна быть пустой.");
        assertEquals(1, history.size(), "история не должна быть пустой.");
   }
}