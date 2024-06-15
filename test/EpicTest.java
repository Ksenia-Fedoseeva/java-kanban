import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.enums.Status;

public class EpicTest {

    @Test
    void differentEpicsEqualOfIdTest() {
        Epic epic1 = new Epic(1, "Test addNewEpic1", "Test addNewEpic1 description", Status.NEW);
        Epic epic2 = new Epic(1, "Test addNewEpic2", "Test addNewEpic2 description", Status.DONE);
        Assertions.assertEquals(epic1, epic2, "Эпики не равны.");
    }

    @Test
    void sameTasksDifferentOfIdTest() {
        Epic epic1 = new Epic(1, "Test addNewEpic1", "Test addNewEpic1 description", Status.NEW);
        Epic epic2  = new Epic(2, "Test addNewEpic1", "Test addNewEpic1 description", Status.NEW);
        Assertions.assertNotEquals(epic1, epic2, "Эпики равны.");
    }

}
