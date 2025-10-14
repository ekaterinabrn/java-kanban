package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class TaskManagerHistoryIntegrationTest {
    
    private TaskManager taskManager;
    
    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
    }

    @Test
    @DisplayName("Удаление задачи из менеджера удаляет её из истории")
    void shouldRemoveTaskFromHistoryWhenDeletedTest() {
        // Создаём задачи
        Task task1 = taskManager.createTask(new Task(Status.NEW, "Task 1", "Description 1"));
        Task task2 = taskManager.createTask(new Task(Status.NEW, "Task 2", "Description 2"));
        Task task3 = taskManager.createTask(new Task(Status.NEW, "Task 3", "Description 3"));
        
        // Просматриваем задачи
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());
        
        //  все задачи в истории
        assertEquals(3, taskManager.getHistory().size(), "В истории должно быть 3 задачи");
        
        // Удаляем задачу 2
        taskManager.deleteTaskById(task2.getId());
        
        // Проверяем, что задача 2 удалена из истории
        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size(), "В истории должно остаться 2 задачи");
        assertEquals(task1.getId(), history.get(0).getId(), "Задача 1 должна остаться в истории");
        assertEquals(task3.getId(), history.get(1).getId(), "Задача 3 должна остаться в истории");
    }
    

    @Test
    @DisplayName("Удаление всех задач очищает историю")
    void shouldRemoveAllTasksFromHistoryWhenDeletedAll() {
        // Создаём несколько задач
        Task task1 = taskManager.createTask(new Task(Status.NEW, "Task 1", "Description 1"));
        Task task2 = taskManager.createTask(new Task(Status.NEW, "Task 2", "Description 2"));
        
        // Просматриваем задачи
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        
        // Удаляем все задачи
        taskManager.deleteAllTask();
        
        // Проверяем, что задачи удалены из истории
        List<Task> history = taskManager.getHistory();
        assertTrue(history.isEmpty(), "История должна быть пустой после удаления всех задач");
    }
    

    @Test
    @DisplayName("Удаление эпика удаляет его и все подзадачи из истории")
    void shouldRemoveEpicAndSubtasksFromHistoryWhenEpicDeleted() {
        // Создаём эпик
        Epic epic = taskManager.createEpic(new Epic("Epic 1", "Epic Description"));
        
        // Создаём подзадачи
        Subtask subtask1 = taskManager.createSubtask(
            new Subtask(Status.NEW, "Subtask 1", "Description 1", epic.getId())
        );
        Subtask subtask2 = taskManager.createSubtask(
            new Subtask(Status.NEW, "Subtask 2", "Description 2", epic.getId())
        );
        
        // Просматриваем эпик и подзадачи
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getSubtaskById(subtask2.getId());
        
        // Проверяем, что все в истории
        assertEquals(3, taskManager.getHistory().size(), "В истории должно быть 3 элемента");
        
        // Удаляем эпик
        taskManager.deleteEpicById(epic.getId());
        
        // Проверяем, что эпик и все подзадачи удалены из истории
        List<Task> history = taskManager.getHistory();
        assertTrue(history.isEmpty(), 
            "История должна быть пустой после удаления эпика (эпик + все подзадачи удалены)");
    }
    

    @Test
    @DisplayName("Удаление подзадачи удаляет её из истории")
    void shouldRemoveSubtaskFromHistoryWhenDeletedTest() {
        // Создаём эпик и подзадачи
        Epic epic = taskManager.createEpic(new Epic("Epic 1", "Epic Description"));
        Subtask subtask1 = taskManager.createSubtask(
            new Subtask(Status.NEW, "Subtask 1", "Description 1", epic.getId())
        );
        Subtask subtask2 = taskManager.createSubtask(
            new Subtask(Status.NEW, "Subtask 2", "Description 2", epic.getId())
        );
        
        // Просматриваем эпик и подзадачи
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getSubtaskById(subtask2.getId());
        
        assertEquals(3, taskManager.getHistory().size());
        
        // Удаляем подзадачу 1
        taskManager.deleteSubtaskById(subtask1.getId());
        
        // Проверяем историю
        List<Task> history = taskManager.getHistory();
        assertEquals(2, history.size(), "В истории должно остаться 2 элемента");
        assertEquals(epic.getId(), history.get(0).getId(), "Эпик должен остаться в истории");
        assertEquals(subtask2.getId(), history.get(1).getId(), "Подзадача 2 должна остаться в истории");
    }
    

    @Test
    @DisplayName("Удаление всех подзадач очищает их из истории")
    void shouldRemoveAllSubtasksFromHistoryWhenDeletedAll() {
        // Создаём эпик и подзадачи
        Epic epic = taskManager.createEpic(new Epic("Epic 1", "Epic Description"));
        Subtask subtask1 = taskManager.createSubtask(
            new Subtask(Status.NEW, "Subtask 1", "Description 1", epic.getId())
        );
        Subtask subtask2 = taskManager.createSubtask(
            new Subtask(Status.NEW, "Subtask 2", "Description 2", epic.getId())
        );
        
        // Просматриваем все
        taskManager.getEpicById(epic.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getSubtaskById(subtask2.getId());
        
        // Удаляем все подзадачи
        taskManager.deleteAllSubtask();
        
        // Проверяем историю - должен остаться только эпик
        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size(), "В истории должен остаться только эпик");
        assertEquals(epic.getId(), history.get(0).getId(), "В истории должен быть эпик");
    }

    @Test
    @DisplayName("Удаление всех эпиков очищает историю")
    void shouldRemoveAllEpicsAndSubtasksFromHistoryWhenDeletedAll() {
        // Создаём несколько эпиков с подзадачами
        Epic epic1 = taskManager.createEpic(new Epic("Epic 1", "Description 1"));
        Subtask subtask1 = taskManager.createSubtask(
            new Subtask(Status.NEW, "Subtask 1", "Description", epic1.getId())
        );
        
        Epic epic2 = taskManager.createEpic(new Epic("Epic 2", "Description 2"));
        Subtask subtask2 = taskManager.createSubtask(
            new Subtask(Status.NEW, "Subtask 2", "Description", epic2.getId())
        );
        
        // Просматриваем все
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getSubtaskById(subtask2.getId());
        
        assertEquals(4, taskManager.getHistory().size());
        
        // Удаляем все эпики
        taskManager.deleteAllEpics();
        
        // Проверяем, что история пустая
        assertTrue(taskManager.getHistory().isEmpty(), 
            "История должна быть пустой");
    }

    @Test
    @DisplayName("С помощью сеттеров экземпляры задач позволяют изменить любое своё поле, но это не может повлиять на данные внутри менеджера.")
    void shouldNotAllowModifyingTaskThroughSetters() {
        // Создаём задачу
        Task task = taskManager.createTask(new Task(Status.NEW, "Original Name", "Original Description"));
        int taskId = task.getId();
        
        // Получаем задачу из менеджера
        Task retrievedTask = taskManager.getTaskById(taskId);
        
        // Изменяем полученную задачу через сеттеры
        retrievedTask.setName("Modified Name");
        retrievedTask.setDescription("Modified Description");
        retrievedTask.setStatus(Status.DONE);
        
        // Получаем задачу снова
        Task taskAfterModification = taskManager.getTaskById(taskId);
        
        // Проверяем, что внутренние данные не изменились
        assertEquals("Original Name", taskAfterModification.getName(), 
            "Имя задачи в менеджере не должно измениться");
        assertEquals("Original Description", taskAfterModification.getDescription(), 
            "Описание задачи в менеджере не должно измениться");
        assertEquals(Status.NEW, taskAfterModification.getStatus(), 
            "Статус задачи в менеджере не должен измениться");
    }
    

    @Test
    @DisplayName("Удаление задачи, которую просматривали несколько раз")
    void shouldRemoveTaskFromHistoryEvenIfViewedMultipleTimes() {
        Task task = taskManager.createTask(new Task(Status.NEW, "Task", "Description"));
        
        // Просматриваем задачу несколько раз
        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task.getId());
        
        // В истории должна быть только одна запись (дубликаты удалены)
        assertEquals(1, taskManager.getHistory().size());
        
        // Удаляем задачу
        taskManager.deleteTaskById(task.getId());
        
        // История должна быть пустой
        assertTrue(taskManager.getHistory().isEmpty(), 
            "История должна быть пустой после удаления задачи");
    }
    

}

