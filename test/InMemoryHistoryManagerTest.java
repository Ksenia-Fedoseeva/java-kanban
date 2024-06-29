import manager.HistoryManager;
import manager.InMemoryHistoryManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.enums.Status;

import java.util.List;

public class InMemoryHistoryManagerTest {

    @Test
    void addTaskTest() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task = new Task("Task 1", "Description 1");
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(1, history.size(), "История должна содержать одну задачу.");
        Assertions.assertEquals(task, history.get(0), "Задача в истории не совпадает с добавленной задачей.");
    }

    @Test
    void addMultipleTasksTest() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Task 1", "Description 1", Status.IN_PROGRESS);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.DONE);
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(2, history.size(), "История должна содержать две задачи.");
        Assertions.assertEquals(task2, history.get(0), "Первая задача в истории не совпадает.");
        Assertions.assertEquals(task1, history.get(1), "Вторая задача в истории не совпадает.");
    }

    @Test
    void addNullTaskTest() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(null);

        List<Task> history = historyManager.getHistory();
        Assertions.assertTrue(history.isEmpty(), "История не должна содержать задач.");
    }

    @Test
    void addTasksWithSameIdTest() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Task 1", "Description 1", Status.IN_PROGRESS);
        Task task2 = new Task(task1.getId(), "Task 1", "Description 1", Status.DONE);
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(1, history.size(), "История должна содержать одну задачу.");
        Assertions.assertEquals(task2, history.get(0), "Задачи не совпадают.");
    }

    @Test
    void removeTaskFromHistoryTest() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.NEW);
        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task1.getId());
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(1, history.size(), "История должна содержать одну задачу после удаления.");
        Assertions.assertEquals(task2, history.get(0), "Задача в истории не совпадает с оставленной задачей.");
    }

    @Test
    void addTaskWithSameIdTest() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.NEW);
        Task task3 = new Task(3, "Task 3", "Description 3", Status.NEW);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        Task task4 = new Task(2, "Task 2 Updated", "Description 2 Updated", Status.IN_PROGRESS);
        historyManager.add(task4);

        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(3, history.size(), "История должна содержать три задачи.");
        Assertions.assertEquals(task4, history.get(0), "Первая задача в истории не совпадает.");
        Assertions.assertEquals(task3, history.get(1), "Вторая задача в истории не совпадает.");
        Assertions.assertEquals(task1, history.get(2), "Третья задача в истории не совпадает.");
    }

    @Test
    void sequentialAddAndRemoveTest() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Task 1", "Description 1", Status.NEW);
        Task task2 = new Task(2, "Task 2", "Description 2", Status.NEW);

        historyManager.add(task1);
        historyManager.add(task2);

        historyManager.remove(task1.getId());
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(1, history.size(), "История должна содержать одну задачу после удаления.");
        Assertions.assertEquals(task2, history.get(0), "Задача в истории не совпадает с оставленной задачей.");

        Task task3 = new Task(3, "Task 3", "Description 3", Status.NEW);
        historyManager.add(task3);
        history = historyManager.getHistory();
        Assertions.assertEquals(2, history.size(), "История должна содержать две задачи после добавления.");
        Assertions.assertEquals(task3, history.get(0), "Первая задача в истории не совпадает.");
        Assertions.assertEquals(task2, history.get(1), "Вторая задача в истории не совпадает.");
    }

}
