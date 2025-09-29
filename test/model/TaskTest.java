package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void testTaskCopyConstructor() {
        Task originalTask = new Task(Status.IN_PROGRESS, "Оригинал", "Описание");
        originalTask.setId(5);
        
        Task copiedTask = new Task(originalTask);
        
        assertEquals(originalTask.getId(), copiedTask.getId());
        assertEquals(originalTask.getStatus(), copiedTask.getStatus());
        assertEquals(originalTask.getName(), copiedTask.getName());
        assertEquals(originalTask.getDescription(), copiedTask.getDescription());
    }

    @Test
    public void testTaskSettersAndGetters() {
        Task task = new Task(Status.NEW, "Имя", "Описание");
        task.setId(10);
        
        assertEquals(10, task.getId());
        assertEquals(Status.NEW, task.getStatus());
        assertEquals("Имя", task.getName());
        assertEquals("Описание", task.getDescription());
    }
}