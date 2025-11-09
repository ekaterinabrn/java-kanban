package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;
    private static final String TASK_NAME = "Задача";
    private static final String DESCRIPTION = "Описание";
    private static final String EPIC_NAME = "Эпик";
    private static final String SUBTASK_NAME = "Подзадача";
    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    @DisplayName("Возможность добавления разных типов задач - при добавлении увеличивает их количество в списке")
    public void addDifferentTaskTypesTest() {
        Task task = new Task(Status.NEW, TASK_NAME, DESCRIPTION);
        Task createdTask = taskManager.createTask(task);
        
        Epic epic = new Epic(EPIC_NAME, DESCRIPTION);
        Epic createdEpic = taskManager.createEpic(epic);
        
        Subtask subtask = new Subtask(Status.NEW, SUBTASK_NAME, DESCRIPTION, createdEpic.getId());
        Subtask createdSubtask = taskManager.createSubtask(subtask);
        
        assertNotNull(createdTask);
        assertNotNull(createdEpic);
        assertNotNull(createdSubtask);
        
        assertEquals(1, taskManager.getAllTask().size());
        assertEquals(1, taskManager.getAllEpics().size());
        assertEquals(1, taskManager.getAllSubtask().size());
    }
    @Test
    @DisplayName("Поиск задачи по ID возвращает искомую задачу")
    public void getTaskByIdReturnsCorrectTaskTest() {
        Task task = new Task(Status.NEW, TASK_NAME, DESCRIPTION);
        Task createdTask = taskManager.createTask(task);
        int taskId = createdTask.getId();
        
        Task foundTask = taskManager.getTaskById(taskId);
        
        assertNotNull(foundTask);
        assertEquals(taskId, foundTask.getId());
        assertEquals(TASK_NAME, foundTask.getName());
    }
    @Test
    @DisplayName("Тест неизменности задачи, сохранённой менеджером")
    public void taskInManagerIsImmutableTest() {
        Task originalTask = new Task(Status.NEW, "Оригинал", DESCRIPTION);
        originalTask.setId(999);
        
        Task createdTask = taskManager.createTask(originalTask);
        
        originalTask.setName("Изменено");
        originalTask.setDescription("Новое описание");
        originalTask.setStatus(Status.DONE);
        
        Task taskFromManager = taskManager.getTaskById(createdTask.getId());
        
        assertEquals("Оригинал", taskFromManager.getName());
        assertEquals(DESCRIPTION, taskFromManager.getDescription());
        assertEquals(Status.NEW, taskFromManager.getStatus());
    }



    @Test
    @DisplayName("Задачи с заданным ID и сгенерированным ID не конфликтуют внутри менеджера")
     public void tasks_NoIdConflictsTest() {
         Task task1 = new Task(Status.NEW, "Задача 1", DESCRIPTION);
         Task createdTask1 = taskManager.createTask(task1);
         int generatedId1 = createdTask1.getId();
         Task task2 = new Task(Status.NEW, "Задача 2", DESCRIPTION);
         Task createdTask2 = taskManager.createTask(task2);
         int generatedId2 = createdTask2.getId();
         assertNotEquals(generatedId1, generatedId2, "Сгенерированные id не должны конфликтовать");
         assertNotNull(taskManager.getTaskById(generatedId1), "Задача 1 не найдена");
         assertNotNull(taskManager.getTaskById(generatedId2), "Задача 2 не найдена ");
     }

    //  Тест создания задачи
     @Test
     @DisplayName("Добавление новой задачи")
     void addNewTaskTest() {
         Task task = new Task(Status.NEW, "Test addNewTask", "Test addNewTask description");
         final Task createdTask = taskManager.createTask(task);
         final int taskId = createdTask.getId();

         final Task savedTask = taskManager.getTaskById(taskId);

         assertNotNull(savedTask, "Задача не найдена.");
         assertEquals(createdTask, savedTask, "Задачи не совпадают.");

         final List<Task> tasks = taskManager.getAllTask();

         assertNotNull(tasks, "Задачи не возвращаются.");
         assertEquals(1, tasks.size(), "Неверное количество задач.");
         assertEquals(createdTask.getName(), tasks.get(0).getName(), "Задачи не совпадают.");
     }
}