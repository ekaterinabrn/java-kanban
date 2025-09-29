package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SubtaskTest {

    @Test
    public void testSubtaskEqualityWithSameId() {
        Subtask subtask1 = new Subtask(Status.NEW, "Подзадача 1", "Описание 1", 1);
        Subtask subtask2 = new Subtask(Status.IN_PROGRESS, "Подзадача 2", "Описание 2", 2);
        
        subtask1.setId(5);
        subtask2.setId(5);
        
        assertEquals(subtask1, subtask2);
        assertEquals(subtask1.hashCode(), subtask2.hashCode());
    }

    @Test
    public void testSubtaskInitEpicId() {
        Subtask subtask = new Subtask(Status.NEW, "Подзадача", "Описание", 5);

        assertEquals(5, subtask.getEpicId());
    }

    @Test
    public void testSubtaskSetEpicId() {
        Subtask subtask = new Subtask(Status.NEW, "Подзадача", "Описание", 5);

        subtask.setEpicId(10);
        assertEquals(10, subtask.getEpicId());
    }

    // Subtask нельзя сделать своим же эпиком
     @Test
     public void testSubtaskCantBeOwnEpic() {
         Subtask subtask = new Subtask(Status.NEW, "Подзадача", "Описание", 5);
         subtask.setId(10);
         subtask.setEpicId(10);
         assertNotEquals(subtask.getId(), subtask.getEpicId(), "Subtask нельзя сделать своим же эпиком");
         assertEquals(5, subtask.getEpicId(), "epicId должен остаться  5");
         assertEquals(10, subtask.getId());
     }
}