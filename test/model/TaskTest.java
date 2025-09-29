package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testTaskEqualityWithSameId() {
        Task task1 = new Task(Status.NEW, "Задача 1", "Описание 1");
        Task task2 = new Task(Status.IN_PROGRESS, "Задача 2", "Описание 2");

        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2);
        assertEquals(task1.hashCode(), task2.hashCode());
    }
//растащить на тесты проверки каждого поля
    @Test
    public void testTaskSettersAndGetters() {
        Task task = new Task(Status.NEW, "Имя", "Описание");
        task.setId(10);

        assertEquals(10, task.getId());
        assertEquals(Status.NEW, task.getStatus());
        assertEquals("Имя", task.getName());
        assertEquals("Описание", task.getDescription());
    }

    // Тест для equals - позитив
    @Test
    public void testTaskEqualsWithSameId() {
        Task task1 = new Task(Status.NEW, "Задача 1", "Описание 1");
        Task task2 = new Task(Status.IN_PROGRESS, "Задача 2", "Описание 2");

        task1.setId(1);
        task2.setId(1);

        assertEquals(task1, task2);
    }

    // Тест для equals негатив
    @Test
    public void testTaskNotEqualsWithDifferentId() {
        Task task1 = new Task(Status.NEW, "Задача 1", "Описание 1");
        Task task2 = new Task(Status.IN_PROGRESS, "Задача 2", "Описание 2");

        task1.setId(1);
        task2.setId(2);

        assertNotEquals(task1, task2);
    }


    // Тест копирующего конструктора - проверка совпадения айди
    @Test
    public void testTaskCopyConstructorId() {
        Task originalTask = new Task(Status.IN_PROGRESS, "Оригинал", "Описание");
        originalTask.setId(5);

        Task copiedTask = new Task(originalTask);

        assertEquals(originalTask.getId(), copiedTask.getId());
    }

    // Тест копирующего конструктора - проверка совпадения статуса
    @Test
    public void testTaskCopyConstructorStatus() {
        Task originalTask = new Task(Status.IN_PROGRESS, "Оригинал", "Описание");
        originalTask.setId(5);

        Task copiedTask = new Task(originalTask);

        assertEquals(originalTask.getStatus(), copiedTask.getStatus());
    }

    // Тест  копирующего конструктора - проверка совпадения имени
    @Test
    public void testTaskCopyConstructorName() {
        Task originalTask = new Task(Status.IN_PROGRESS, "Оригинал", "Описание");
        originalTask.setId(5);

        Task copiedTask = new Task(originalTask);

        assertEquals(originalTask.getName(), copiedTask.getName());
    }

    // Тест  копирующего конструктора - проверка совпадения описания
    @Test
    public void testTaskCopyConstructorDescription() {
        Task originalTask = new Task(Status.IN_PROGRESS, "Оригинал", "Описание");
        originalTask.setId(5);

        Task copiedTask = new Task(originalTask);

        assertEquals(originalTask.getDescription(), copiedTask.getDescription());
    }

    // Тест для копирующего конструктора
    @Test
    public void testTaskCopyConstructor() {
        Task taskask = new Task(Status.IN_PROGRESS, "Original", "Описание");
        taskask.setId(5);

        Task copiedTask = new Task(taskask);

        assertNotSame(taskask, copiedTask);

        // Изменение оригинала не влияет на копию
        taskask.setName("Изменено");
        assertNotEquals("Изменено", copiedTask.getName());
    }

}