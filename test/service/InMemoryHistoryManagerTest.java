package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    private InMemoryHistoryManager historyManager;

    @BeforeEach
    public void setUp() {
        historyManager = new InMemoryHistoryManager();
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

     assertNotNull(history, "история не должна быть пустой.");
        assertEquals(1, history.size(), "история не должна быть пустой.");
   }


    @DisplayName("Проверка  пустой истории")
    @Test
    public void testEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history);
        assertTrue(history.isEmpty());
    }


    @DisplayName("Повторное добавление задачи перемещает её в конец")
    @Test
    public void testNoDuplicates() {
        Task task1 = new Task(Status.NEW, "Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task(Status.NEW, "Task 2", "Description 2");
        task2.setId(2);
        Task task3 = new Task(Status.NEW, "Task 3", "Description 3");
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size(), "История не должна содержать дубликаты");
        assertEquals(1, history.get(2).getId(), "Повторно добавленная задача должна быть в конце");
    }


    @DisplayName("Удаление из истории: удаление из начала истории")
    @Test
    public void testRemoveFromBeginning() {
        Task task1 = new Task(Status.NEW, "Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task(Status.NEW, "Task 2", "Description 2");
        task2.setId(2);
        Task task3 = new Task(Status.NEW, "Task 3", "Description 3");
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(1);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertFalse(history.stream().anyMatch(t -> t.getId() == 1));
    }

    @DisplayName("Удаления из истории: удаление из середины истории")
    @Test
    public void testRemoveFromMiddle() {
        Task task1 = new Task(Status.NEW, "Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task(Status.NEW, "Task 2", "Description 2");
        task2.setId(2);
        Task task3 = new Task(Status.NEW, "Task 3", "Description 3");
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertFalse(history.stream().anyMatch(t -> t.getId() == 2));
    }

    @DisplayName("Удаления из истории: удаление из конца истории")
    @Test
    public void testRemoveFromEnd() {
        Task task1 = new Task(Status.NEW, "Task 1", "Description 1");
        task1.setId(1);
        Task task2 = new Task(Status.NEW, "Task 2", "Description 2");
        task2.setId(2);
        Task task3 = new Task(Status.NEW, "Task 3", "Description 3");
        task3.setId(3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        historyManager.remove(3);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertFalse(history.stream().anyMatch(t -> t.getId() == 3));
    }
}