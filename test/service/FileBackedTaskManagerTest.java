package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    private File tempFile;

    @BeforeEach
    public void setUp() throws IOException {
        tempFile = File.createTempFile("testCat", ".csv");
        tempFile.deleteOnExit();
    }

    @Test
    @DisplayName("Сохранение и загрузка пустого файла")
    public void saveAndLoadEmptyFileTest() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        
        // ИЗМЕНЕНО: конструктор больше не пишет заголовок сам — проверяем только загрузку
        
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        
        assertEquals(0, loadedManager.getAllTask().size());
        assertEquals(0, loadedManager.getAllEpics().size());
        assertEquals(0, loadedManager.getAllSubtask().size());
    }

    @Test
    @DisplayName("Сохранение нескольких задач")
    public void saveMultipleTasksTest() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        
        Task task1 = new Task(Status.NEW, "Task1", "Description task1");
        Task task2 = new Task(Status.IN_PROGRESS, "Task2", "Description task2");
        manager.createTask(task1);
        manager.createTask(task2);
        
        Epic epic1 = new Epic("Epic1", "Description epic1");
        Epic createdEpic = manager.createEpic(epic1);
        
        Subtask subtask1 = new Subtask(Status.DONE, "Subtask1", "Description subtask1", createdEpic.getId());
        manager.createSubtask(subtask1);
        
        String content = Files.readString(tempFile.toPath());
        
        assertTrue(content.contains("Task1"));
        assertTrue(content.contains("Task2"));
        assertTrue(content.contains("Epic1"));
        assertTrue(content.contains("Subtask1"));
        assertTrue(content.contains("Description task1"));
        assertTrue(content.contains("Description epic1"));
    }

    @Test
    @DisplayName("Загрузка нескольких задач")
    public void loadMultipleTasksTest() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        
        Task task1 = new Task(Status.NEW, "Task1", "Description task1");
        Task createdTask1 = manager.createTask(task1);
        int taskId1 = createdTask1.getId();
        
        Task task2 = new Task(Status.NEW, "Task2", "Description task2");
        Task createdTask2 = manager.createTask(task2);
        int taskId2 = createdTask2.getId();
        Task updatedTask2 = new Task(Status.DONE, "Task2", "Description task2");
        updatedTask2.setId(taskId2);
        manager.updateTask(updatedTask2);
        
        Epic epic1 = new Epic("Epic1", "Description epic1");
        Epic createdEpic = manager.createEpic(epic1);
        int epicId = createdEpic.getId();
        
        Subtask subtask1 = new Subtask(Status.IN_PROGRESS, "Subtask1", "Description subtask1", epicId);
        Subtask createdSubtask = manager.createSubtask(subtask1);
        int subtaskId = createdSubtask.getId();
        
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        
        assertEquals(2, loadedManager.getAllTask().size());
        assertEquals(1, loadedManager.getAllEpics().size());
        assertEquals(1, loadedManager.getAllSubtask().size());
        
        Task loadedTask1 = loadedManager.getTaskById(taskId1);
        assertNotNull(loadedTask1);
        assertEquals("Task1", loadedTask1.getName());
        assertEquals(Status.NEW, loadedTask1.getStatus());
        assertEquals("Description task1", loadedTask1.getDescription());
        
        Task loadedTask2 = loadedManager.getTaskById(taskId2);
        assertNotNull(loadedTask2);
        assertEquals("Task2", loadedTask2.getName());
        assertEquals(Status.DONE, loadedTask2.getStatus());
        
        Epic loadedEpic = loadedManager.getEpicById(epicId);
        assertNotNull(loadedEpic);
        assertEquals("Epic1", loadedEpic.getName());
        
        Subtask loadedSubtask = loadedManager.getSubtaskById(subtaskId);
        assertNotNull(loadedSubtask);
        assertEquals("Subtask1", loadedSubtask.getName());
        assertEquals(epicId, loadedSubtask.getEpicId());
        assertEquals(Status.IN_PROGRESS, loadedSubtask.getStatus());
        
        List<Subtask> epicSubtasks = loadedManager.getEpicSubtask(epicId);
        assertEquals(1, epicSubtasks.size());
        assertEquals(subtaskId, epicSubtasks.get(0).getId());
    }

    @Test
    @DisplayName("Сохранение после обновления задачи")
    public void saveAfterUpdateTest() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        
        Task task = new Task(Status.NEW, "Task1", "Description task1");
        Task createdTask = manager.createTask(task);
        int taskId = createdTask.getId();
        
        Task updatedTask = new Task(Status.DONE, "Updated Task1", "Updated description");
        updatedTask.setId(taskId);
        manager.updateTask(updatedTask);
        
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        Task loadedTask = loadedManager.getTaskById(taskId);
        
        assertNotNull(loadedTask);
        assertEquals("Updated Task1", loadedTask.getName());
        assertEquals(Status.DONE, loadedTask.getStatus());
        assertEquals("Updated description", loadedTask.getDescription());
    }

    @Test
    @DisplayName("Сохранение после удаления задачи")
    public void saveAfterDeleteTest() throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(tempFile);
        
        Task task1 = new Task(Status.NEW, "Task1", "Description task1");
        Task createdTask1 = manager.createTask(task1);
        int taskId1 = createdTask1.getId();
        
        Task task2 = new Task(Status.NEW, "Task2", "Description task2");
        Task createdTask2 = manager.createTask(task2);
        int taskId2 = createdTask2.getId();
        
        manager.deleteTaskById(taskId1);
        
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(tempFile);
        
        assertEquals(1, loadedManager.getAllTask().size());
        assertNull(loadedManager.getTaskById(taskId1));
        assertNotNull(loadedManager.getTaskById(taskId2));
    }
}

