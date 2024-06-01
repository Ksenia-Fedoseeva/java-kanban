import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.enums.Status;

public class TaskTest {

    @Test
    void differentTasksEqualOfIdTest() {
        Task task1 = new Task(1, "Test addNewTask1", "Test addNewTask1 description", Status.NEW);
        Task task2 = new Task(1, "Test addNewTask2", "Test addNewTask2 description", Status.DONE);
        Assertions.assertEquals(task1, task2, "Задачи не равны.");
    }

    @Test
    void sameTasksDifferentOfIdTest() {
        Task task1 = new Task(1, "Test addNewTask1", "Test addNewTask1 description", Status.NEW);
        Task task2 = new Task(2, "Test addNewTask1", "Test addNewTask1 description", Status.NEW);
        Assertions.assertNotEquals(task1, task2, "Задачи равны.");
    }

}
