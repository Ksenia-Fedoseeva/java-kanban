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
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(2, history.size(), "История должна содержать две задачи.");
        Assertions.assertEquals(task1, history.get(0), "Первая задача в истории не совпадает.");
        Assertions.assertEquals(task2, history.get(1), "Вторая задача в истории не совпадает.");
    }

    @Test
    void addNullTaskTest() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(null);

        List<Task> history = historyManager.getHistory();
        Assertions.assertTrue(history.isEmpty(), "История не должна содержать задач.");
    }

    @Test
    void historyLimitTest() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        for (int i = 1; i <= 11; i++) {
            Task task = new Task("Task " + i, "Description " + i);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(10, history.size(), "История должна содержать только десять последних задач.");
        Assertions.assertEquals("Task 2", history.get(0).getName(), "Первая задача в истории должна быть Task 2.");
        Assertions.assertEquals("Task 11", history.get(9).getName(), "Последняя задача в истории должна быть Task 11.");
    }

    @Test
    void addTasksWithSameIdTest() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task(task1.getId(), "Task 1", "Description 1", Status.DONE);
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(2, history.size(), "История должна содержать две задачи.");
        Assertions.assertEquals(task1, history.get(0), "Первая задача в истории не совпадает.");
        Assertions.assertEquals(task2, history.get(1), "Вторая задача в истории не совпадает.");
    }

}
