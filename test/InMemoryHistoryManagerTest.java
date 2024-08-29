import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class InMemoryHistoryManagerTest {
    private HistoryManager historyManager;
    private Task task1;

    @BeforeEach
    void init() {
        historyManager = new InMemoryHistoryManager();

        task1 = new Task(1, "Task 1", "Description 1", Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.now());
        historyManager.add(task1);
    }

    @Test
    void addTaskTest() {
        Task task2 = new Task(2, "Task 2", "Description 2", Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.now());
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(2, history.size(), "История должна содержать 2 задачи.");
        Assertions.assertEquals(task2, history.get(0), "Задача в истории не совпадает с добавленной задачей.");
    }

    @Test
    void addMultipleTasksTest() {
        Task task2 = new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS, Duration.ofMinutes(30),
                LocalDateTime.now());
        Task task3 = new Task(3, "Task 3", "Description 3", Status.DONE, Duration.ofMinutes(45),
                LocalDateTime.now().plusHours(1));
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(3, history.size(), "История должна содержать 3 задачи.");
        Assertions.assertEquals(task3, history.get(0), "Первая задача в истории не совпадает.");
        Assertions.assertEquals(task1, history.get(2), "Третья задача в истории не совпадает.");
    }

    @Test
    void addNullTaskTest() {
        historyManager.remove(1);
        historyManager.add(null);

        List<Task> history = historyManager.getHistory();
        Assertions.assertTrue(history.isEmpty(), "История не должна содержать задач.");
    }

    @Test
    void addTasksWithSameIdTest() {
        Task task2 = new Task(1, "Task 1", "Description 1", Status.IN_PROGRESS,
                Duration.ofMinutes(30), LocalDateTime.now());
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(1, history.size(), "История должна содержать одну задачу.");
        Assertions.assertEquals(task1, history.get(0), "Задачи не совпадают.");
    }

    @Test
    void removeTaskFromHistoryTest() {
        Task task2 = new Task(2, "Task 2", "Description 2", Status.NEW, Duration.ofMinutes(45),
                LocalDateTime.now().plusHours(1));
        historyManager.add(task2);

        historyManager.remove(task2.getId());
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(1, history.size(), "История должна содержать одну задачу после удаления.");
        Assertions.assertEquals(task1, history.get(0), "Задача в истории не совпадает с оставленной задачей.");
    }

    @Test
    void sequentialAddAndRemoveTest() {
        Task task2 = new Task(2, "Task 2", "Description 2", Status.NEW, Duration.ofMinutes(45),
                LocalDateTime.now().plusHours(1));

        historyManager.add(task2);

        historyManager.remove(task1.getId());
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(1, history.size(), "История должна содержать одну задачу после удаления.");
        Assertions.assertEquals(task2, history.get(0), "Задача в истории не совпадает с оставленной задачей.");

        Task task3 = new Task(3, "Task 3", "Description 3", Status.NEW, Duration.ofMinutes(60),
                LocalDateTime.now().plusHours(2));
        historyManager.add(task3);
        history = historyManager.getHistory();
        Assertions.assertEquals(2, history.size(), "История должна содержать две задачи после добавления.");
        Assertions.assertEquals(task3, history.get(0), "Первая задача в истории не совпадает.");
        Assertions.assertEquals(task2, history.get(1), "Вторая задача в истории не совпадает.");
    }

}
