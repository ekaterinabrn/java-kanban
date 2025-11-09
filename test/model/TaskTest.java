package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    private static final String TASK_DESCRIPTION = "Описание ";
    private static final String TASK_NAME = "Имя";
    private static final String ORIGINAL_NAME = "Оригинал";
    private static final String CHANGED_NAME = "Изменено";


    @Test
    @DisplayName("Задачи с одинаковым ID равны")
    public void testTaskEqualityWithSameId() {
        Task task1 = new Task(Status.NEW, "Задача 1", "Описание 1");
        Task task2 = new Task(Status.IN_PROGRESS, "Задача 2", "Описание 2");

        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
    }


    @Test
    @DisplayName("Сеттеры и геттеры задачи работают корректно")
    public void taskSettersAndGettersTest() {
        Task task = new Task(Status.NEW, TASK_NAME, TASK_DESCRIPTION);
        task.setId(10);

        assertEquals(10, task.getId());
        assertEquals(Status.NEW, task.getStatus());
        assertEquals(TASK_NAME, task.getName());
        assertEquals(TASK_DESCRIPTION, task.getDescription());
    }


    @Test
    @DisplayName("Задачи равны при одинаковом ID")
    public void testTaskEqualsWithSameId() {
        Task task1 = new Task(Status.NEW, "Задача 1", "Описание 1");
        Task task2 = new Task(Status.IN_PROGRESS, "Задача 2", "Описание 2");

        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2);
    }


    @Test
    @DisplayName("Задачи не равны при разных ID")
    public void testTaskNotEqualsWithDifferentId() {
        Task task1 = new Task(Status.NEW, "Задача 1", "Описание 1");
        Task task2 = new Task(Status.IN_PROGRESS, "Задача 2", "Описание 2");

        task1.setId(1);
        task2.setId(2);

        assertNotEquals(task1, task2);
    }


    @Test
    @DisplayName("Конструктор копирования сохраняет ID")
    public void testTaskCopyConstructorId() {
        Task originalTask = new Task(Status.IN_PROGRESS, ORIGINAL_NAME, TASK_DESCRIPTION);
        originalTask.setId(5);

        Task copiedTask = new Task(originalTask);

        assertEquals(originalTask.getId(), copiedTask.getId());
    }

    @Test
    @DisplayName("Конструктор копирования сохраняет статус")
    public void testTaskCopyConstructorStatus() {
        Task originalTask = new Task(Status.IN_PROGRESS, ORIGINAL_NAME, TASK_DESCRIPTION);
        originalTask.setId(5);

        Task copiedTask = new Task(originalTask);

        assertEquals(originalTask.getStatus(), copiedTask.getStatus());
    }


    @Test
    @DisplayName("Конструктор копирования сохраняет имя")
    public void testTaskCopyConstructorName() {
        Task originalTask = new Task(Status.IN_PROGRESS, ORIGINAL_NAME, TASK_DESCRIPTION);
        originalTask.setId(5);

        Task copiedTask = new Task(originalTask);

        assertEquals(originalTask.getName(), copiedTask.getName());
    }


    @Test
    @DisplayName("Конструктор копирования сохраняет описание")
    public void testTaskCopyConstructorDescription() {
        Task originalTask = new Task(Status.IN_PROGRESS, ORIGINAL_NAME, TASK_DESCRIPTION);
        originalTask.setId(5);

        Task copiedTask = new Task(originalTask);

        assertEquals(originalTask.getDescription(), copiedTask.getDescription());
    }


    @Test
    @DisplayName("Конструктор копирования создаёт полную копию")
    public void testTaskCopyConstructor() {
        Task taskask = new Task(Status.IN_PROGRESS, ORIGINAL_NAME, TASK_DESCRIPTION);
        taskask.setId(5);

        Task copiedTask = new Task(taskask);

        assertNotSame(taskask, copiedTask);

        // Изменение оригинала не влияет на копию
        taskask.setName(CHANGED_NAME);
        assertNotEquals(CHANGED_NAME, copiedTask.getName());
    }

}