import customсollection.CustomLinkedList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.enums.Status;

import java.util.List;

public class CustomLinkedListTest {
    private CustomLinkedList customLinkedList;
    private Task task1;
    private Task task2;

    @BeforeEach
    void init() {
        customLinkedList = new CustomLinkedList();
        task1 = new Task(1, "Task 1", "Description 1", Status.NEW);
        task2 = new Task(2, "Task 2", "Description 2", Status.IN_PROGRESS);
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
    void removeTaskTest() {
        customLinkedList.linkLast(task1);
        customLinkedList.linkLast(task2);
        customLinkedList.removeTask(task1.getId());

        List<Task> tasks = customLinkedList.getTasks();
        Assertions.assertEquals(1, tasks.size(), "Должна быть 1 задача в списке.");
        Assertions.assertEquals(task2, tasks.get(0), "Оставшаяся задача должна быть Task 2.");
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
