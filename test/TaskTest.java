import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class TaskTest {

    @Test
    void differentTasksEqualOfIdTest() {
        Task task1 = new Task(1, "Test addNewTask1", "Test addNewTask1 description",
                Status.NEW, Duration.ofMinutes(60), LocalDateTime.of(2024, 8, 25, 10,
                0));
        Task task2 = new Task(1, "Test addNewTask2", "Test addNewTask2 description",
                Status.DONE, Duration.ofMinutes(90), LocalDateTime.of(2024, 8, 25, 11,
                0));
        Assertions.assertEquals(task1, task2, "Задачи не равны.");
    }

    @Test
    void sameTasksDifferentOfIdTest() {
        Task task1 = new Task(1, "Test addNewTask1", "Test addNewTask1 description",
                Status.NEW, Duration.ofMinutes(60), LocalDateTime.of(2024, 8, 25, 10,
                0));
        Task task2 = new Task(2, "Test addNewTask1", "Test addNewTask1 description",
                Status.NEW, Duration.ofMinutes(60), LocalDateTime.of(2024, 8, 25, 10,
                0));
        Assertions.assertNotEquals(task1, task2, "Задачи равны.");
    }

}