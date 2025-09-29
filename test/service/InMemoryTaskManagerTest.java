package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    private InMemoryTaskManager taskManager;

    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }
    // надо разбивать
    @Test
    public void testTaskManagerAddsDifferentTaskTypes() {
        Task task = new Task(Status.NEW, "Задача", "Описание");
        Task createdTask = taskManager.createTask(task);
        
        Epic epic = new Epic("Эпик", "Описание");
        Epic createdEpic = taskManager.createEpic(epic);
        
        Subtask subtask = new Subtask(Status.NEW, "Подзадача", "Описание", createdEpic.getId());
        Subtask createdSubtask = taskManager.createSubtask(subtask);
        
        assertNotNull(createdTask);
        assertNotNull(createdEpic);
        assertNotNull(createdSubtask);
        
        assertEquals(1, taskManager.getAllTask().size());
        assertEquals(1, taskManager.getAllEpics().size());
        assertEquals(1, taskManager.getAllSubtask().size());
    }

    @Test
    public void testTaskManagerCanFindTasksById() {
        Task task = new Task(Status.NEW, "Задача", "Описание");
        Task createdTask = taskManager.createTask(task);
        int taskId = createdTask.getId();
        
        Task foundTask = taskManager.getTaskById(taskId);
        
        assertNotNull(foundTask);
        assertEquals(taskId, foundTask.getId());
        assertEquals("Задача", foundTask.getName());
    }

    @Test
    public void testTaskImmutabilityWhenAddedToManager() {
        Task originalTask = new Task(Status.NEW, "Оригинал", "Описание");
        originalTask.setId(999);
        
        Task createdTask = taskManager.createTask(originalTask);
        
        originalTask.setName("Изменено");
        originalTask.setDescription("Новое описание");
        originalTask.setStatus(Status.DONE);
        
        Task taskFromManager = taskManager.getTaskById(createdTask.getId());
        
        assertEquals("Оригинал", taskFromManager.getName());
        assertEquals("Описание", taskFromManager.getDescription());
        assertEquals(Status.NEW, taskFromManager.getStatus());
    }


    // проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
     @Test
     public void testTaskManagerHandlesGeneratedAndGivenIds() {
         Task task1 = new Task(Status.NEW, "Задача 1", "Описание");
         Task createdTask1 = taskManager.createTask(task1);
         int generatedId1 = createdTask1.getId();
         Task task2 = new Task(Status.NEW, "Задача 2", "Описание");
         Task createdTask2 = taskManager.createTask(task2);
         int generatedId2 = createdTask2.getId();
         assertNotEquals(generatedId1, generatedId2, "Сгенерированные id не должны конфликтовать");
         assertNotNull(taskManager.getTaskById(generatedId1), "Задача 1 не найдена");
         assertNotNull(taskManager.getTaskById(generatedId2), "Задача 2 не найдена ");
     }

    //  Тест создания задачи
     @Test
     void addNewTask() {
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