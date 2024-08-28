import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected final T taskManager;
    protected Task task;
    protected Epic epic;
    protected Subtask subtask;

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    @BeforeEach
    void init() {

        task = new Task("Test addNewTask", "Test addNewTask description", Duration.ofMinutes(60),
                LocalDateTime.now());
        taskManager.createTask(task);

        epic = new Epic("Test addNewEpic", "Test addNewEPic description");
        taskManager.createEpic(epic);

        subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description", epic.getId(),
                Duration.ofMinutes(90), LocalDateTime.now().plusHours(1));
        taskManager.createSubtask(subtask);
    }

    @Test
    void createTaskTest() {
        Task newTask = new Task("Test addNewTask", "Test addNewTask description", Duration.ofMinutes(60),
                LocalDateTime.now().plusDays(1));
        taskManager.createTask(newTask);

        Task actualTask = taskManager.getTaskById(newTask.getId());
        Assertions.assertNotNull(actualTask, "Задача не найдена.");
        Assertions.assertEquals(newTask, actualTask, "Задачи не совпадают.");
    }

    @Test
    void createTaskIfTasksHaveTimeConflictTest() {
        Task newTask = new Task("Test addNewTask", "Test addNewTask description", Duration.ofMinutes(60),
                LocalDateTime.now());

        Assertions.assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(newTask));
    }

    @Test
    void createEpicTest() {
        Epic newEpic = new Epic("Test addNewEpic", "Test addNewEPic description");
        taskManager.createEpic(newEpic);

        Epic actualEpic = taskManager.getEpicById(newEpic.getId());
        Assertions.assertNotNull(actualEpic, "Эпик не найден.");
        Assertions.assertEquals(newEpic, actualEpic, "Эпики не совпадают.");
    }

    @Test
    void createSubtaskTest() {
        Subtask newSubtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                epic.getId(), Duration.ofMinutes(90), LocalDateTime.now().plusDays(1));
        taskManager.createSubtask(newSubtask);

        Subtask actualSubtask = taskManager.getSubtaskById(newSubtask.getId());
        Assertions.assertNotNull(actualSubtask, "Подзадача не найдена.");
        Assertions.assertEquals(newSubtask, actualSubtask, "Подзадачи не совпадают.");
    }

    @Test
    void createSubtaskIfSubtasksHaveTimeConflictTest() {
        Subtask newSubtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                epic.getId(), Duration.ofMinutes(90), LocalDateTime.now().plusHours(1));

        Assertions.assertThrows(IllegalArgumentException.class, () -> taskManager.createSubtask(newSubtask));
    }

    @Test
    void updateTaskTest() {
        Task updatedTask = new Task(task.getId(), "Test addNewTask", "Test addNewTask description Update",
                Status.IN_PROGRESS, task.getDuration(), task.getStartTime().plusDays(1));
        taskManager.updateTask(updatedTask);

        Task actualUpdatedTask = taskManager.getTaskById(updatedTask.getId());
        Assertions.assertNotNull(actualUpdatedTask, "Задача не найдена.");
        Assertions.assertEquals(updatedTask, actualUpdatedTask, "Задачи не совпадают.");
        Assertions.assertEquals(1, taskManager.getAllTasks().size(), "Неверное количество задач.");
    }

    @Test
    void updateTaskIfTasksHaveTimeConflictTest() {
        Task updatedTask = new Task(task.getId(), "Test addNewTask", "Test addNewTask description Update",
                Status.IN_PROGRESS, Duration.ofMinutes(60), LocalDateTime.now());

        Assertions.assertThrows(IllegalArgumentException.class, () -> taskManager.createTask(updatedTask));
    }

    @Test
    void updateEpicTest() {
        Epic updatedEpic = new Epic(epic.getId(), "Test addNewEpic", "Test addNewEpic description Update",
                Status.IN_PROGRESS, task.getDuration(), task.getStartTime());
        taskManager.updateEpic(updatedEpic);

        Epic actualUpdatedEpic = taskManager.getEpicById(updatedEpic.getId());
        Assertions.assertNotNull(actualUpdatedEpic, "Эпик не найден.");
        Assertions.assertEquals(updatedEpic, actualUpdatedEpic, "Эпики не совпадают.");
        Assertions.assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество эпиков.");
    }

    @Test
    void updateSubtaskTest() {
        Subtask updatedSubtask = new Subtask(subtask.getId(), "Test addNewSubtask",
                "Test addNewSubtask description Update", Status.IN_PROGRESS, epic.getId(), subtask.getDuration(),
                subtask.getStartTime().plusDays(1));
        taskManager.updateSubtask(updatedSubtask);

        Subtask actualSubtask = taskManager.getSubtaskById(updatedSubtask.getId());
        Assertions.assertNotNull(actualSubtask, "Подзадача не найдена.");
        Assertions.assertEquals(updatedSubtask, actualSubtask, "Подзадачи не совпадают.");
        Assertions.assertEquals(1, taskManager.getAllSubtasks().size(), "Неверное количество подзадач.");
    }

    @Test
    void updateSubtaskIfSubtasksHaveTimeConflictTest() {
        Subtask updatedSubtask = new Subtask(subtask.getId(), "Test addNewSubtask",
                "Test addNewSubtask description Update", Status.IN_PROGRESS, epic.getId(), Duration.ofMinutes(60),
                LocalDateTime.now());

        Assertions.assertThrows(IllegalArgumentException.class, () -> taskManager.createSubtask(updatedSubtask));
    }

    @Test
    void deleteTaskByIdTest() {
        taskManager.deleteTaskById(task.getId());
        Task deletedTask = taskManager.getTaskById(task.getId());
        Assertions.assertNull(deletedTask, "Задача не была удалена.");
    }

    @Test
    void deleteEpicByIdTest() {
        taskManager.deleteEpicById(epic.getId());
        Epic deletedEpic = taskManager.getEpicById(epic.getId());
        Subtask deletedSubtask = taskManager.getSubtaskById(subtask.getId());
        Assertions.assertNull(deletedEpic, "Эпик не был удален.");
        Assertions.assertNull(deletedSubtask, "Подзадача не была удалена вместе с эпиком.");
    }

    @Test
    void deleteSubtaskByIdTest() {
        taskManager.deleteSubtaskById(subtask.getId());
        Subtask deletedSubtask = taskManager.getSubtaskById(subtask.getId());
        Assertions.assertNull(deletedSubtask, "Подзадача не была удалена.");
    }

    @Test
    void deleteAllTasksTest() {
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Duration.ofMinutes(20),
                LocalDateTime.now().plusDays(1));
        Task task2 = new Task("Test addNewTask", "Test addNewTask description", Duration.ofMinutes(15),
                LocalDateTime.now().plusDays(2));
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.deleteAllTasks();
        List<Task> tasks = taskManager.getAllTasks();
        Assertions.assertTrue(tasks.isEmpty(), "Все задачи должны быть удалены.");
    }

    @Test
    void deleteAllEpicsTest() {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        Epic epic2 = new Epic("Epic 2", "Description 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic1.getId(), Duration.ofMinutes(60),
                LocalDateTime.now().plusDays(1));
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic2.getId(), Duration.ofMinutes(90),
                LocalDateTime.now().plusDays(2));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.deleteAllEpics();
        List<Epic> epics = taskManager.getAllEpics();
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        Assertions.assertTrue(epics.isEmpty(), "Все эпики должны быть удалены.");
        Assertions.assertTrue(subtasks.isEmpty(), "Все подзадачи должны быть удалены вместе с эпиками.");
    }

    @Test
    void deleteAllSubtasksTest() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getId(), Duration.ofMinutes(60),
                LocalDateTime.now().plusDays(1));
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getId(), Duration.ofMinutes(90),
                LocalDateTime.now().plusDays(2));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        taskManager.deleteAllSubtasks();
        List<Subtask> subtasks = taskManager.getAllSubtasks();
        Epic updatedEpic = taskManager.getEpicById(epic.getId());
        Assertions.assertTrue(subtasks.isEmpty(), "Все подзадачи должны быть удалены.");
        Assertions.assertTrue(updatedEpic.getSubtasksId().isEmpty(), "Все ID подзадач должны быть удалены из эпика.");
        Assertions.assertEquals(Status.NEW, updatedEpic.getStatus(), "Статус эпика должен быть NEW.");
    }

    @Test
    void getHistoryTest() {
        Task task1 = new Task("Task 1", "Description 1", Duration.ofMinutes(60),
                LocalDateTime.now().plusDays(1));
        Task task2 = new Task("Task 2", "Description 2", Duration.ofMinutes(90),
                LocalDateTime.now().plusDays(2));
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());

        List<Task> history = taskManager.getHistory();
        Assertions.assertEquals(2, history.size(), "История должна содержать 2 задачи.");
        Assertions.assertEquals(task2, history.get(0), "Первая задача в истории не совпадает.");
        Assertions.assertEquals(task1, history.get(1), "Вторая задача в истории не совпадает.");
    }

    @Test
    void removeTaskFromHistoryWhenDeletedFromTaskManagerTest() {
        Task task = new Task("Task", "Description", Duration.ofMinutes(60), LocalDateTime.now().plusDays(1));
        taskManager.createTask(task);
        taskManager.deleteTaskById(task.getId());

        List<Task> history = taskManager.getHistory();
        Assertions.assertTrue(history.isEmpty(), "История должна быть пустой после удаления задачи из TaskManager.");
    }

    @Test
    void getPrioritizedTasksTest() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();

        Task task1 = new Task("Task 1", "Description 1", Duration.ofMinutes(60),
                LocalDateTime.of(2024, 8, 25, 10, 0).plusDays(1));
        Task task2 = new Task("Task 2", "Description 2", Duration.ofMinutes(120),
                LocalDateTime.of(2024, 8, 25, 8, 0));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Epic 1", "Description Epic 1");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic1.getId(),
                Duration.ofMinutes(60), LocalDateTime.of(2024, 8, 25, 9, 0).plusDays(1));
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic1.getId(),
                Duration.ofMinutes(30), LocalDateTime.of(2024, 8, 25, 7, 30));

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        List<Task> prioritizedTasks = taskManager.getPrioritizedTasks();

        Assertions.assertEquals(subtask2, prioritizedTasks.get(0), "Первая задача должна быть Subtask 2");
        Assertions.assertEquals(task2, prioritizedTasks.get(1), "Вторая задача должна быть Task 2");
        Assertions.assertEquals(subtask1, prioritizedTasks.get(2), "Третья задача должна быть Subtask 1");
        Assertions.assertEquals(task1, prioritizedTasks.get(3), "Четвертая задача должна быть Task 1");
    }

    @Test
    void updateTaskIdTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Duration.ofMinutes(60),
                LocalDateTime.now().plusDays(1));
        taskManager.createTask(task);

        Integer oldId = task.getId();

        task.setId(50);
        Task actualTask = taskManager.getTaskById(oldId);

        Assertions.assertNotNull(actualTask, "Задача не найдена.");
        Assertions.assertNotEquals(oldId, task.getId(), "Id задач не должны совпадать.");
    }

    @Test
    void createSubtaskWithoutEpicTest() {
        Subtask subtask = new Subtask("Test Subtask", "Description", 999, Duration.ofMinutes(30), LocalDateTime.now().plusDays(1));
        Assertions.assertThrows(NullPointerException.class, () -> taskManager.createSubtask(subtask), "Должно выбрасываться исключение при создании подзадачи без эпика");
    }

}
