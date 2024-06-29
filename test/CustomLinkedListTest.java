import customcollection.CustomLinkedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.enums.Status;

import java.util.ArrayList;
import java.util.List;

public class CustomLinkedListTest {
    private CustomLinkedList customLinkedList;
    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;
    private Task task5;
    private List<Task> testListTask;

    @BeforeEach
    void init() {
        customLinkedList = new CustomLinkedList();
        task1 = new Task(1, "Task 1", "Description 1", Status.NEW);
        task2 = new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS);
        task3 = new Task(3, "Task 3", "Description 3", Status.DONE);
        task4 = new Task(4, "Task 4", "Description 4", Status.NEW);
        task5 = new Task(5, "Task 5", "Description 5", Status.IN_PROGRESS);
        testListTask = new ArrayList<>();
        testListTask.add(task5);
        testListTask.add(task4);
        testListTask.add(task3);
        testListTask.add(task2);
        testListTask.add(task1);
    }

    @Test
    void linkLastTest() {
        customLinkedList.linkLast(task1);
        customLinkedList.linkLast(task2);

        List<Task> tasks = customLinkedList.getTasks();
        Assertions.assertEquals(2, tasks.size(), "Должно быть 2 задачи в списке.");
        Assertions.assertEquals(task2, tasks.get(0), "Первая задача должна быть Task 1.");
        Assertions.assertEquals(task1, tasks.get(1), "Вторая задача должна быть Task 2.");
    }

    @Test
    void removeOnlyOneTaskTest() {
        customLinkedList.linkLast(task1);
        customLinkedList.removeTask(task1.getId());

        List<Task> tasks = customLinkedList.getTasks();
        Assertions.assertEquals(0, tasks.size(), "Должно быть 0 задач в списке.");
    }

    @Test
    void removeFirstWhenHasTwoTasksTest() {
        customLinkedList.linkLast(task1);
        customLinkedList.linkLast(task2);
        customLinkedList.removeTask(task1.getId());

        List<Task> tasks = customLinkedList.getTasks();
        Assertions.assertEquals(1, tasks.size(), "Должна быть 1 задача в списке.");
        Assertions.assertEquals(task2, tasks.get(0), "Оставшаяся задача должна быть Task 2.");
    }

    @Test
    void removeLastWhenHasTwoTasksTest() {
        customLinkedList.linkLast(task1);
        customLinkedList.linkLast(task2);
        customLinkedList.removeTask(task2.getId());

        List<Task> tasks = customLinkedList.getTasks();
        Assertions.assertEquals(1, tasks.size(), "Должна быть 1 задача в списке.");
        Assertions.assertEquals(task1, tasks.get(0), "Оставшаяся задача должна быть Task 1.");
    }

    @Test
    void removeFirstWhenHasFiveTasksTest() {
        customLinkedList.linkLast(task1);
        customLinkedList.linkLast(task2);
        customLinkedList.linkLast(task3);
        customLinkedList.linkLast(task4);
        customLinkedList.linkLast(task5);
        customLinkedList.removeTask(task1.getId());

        List<Task> tasks = customLinkedList.getTasks();
        Assertions.assertEquals(4, tasks.size(), "Должна быть 4 задачи в списке.");

        testListTask.remove(task1);
        Assertions.assertEquals(testListTask, tasks, "Списки задач не совпадают.");
    }

    @Test
    void removeLastWhenHasFiveTasksTest() {
        customLinkedList.linkLast(task1);
        customLinkedList.linkLast(task2);
        customLinkedList.linkLast(task3);
        customLinkedList.linkLast(task4);
        customLinkedList.linkLast(task5);
        customLinkedList.removeTask(task5.getId());

        List<Task> tasks = customLinkedList.getTasks();
        Assertions.assertEquals(4, tasks.size(), "Должна быть 4 задачи в списке.");

        testListTask.remove(task5);
        Assertions.assertEquals(testListTask, tasks, "Списки задач не совпадают.");
    }

    @Test
    void removeMiddleWhenHasFiveTasksTest() {
        customLinkedList.linkLast(task1);
        customLinkedList.linkLast(task2);
        customLinkedList.linkLast(task3);
        customLinkedList.linkLast(task4);
        customLinkedList.linkLast(task5);
        customLinkedList.removeTask(task3.getId());

        List<Task> tasks = customLinkedList.getTasks();
        Assertions.assertEquals(4, tasks.size(), "Должна быть 4 задачи в списке.");

        testListTask.remove(task3);
        Assertions.assertEquals(testListTask, tasks, "Списки задач не совпадают.");
    }

    @Test
    void addDuplicateTaskTest() {
        Task duplicateTask = new Task(task1.getId(), "Task 1", "Description 1 updated", Status.DONE);
        customLinkedList.linkLast(task1);
        customLinkedList.linkLast(duplicateTask);

        List<Task> tasks = customLinkedList.getTasks();
        Assertions.assertEquals(1, tasks.size(), "Должна быть 1 задача в списке.");
        Assertions.assertEquals(duplicateTask, tasks.get(0), "Задача в списке должна быть обновленной Task 1.");
    }


    @Test
    void addDuplicateTaskTest2() {
        Task duplicateTask = new Task(task1.getId(), "Task 1", "Description 1 updated", Status.DONE);
        customLinkedList.linkLast(task1);
        customLinkedList.linkLast(task2);
        customLinkedList.linkLast(duplicateTask);

        List<Task> tasks = customLinkedList.getTasks();
        Assertions.assertEquals(2, tasks.size(), "Должно быть 2 задачи в списке.");
        Assertions.assertEquals(duplicateTask, tasks.get(0), "Задача в списке должна быть обновленной Task 1.");
    }

}
