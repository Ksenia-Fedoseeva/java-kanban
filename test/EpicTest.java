import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class EpicTest {

    @Test
    void differentEpicsEqualOfIdTest() {
        LocalDateTime startTime = LocalDateTime.of(2024, 8, 25, 10, 0);
        Duration duration = Duration.ofMinutes(60);

        Epic epic1 = new Epic(1, "Test addNewEpic1", "Test addNewEpic1 description", Status.NEW,
                duration, startTime);
        Epic epic2 = new Epic(1, "Test addNewEpic2", "Test addNewEpic2 description", Status.DONE,
                duration, startTime);

        Assertions.assertEquals(epic1, epic2, "Эпики с одинаковым id не равны.");
    }

    @Test
    void sameTasksDifferentOfIdTest() {
        // Задаем стартовое время и продолжительность для эпиков
        LocalDateTime startTime = LocalDateTime.of(2024, 8, 25, 10, 0);
        Duration duration = Duration.ofMinutes(60);

        Epic epic1 = new Epic(1, "Test addNewEpic1", "Test addNewEpic1 description", Status.NEW,
                duration, startTime);
        Epic epic2 = new Epic(2, "Test addNewEpic1", "Test addNewEpic1 description", Status.NEW,
                duration, startTime);

        Assertions.assertNotEquals(epic1, epic2, "Эпики с разными id равны.");
    }

}