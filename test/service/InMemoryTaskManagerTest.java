package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// касс наследуется от TaskManagerTest
public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    @Override
    public void setUp() {
        taskManager = new InMemoryTaskManager();
    }

    // статус Epic
    @DisplayName("проверка статуса Epic: все подзадачи NEW")
    @Test
    public void testEpicStatusAllNew() {
        Epic epic = new Epic(EPIC_NAME, DESCRIPTION);
        Epic createdEpic = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask(Status.NEW, SUBTASK_NAME + " 1", DESCRIPTION, createdEpic.getId());
        Subtask subtask2 = new Subtask(Status.NEW, SUBTASK_NAME + " 2", DESCRIPTION, createdEpic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic epicFromManager = taskManager.getEpicById(createdEpic.getId());
        assertEquals(Status.NEW, epicFromManager.getStatus());
    }

    @DisplayName("проверка статуса Epic: все подзадачи DONE")
    @Test
    public void testEpicStatusAllDone() {
        Epic epic = new Epic(EPIC_NAME, DESCRIPTION);
        Epic createdEpic = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask(Status.DONE, SUBTASK_NAME + " 1", DESCRIPTION, createdEpic.getId());
        Subtask subtask2 = new Subtask(Status.DONE, SUBTASK_NAME + " 2", DESCRIPTION, createdEpic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic epicFromManager = taskManager.getEpicById(createdEpic.getId());
        assertEquals(Status.DONE, epicFromManager.getStatus());
    }

    @DisplayName("проверка статуса Epic: подзадачи NEW и DONE")
    @Test
    public void testEpicStatusNewAndDone() {
        Epic epic = new Epic(EPIC_NAME, DESCRIPTION);
        Epic createdEpic = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask(Status.NEW, SUBTASK_NAME + " 1", DESCRIPTION, createdEpic.getId());
        Subtask subtask2 = new Subtask(Status.DONE, SUBTASK_NAME + " 2", DESCRIPTION, createdEpic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic epicFromManager = taskManager.getEpicById(createdEpic.getId());
        assertEquals(Status.IN_PROGRESS, epicFromManager.getStatus());
    }

    @DisplayName("проверка статуса Epic: подзадачи IN_PROGRESS")
    @Test
    public void testEpicStatusInProgress() {
        Epic epic = new Epic(EPIC_NAME, DESCRIPTION);
        Epic createdEpic = taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask(Status.IN_PROGRESS, SUBTASK_NAME + " 1", DESCRIPTION, createdEpic.getId());
        taskManager.createSubtask(subtask1);

        Epic epicFromManager = taskManager.getEpicById(createdEpic.getId());
        assertEquals(Status.IN_PROGRESS, epicFromManager.getStatus());
    }


    @DisplayName("Задачи сортируются по startTime")
    @Test
    public void testGetPrioritizedTasks() {
        LocalDateTime now = LocalDateTime.now();
        Task task1 = new Task(Status.NEW, "Task 1", DESCRIPTION);
        task1.setStartTime(now.plusHours(2));
        task1.setDuration(Duration.ofMinutes(30));

        Task task2 = new Task(Status.NEW, "Task 2", DESCRIPTION);
        task2.setStartTime(now.plusHours(1));
        task2.setDuration(Duration.ofMinutes(30));

        Task createdTask1 = taskManager.createTask(task1);
        Task createdTask2 = taskManager.createTask(task2);

        List<Task> prioritized = taskManager.getPrioritizedTasks();

        assertEquals(2, prioritized.size());
        assertEquals(createdTask2.getId(), prioritized.get(0).getId());
        assertEquals(createdTask1.getId(), prioritized.get(1).getId());
    }

    @DisplayName("Задачи без startTime не включаются")
    @Test
    public void testGetPrioritizedTasksExcludesNullStartTime() {
        Task task1 = new Task(Status.NEW, "Task 1", DESCRIPTION);
        task1.setStartTime(LocalDateTime.now());
        task1.setDuration(Duration.ofMinutes(30));

        Task task2 = new Task(Status.NEW, "Task 2", DESCRIPTION);

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        List<Task> prioritized = taskManager.getPrioritizedTasks();

        assertEquals(1, prioritized.size());
        assertEquals(task1.getName(), prioritized.get(0).getName());
    }


    @DisplayName("Проверки пересечения задач: задачи пересекаются")
    @Test
    public void testTasksOverlapping() {
        LocalDateTime now = LocalDateTime.now();
        Task task1 = new Task(Status.NEW, "Task 1", DESCRIPTION);
        task1.setStartTime(now);
        task1.setDuration(Duration.ofHours(2));

        Task task2 = new Task(Status.NEW, "Task 2", DESCRIPTION);
        task2.setStartTime(now.plusHours(1));
        task2.setDuration(Duration.ofHours(2));

        Task createdTask1 = taskManager.createTask(task1);
        assertNotNull(createdTask1);

        Task createdTask2 = taskManager.createTask(task2);
        assertNull(createdTask2, "Задачи с пересекающимся временем не должны создаваться");
    }

    @DisplayName("Проверки пересечения задач: задачи не пересекаются")
    @Test
    public void testTasksNotOverlapping() {
        LocalDateTime now = LocalDateTime.now();
        Task task1 = new Task(Status.NEW, "Task 1", DESCRIPTION);
        task1.setStartTime(now);
        task1.setDuration(Duration.ofHours(1));

        Task task2 = new Task(Status.NEW, "Task 2", DESCRIPTION);
        task2.setStartTime(now.plusHours(2));
        task2.setDuration(Duration.ofHours(1));

        Task createdTask1 = taskManager.createTask(task1);
        Task createdTask2 = taskManager.createTask(task2);

        assertNotNull(createdTask1);
        assertNotNull(createdTask2);
    }


    @DisplayName("Время расчет Epic")
    @Test
    public void testEpicTimeFieldsCalculation() {
        Epic epic = new Epic(EPIC_NAME, DESCRIPTION);
        Epic createdEpic = taskManager.createEpic(epic);

        LocalDateTime start1 = LocalDateTime.now();
        LocalDateTime start2 = start1.plusHours(1);

        Subtask subtask1 = new Subtask(Status.NEW, SUBTASK_NAME + " 1", DESCRIPTION, createdEpic.getId());
        subtask1.setStartTime(start2);
        subtask1.setDuration(Duration.ofMinutes(30));

        Subtask subtask2 = new Subtask(Status.NEW, SUBTASK_NAME + " 2", DESCRIPTION, createdEpic.getId());
        subtask2.setStartTime(start1);
        subtask2.setDuration(Duration.ofMinutes(60));

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic epicFromManager = taskManager.getEpicById(createdEpic.getId());
        assertNotNull(epicFromManager.getStartTime());
        assertEquals(start1, epicFromManager.getStartTime());
        assertEquals(Duration.ofMinutes(90), epicFromManager.getDuration());
        assertNotNull(epicFromManager.getEndTime());
    }


    @DisplayName("ТПолучения времени окончания для задачи")
    @Test
    public void testTaskGetEndTime() {
        LocalDateTime start = LocalDateTime.now();
        Duration duration = Duration.ofHours(2);
        Task task = new Task(Status.NEW, TASK_NAME, DESCRIPTION);
        task.setStartTime(start);
        task.setDuration(duration);

        LocalDateTime endTime = task.getEndTime();
        assertNotNull(endTime);
        assertEquals(start.plus(duration), endTime);
    }

    @DisplayName("Проверки пересечения интервалов: граничные значения")
    @Test
    public void testTasksOverlappingBoundaryCases() {
        LocalDateTime now = LocalDateTime.now();
        
        Task task1 = new Task(Status.NEW, "Task 1", DESCRIPTION);
        task1.setStartTime(now);
        task1.setDuration(Duration.ofHours(1));
        taskManager.createTask(task1);

        Task task2 = new Task(Status.NEW, "Task 2", DESCRIPTION);
        task2.setStartTime(now.plusHours(1));
        task2.setDuration(Duration.ofHours(1));
        Task createdTask2 = taskManager.createTask(task2);
        
        assertNotNull(createdTask2, "Задачи, не должны пересекаться");
    }


}