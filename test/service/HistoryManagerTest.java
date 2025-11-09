package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    
    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    private static final String DESCRIPTION = "Описание";
    private static final String TASK1_IS_FIRST ="Первая задача должна быть задача 1";
    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        
        // Создаём тестовые задачи с разными ID
        task1 = new Task(Status.NEW, "Task 1", DESCRIPTION);
        task1.setId(1);
        
        task2 = new Task(Status.NEW, "Task 2", DESCRIPTION);
        task2.setId(2);
        
        task3 = new Task(Status.NEW, "Task 3", DESCRIPTION);
        task3.setId(3);
    }

    @Test
    @DisplayName("Дубликаты удаляются и перемещаются в конец списка")
    void shouldRemoveDuplicatesAndMoveToEndTest() {
        // Добавляем задачи в историю
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();
        assertEquals(3, history.size(), "История должна содержать 3 задачи");
        
        // Повторно добавляем task1
        historyManager.add(task1);

        history = historyManager.getHistory();
        assertEquals(3, history.size(), "Размер истории не должен измениться");
        assertEquals(2, history.get(0).getId(), "Первой должна быть задача 2");
        assertEquals(3, history.get(1).getId(), "Второй должна быть задача 3");
        assertEquals(1, history.get(2).getId(), "Третьей (последней) должна быть задача 1");
    }
    

    @Test
    @DisplayName("История может содержать неограниченное количество задач")
    void shouldAllowUnlimitedHistorySizeTest() {
        // Добавляем 100 задач
        for (int i = 1; i <= 100; i++) {
            Task task = new Task(Status.NEW, "Task " + i, DESCRIPTION + i);
            task.setId(i);
            historyManager.add(task);
        }
        
        // Проверяем, что все задачи сохранились
        List<Task> history = historyManager.getHistory();
        assertEquals(100, history.size(), "История должна содержать все 100 задач");
        assertEquals(1, history.get(0).getId(), TASK1_IS_FIRST);
        assertEquals(100, history.get(99).getId(), "Последняя задача должна быть Task 100");
    }

    @Test
    @DisplayName("Задача удаляется из истории")
    void shouldRemoveTaskFromHistoryTest() {
        // Добавляем задачи
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        assertEquals(3, historyManager.getHistory().size());
        // Удаляем задачу 2
        historyManager.remove(2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size(), "После удаления должно остаться 2 задачи");
        assertEquals(1, history.get(0).getId(), TASK1_IS_FIRST);
        assertEquals(3, history.get(1).getId(), "Вторая задача должна быть Task 3");
    }

    @Test
    @DisplayName("Удаление первой задачи из истории")
    void shouldRemoveFirstTaskFromHistoryTest() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        
        // Удаляем первую задачу
        historyManager.remove(1);
        
        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(2, history.get(0).getId(), "Первой должна стать задача 2");
        assertEquals(3, history.get(1).getId(), "Второй должна быть задача 3");
    }

    @Test
    @DisplayName("Удаление последней задачи из истории")
    void shouldRemoveLastTaskFromHistoryTest() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        
        // Удаляем последнюю задачу
        historyManager.remove(3);
        
        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(1, history.get(0).getId());
        assertEquals(2, history.get(1).getId());
    }
    

    @Test
    @DisplayName("Удаление средней задачи из истории")
    void shouldRemoveMiddleTaskFromHistoryTest() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        
        // Удаляем среднюю задачу
        historyManager.remove(2);
        
        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(1, history.get(0).getId());
        assertEquals(3, history.get(1).getId());
    }
    

    @Test
    @DisplayName("Возврат пустого списка когда история пуста")
    void shouldReturnEmptyListWhenHistoryIsEmptyTest() {
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не должна быть null");
        assertTrue(history.isEmpty(), "История должна быть пустой");
    }
    

    @Test
    @DisplayName("Удаление единственной задачи из истории")
    void removingOnlyTaskTest() {
        historyManager.add(task1);
        
        assertEquals(1, historyManager.getHistory().size());
        
        historyManager.remove(1);
        
        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не должна быть null");
        assertTrue(history.isEmpty(), "История должна быть пустой после удаления единственной задачи");
    }
    

}

