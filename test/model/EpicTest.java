package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {
    private static final String EPIC_NAME = "Эпик";
    private static final String EPIC_DESCRIPTION = "Описание";


    @Test
    @DisplayName("Эпики с одинаковым ID равны")
    public void testEpicEqualityWithSameId() {
        Epic epic1 = new Epic("Эпик 1", "Описание 1");
        Epic epic2 = new Epic("Эпик 2", "Описание 2");

        epic1.setId(1);
        epic2.setId(1);

        assertEquals(epic1, epic2);
        assertEquals(epic1.hashCode(), epic2.hashCode());
    }

    @Test
    @DisplayName("Подзадачи успешно добавляются в эпик")
        public void subtasksAddedSuccessfullyTest() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESCRIPTION);
        epic.setId(1);

        epic.addSubtaskId(10);
        epic.addSubtaskId(20);

        assertEquals(2, epic.getSubtaskIds().size());
        assertTrue(epic.getSubtaskIds().contains(10));
        assertTrue(epic.getSubtaskIds().contains(20));
    }

    @Test
    @DisplayName("Подзадача успешно удаляется из эпика")
    public void subtaskRemovedFromEpicSuccessfullyTest() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESCRIPTION);
        epic.setId(1);
        epic.addSubtaskId(10);
        epic.addSubtaskId(20);

        epic.removeSubtaskId(20);

        assertEquals(1, epic.getSubtaskIds().size());
        assertTrue(epic.getSubtaskIds().contains(10));
        assertFalse(epic.getSubtaskIds().contains(20));
    }

    @Test
    @DisplayName("Удаление всех подзадач очищает список")
    public void deleteAllSubtasksFromEpicSubtasksClearedTest() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESCRIPTION);
        epic.setId(1);
        epic.addSubtaskId(10);
        epic.addSubtaskId(20);

        epic.deleteAllSubtasks();

        assertTrue(epic.getSubtaskIds().isEmpty());
    }

    @Test
    @DisplayName("Статус эпика устанавливается в NEW")
    public void epicStatusSetToNewTest() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESCRIPTION);
        assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    @DisplayName("Эпик не может добавить себя как подзадачу")
    public void testEpicCannotAddItselfAsSubtask() {
        Epic epic = new Epic(EPIC_NAME, EPIC_DESCRIPTION);
        epic.setId(1);
        epic.addSubtaskId(1);
        assertFalse(epic.getSubtaskIds().contains(1), "Объект Epic нельзя добавить в самого себя в виде подзадачи");
    }
}