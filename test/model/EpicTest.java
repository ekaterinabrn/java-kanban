package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {


    @DisplayName("")
    @Test
    public void testEpicEqualityWithSameId() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик 2", "Описание 2");

        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2);
        assertEquals(epic1.hashCode(), epic2.hashCode());
    }

    @DisplayName("")
    @Test
    public void testAddSubtasks() {
        Epic epic = new Epic("эпик один", "Описание");
        epic.setId(1);

        epic.addSubtaskId(10);
        epic.addSubtaskId(20);

        assertEquals(2, epic.getSubtaskIds().size());
        assertTrue(epic.getSubtaskIds().contains(10));
        assertTrue(epic.getSubtaskIds().contains(20));
    }

    @DisplayName("")
    @Test
    public void testRemoveSubtask() {
        Epic epic = new Epic("эпик один", "Описание");
        epic.setId(1);
        epic.addSubtaskId(10);
        epic.addSubtaskId(20);

        epic.removeSubtaskId(20);

        assertEquals(1, epic.getSubtaskIds().size());
        assertTrue(epic.getSubtaskIds().contains(10));
        assertFalse(epic.getSubtaskIds().contains(20));
    }

    @DisplayName("")
    @Test
    public void testDeleteAllSubtasks() {
        Epic epic = new Epic("эпик один", "Описание");
        epic.setId(1);
        epic.addSubtaskId(10);
        epic.addSubtaskId(20);

        epic.deleteAllSubtasks();

        assertTrue(epic.getSubtaskIds().isEmpty());
    }

    @DisplayName("")
    @Test
    public void testEpicInitialStatus() {
        Epic epic = new Epic("Эпик", "Описание эпика");
        assertEquals(Status.NEW, epic.getStatus());
    }

    @DisplayName("Epic нельзя добавить в самого себя в виде подзадачи")

    @Test
    public void testEpicCannotAddItselfAsSubtask() {
        Epic epic = new Epic("Эпик", "Описание");
        epic.setId(1);
        epic.addSubtaskId(1);
        assertFalse(epic.getSubtaskIds().contains(1), "Объект Epic нельзя добавить в самого себя в виде подзадачи");
    }
}