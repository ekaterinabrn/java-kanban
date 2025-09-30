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


    @DisplayName("проверка равенста сабтаск с одним  айди но раззными др полями")
    @Test
    public void testSubtaskEqualityWithSameId() {
        Subtask subtask1 = new Subtask(Status.NEW, "Подзадача 1", SUBTASK_DESCRIPTION, 1);
        Subtask subtask2 = new Subtask(Status.IN_PROGRESS, "Подзадача 2", SUBTASK_DESCRIPTION, 2);

        subtask1.setId(5);
        subtask2.setId(5);

        assertEquals(subtask1, subtask2);
        assertEquals(subtask1.hashCode(), subtask2.hashCode());
    }

    @DisplayName("тест попадания подзадачи в нужный эпик")
    @Test
    public void testSubtaskInitEpicId() {
        Subtask subtask = new Subtask(Status.NEW, "Подзадача", "Описание", 5);

        assertEquals(5, subtask.getEpicId());
    }

    @DisplayName("Установка epicId через сеттер")
    @Test
    public void testSubtaskSetEpicId() {
        Subtask subtask = new Subtask(Status.NEW, "Подзадача", "Описание", 5);

        subtask.setEpicId(ID_TEN);
        assertEquals(ID_TEN, subtask.getEpicId());
    }

    @DisplayName("Subtask нельзя сделать своим же эпиком")
    // Subtask нельзя сделать своим же эпиком
    @Test
    public void testSubtaskCantBeOwnEpic() {
        Subtask subtask = new Subtask(Status.NEW, SUBTASK_NAME, SUBTASK_DESCRIPTION, EPIC_ID_INITIAL);
        subtask.setId(ID_TEN);
        subtask.setEpicId(ID_TEN);
        assertNotEquals(subtask.getId(), subtask.getEpicId(), "Subtask нельзя сделать своим же эпиком");
        assertEquals(EPIC_ID_INITIAL, subtask.getEpicId(), "epicId должен остаться  5");
        assertEquals(ID_TEN, subtask.getId());
    }
}