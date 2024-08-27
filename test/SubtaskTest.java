import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Subtask;
import tasks.enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubtaskTest {

    @Test
    void differentEpicsEqualOfIdTest() {
        Subtask subtask1 = new Subtask(1, "Test addNewSubtask1", "Test addNewSubtask1 description",
                Status.NEW, 1, Duration.ofMinutes(60), LocalDateTime.of(2024, 8, 25,
                10, 0));
        Subtask subtask2 = new Subtask(1, "Test addNewSubtask2", "Test addNewSubtask2 description",
                Status.DONE, 1, Duration.ofMinutes(90), LocalDateTime.of(2024, 8, 25,
                11, 0));
        Assertions.assertEquals(subtask1, subtask2, "Подзадачи не равны.");
    }

    @Test
    void sameTasksDifferentOfIdTest() {
        Subtask subtask1 = new Subtask(1, "Test addNewSubtask1", "Test addNewSubtask1 description",
                Status.NEW, 1, Duration.ofMinutes(60), LocalDateTime.of(2024, 8, 25,
                10, 0));
        Subtask subtask2 = new Subtask(2, "Test addNewSubtask1", "Test addNewSubtask1 description",
                Status.NEW, 1, Duration.ofMinutes(60), LocalDateTime.of(2024, 8, 25,
                10, 0));
        Assertions.assertNotEquals(subtask1, subtask2, "Подзадачи равны.");
    }

}