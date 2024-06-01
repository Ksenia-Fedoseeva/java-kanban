import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.enums.Status;

public class SubtaskTest {

    @Test
    void differentEpicsEqualOfIdTest() {
        Subtask subtask1 = new Subtask(1, "Test addNewSubtask1", "Test addNewSubtask1 description",
                Status.NEW, 1);
        Subtask subtask2 = new Subtask(1, "Test addNewSubtask2", "Test addNewSubtask2 description",
                Status.DONE, 1);
        Assertions.assertEquals(subtask1, subtask2, "Подзадачи не равны.");
    }

    @Test
    void sameTasksDifferentOfIdTest() {
        Subtask subtask1 = new Subtask(1, "Test addNewSubtask1", "Test addNewSubtask1 description",
                Status.NEW, 1);
        Subtask subtask2  = new Subtask(2, "Test addNewSubtask1", "Test addNewSubtask1 description",
                Status.NEW, 1);
        Assertions.assertNotEquals(subtask1, subtask2, "Подзадачи равны.");
    }

}
