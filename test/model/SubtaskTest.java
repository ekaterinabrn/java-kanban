package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SubtaskTest {
    private static final String SUBTASK_NAME = "Подзадача";
    private static final String SUBTASK_DESCRIPTION = "Описание";
    private static final int EPIC_ID_INITIAL = 5;
    private static final int ID_TEN = 10;


    @Test
    @DisplayName("Подзадачи с одинаковым ID равны")
    public void testSubtaskEqualityWithSameId() {
        Subtask subtask1 = new Subtask(Status.NEW, "Подзадача 1", SUBTASK_DESCRIPTION, 1);
        Subtask subtask2 = new Subtask(Status.IN_PROGRESS, "Подзадача 2", SUBTASK_DESCRIPTION, 2);

        subtask1.setId(5);
        subtask2.setId(5);

        assertEquals(subtask1, subtask2);
        assertEquals(subtask1.hashCode(), subtask2.hashCode());
    }

    @Test
    @DisplayName("Подзадача инициализируется с ID эпика")
    public void testSubtaskInitEpicId() {
        Subtask subtask = new Subtask(Status.NEW, "Подзадача", "Описание", 5);

        assertEquals(5, subtask.getEpicId());
    }

    @Test
    @DisplayName("ID эпика подзадачи можно изменить")
    public void testSubtaskSetEpicId() {
        Subtask subtask = new Subtask(Status.NEW, "Подзадача", "Описание", 5);

        subtask.setEpicId(ID_TEN);
        assertEquals(ID_TEN, subtask.getEpicId());
    }

    @Test
    @DisplayName("Подзадача не может быть своим эпиком")
    public void testSubtaskCantBeOwnEpic() {
        Subtask subtask = new Subtask(Status.NEW, SUBTASK_NAME, SUBTASK_DESCRIPTION, EPIC_ID_INITIAL);
        subtask.setId(ID_TEN);
        subtask.setEpicId(ID_TEN);
        assertNotEquals(subtask.getId(), subtask.getEpicId(), "Subtask нельзя сделать своим же эпиком");
        assertEquals(EPIC_ID_INITIAL, subtask.getEpicId(), "epicId должен остаться  5");
        assertEquals(ID_TEN, subtask.getId());
    }
}