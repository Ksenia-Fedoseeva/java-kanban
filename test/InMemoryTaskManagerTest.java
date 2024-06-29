import manager.InMemoryTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

import java.util.List;


class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private Task task;
    private Epic epic;
    private Subtask subtask;

    @BeforeEach
    void init() {
        taskManager = new InMemoryTaskManager();
        task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task);

        epic = new Epic("Test addNewEpic", "Test addNewEPic description");
        taskManager.createEpic(epic);

        subtask = new Subtask("Test addNewEpic", "Test addNewEPic description", epic.getId());
        taskManager.createSubtask(subtask);
    }

    @Test
    void createTaskTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task);
        Task actualTask = taskManager.getTaskById(task.getId());
        Assertions.assertNotNull(actualTask, "Задача не найдена.");
        Assertions.assertEquals(task, actualTask, "Задачи не совпадают.");
    }

    @Test
    void createEpicTest() {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEPic description");
        taskManager.createEpic(epic);
        Epic actualEpic = taskManager.getEpicById(epic.getId());
        Assertions.assertNotNull(actualEpic, "Эпик не найден.");
        Assertions.assertEquals(epic, actualEpic, "Эпики не совпадают.");
    }

    @Test
    void createSubtaskTest() {
        Subtask subtask = new Subtask("Test addNewEpic", "Test addNewEPic description", epic.getId());
        taskManager.createSubtask(subtask);

        Subtask actualSubtask = taskManager.getSubtaskById(subtask.getId());
        Assertions.assertNotNull(actualSubtask, "Подзадача не найдена.");
        Assertions.assertEquals(subtask, actualSubtask, "Подзадачи не совпадают.");
    }

    @Test
    void updateTaskTest() {
        Task updatedTask = new Task(task.getId(), "Test addNewTask", "Test addNewTask description Update",
                Status.IN_PROGRESS);
        taskManager.updateTask(updatedTask);

        Task actualUpdatedTask = taskManager.getTaskById(updatedTask.getId());
        Assertions.assertNotNull(actualUpdatedTask, "Задача не найдена.");
        Assertions.assertEquals(updatedTask, actualUpdatedTask, "Задачи не совпадают.");
        Assertions.assertEquals(1, taskManager.getAllTasks().size(), "Неверное количество задач.");
    }

    @Test
    void updateEpicTest() {
        Epic updateEpic = new Epic(epic.getId(), "Test addNewEpic", "Test addNewEPic description Update",
                Status.IN_PROGRESS);
        taskManager.updateEpic(updateEpic);

        Epic actualUpdateEpic = taskManager.getEpicById(updateEpic.getId());
        Assertions.assertNotNull(actualUpdateEpic, "Эпик не найден.");
        Assertions.assertEquals(updateEpic, actualUpdateEpic, "Эпики не совпадают.");
        Assertions.assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество эпиков.");
    }

    @Test
    void updateSubtaskTest() {
        Subtask updateSubtask = new Subtask(subtask.getId(), "Test addNewEpic",
                "Test addNewEPic description Update", Status.IN_PROGRESS, epic.getId());
        taskManager.updateSubtask(updateSubtask);

        Subtask actualSubtask = taskManager.getSubtaskById(updateSubtask.getId());
        Assertions.assertNotNull(actualSubtask, "Подзадача не найдена.");
        Assertions.assertEquals(updateSubtask, actualSubtask, "Подзадачи не совпадают.");
        Assertions.assertEquals(1, taskManager.getAllSubtasks().size(), "Неверное количество подзадач.");
    }

    @Test
    void getAllTasksTest() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Test addNewTask", "Test addNewTask description");
        Task task2 = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        List<Task> tasks = taskManager.getAllTasks();
        Assertions.assertEquals(2, tasks.size(), "Должно быть 2 задачи.");
        Assertions.assertTrue(tasks.contains(task1), "Список задач должен содержать Task 1.");
        Assertions.assertTrue(tasks.contains(task2), "Список задач должен содержать Task 2.");
    }

    @Test
    void getAllEpicsTest() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Epic 1", "Description 1");
        Epic epic2 = new Epic("Epic 2", "Description 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        List<Epic> epics = taskManager.getAllEpics();
        Assertions.assertEquals(2, epics.size(), "Должно быть 2 эпика.");
        Assertions.assertTrue(epics.contains(epic1), "Список эпиков должен содержать Epic 1.");
        Assertions.assertTrue(epics.contains(epic2), "Список эпиков должен содержать Epic 2.");
    }

    @Test
    void getAllSubtasksTest() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        List<Subtask> subtasks = taskManager.getAllSubtasks();
        Assertions.assertEquals(2, subtasks.size(), "Должно быть 2 подзадачи.");
        Assertions.assertTrue(subtasks.contains(subtask1), "Список подзадач должен содержать Subtask 1.");
        Assertions.assertTrue(subtasks.contains(subtask2), "Список подзадач должен содержать Subtask 2.");
    }

    @Test
    void getAllSubtasksByEpicIdTest() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        List<Subtask> subtasks = taskManager.getAllSubtasksByEpicId(epic.getId());
        Assertions.assertEquals(2, subtasks.size(), "Должно быть 2 подзадачи.");
        Assertions.assertTrue(subtasks.contains(subtask1), "Список подзадач должен содержать Subtask 1.");
        Assertions.assertTrue(subtasks.contains(subtask2), "Список подзадач должен содержать Subtask 2.");
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
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Test addNewTask", "Test addNewTask description");
        Task task2 = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.deleteAllTasks();
        List<Task> tasks = taskManager.getAllTasks();
        Assertions.assertTrue(tasks.isEmpty(), "Все задачи должны быть удалены.");
    }

    @Test
    void deleteAllEpicsTest() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic1 = new Epic("Epic 1", "Description 1");
        Epic epic2 = new Epic("Epic 2", "Description 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic1.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic2.getId());
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
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getId());
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
    void updateEpicStatusBasedOnSubtasksTest() {
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getId());
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getId());
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic createdEpic = taskManager.getEpicById(epic.getId());
        Assertions.assertEquals(Status.NEW, createdEpic.getStatus(), "Статус эпика должен быть NEW.");

        subtask1.setStatus(Status.IN_PROGRESS);
        taskManager.updateSubtask(subtask1);
        Epic updatedEpic1 = taskManager.getEpicById(epic.getId());
        Assertions.assertEquals(Status.IN_PROGRESS, updatedEpic1.getStatus(), "Статус эпика должен быть IN_PROGRESS.");

        subtask1.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        Epic updatedEpic2 = taskManager.getEpicById(epic.getId());
        Assertions.assertEquals(Status.IN_PROGRESS, updatedEpic2.getStatus(), "Статус эпика должен быть IN_PROGRESS.");

        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask2);
        Epic updatedEpic3 = taskManager.getEpicById(epic.getId());
        Assertions.assertEquals(Status.DONE, updatedEpic3.getStatus(), "Статус эпика должен быть DONE.");
    }

    @Test
    void getHistoryTest() {
        TaskManager taskManager = new InMemoryTaskManager();
        Task task1 = new Task("Task 1", "Description 1");
        Task task2 = new Task("Task 2", "Description 2");
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
        TaskManager taskManager = new InMemoryTaskManager();
        Task task = new Task("Task", "Description");
        taskManager.createTask(task);
        taskManager.deleteTaskById(task.getId());

        List<Task> history = taskManager.getHistory();
        Assertions.assertTrue(history.isEmpty(), "История должна быть пустой после удаления задачи из TaskManager.");
    }

    // Тестируем следующий пункт из ТЗ

    // С помощью сеттеров экземпляры задач позволяют изменить любое своё поле, но это может повлиять на данные внутри менеджера.
    // Протестируйте эти кейсы и подумайте над возможными вариантами решения проблемы.
    @Test
    void updateTaskIdTest() {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task);

        Integer oldId = task.getId();
        task.setId(50);
        Task actualTask = taskManager.getTaskById(oldId);

        Assertions.assertNotNull(actualTask, "Задача не найдена.");
        Assertions.assertNotEquals(oldId, task.getId(), "Id задач совпадают.");
        // Изначально, когда писала этот проект, я предложила сделать счетчик id для задач внутри класса Task, и не писать сеттер
        // для поля id. На данном этапе я не вижу других путей решения этой проблемы
    }

}