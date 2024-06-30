import manager.FileBackedTaskManager;
import manager.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


class FileBackedTaskManagerTest {
    private TaskManager taskManager;
    private Task task;
    private Epic epic;
    private Subtask subtask;
    private File tempFile;

    @BeforeEach
    void init() throws IOException {
        tempFile = File.createTempFile("taskStorage", ".csv");
        taskManager = new FileBackedTaskManager(Paths.get(tempFile.getPath()));
        task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task);

        epic = new Epic("Test addNewEpic", "Test addNewEPic description");
        taskManager.createEpic(epic);

        subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description", epic.getId());
        taskManager.createSubtask(subtask);
    }

    @Test
    void createTaskTest() throws IOException {
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task);
        Task actualTask = taskManager.getTaskById(task.getId());
        Assertions.assertNotNull(actualTask, "Задача не найдена.");
        Assertions.assertEquals(task, actualTask, "Задачи не совпадают.");

        List<String> linesFromFileExpected = new ArrayList<>();
        linesFromFileExpected.add("id,type,name,status,description,epic");
        linesFromFileExpected.add("1,TASK,Test addNewTask,NEW,Test addNewTask description,");
        linesFromFileExpected.add("4,TASK,Test addNewTask,NEW,Test addNewTask description,");
        linesFromFileExpected.add("2,EPIC,Test addNewEpic,NEW,Test addNewEPic description,");
        linesFromFileExpected.add("3,SUBTASK,Test addNewSubtask,NEW,Test addNewSubtask description,2");

        assertEqualsFileContent(linesFromFileExpected, tempFile);
    }

    @Test
    void createEpicTest() throws IOException {
        Epic epic = new Epic("Test addNewEpic", "Test addNewEPic description");
        taskManager.createEpic(epic);
        Epic actualEpic = taskManager.getEpicById(epic.getId());
        Assertions.assertNotNull(actualEpic, "Эпик не найден.");
        Assertions.assertEquals(epic, actualEpic, "Эпики не совпадают.");

        List<String> linesFromFileExpected = new ArrayList<>();
        linesFromFileExpected.add("id,type,name,status,description,epic");
        linesFromFileExpected.add("1,TASK,Test addNewTask,NEW,Test addNewTask description,");
        linesFromFileExpected.add("2,EPIC,Test addNewEpic,NEW,Test addNewEPic description,");
        linesFromFileExpected.add("4,EPIC,Test addNewEpic,NEW,Test addNewEPic description,");
        linesFromFileExpected.add("3,SUBTASK,Test addNewSubtask,NEW,Test addNewSubtask description,2");

        assertEqualsFileContent(linesFromFileExpected, tempFile);
    }

    @Test
    void createSubtaskTest() throws IOException {
        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description", epic.getId());
        taskManager.createSubtask(subtask);

        Subtask actualSubtask = taskManager.getSubtaskById(subtask.getId());
        Assertions.assertNotNull(actualSubtask, "Подзадача не найдена.");
        Assertions.assertEquals(subtask, actualSubtask, "Подзадачи не совпадают.");

        List<String> linesFromFileExpected = new ArrayList<>();
        linesFromFileExpected.add("id,type,name,status,description,epic");
        linesFromFileExpected.add("1,TASK,Test addNewTask,NEW,Test addNewTask description,");
        linesFromFileExpected.add("2,EPIC,Test addNewEpic,NEW,Test addNewEPic description,");
        linesFromFileExpected.add("3,SUBTASK,Test addNewSubtask,NEW,Test addNewSubtask description,2");
        linesFromFileExpected.add("4,SUBTASK,Test addNewSubtask,NEW,Test addNewSubtask description,2");

        assertEqualsFileContent(linesFromFileExpected, tempFile);
    }

    @Test
    void updateTaskTest() throws IOException {
        Task updatedTask = new Task(task.getId(), "Test addNewTask", "Test addNewTask description Update",
                Status.IN_PROGRESS);
        taskManager.updateTask(updatedTask);

        Task actualUpdatedTask = taskManager.getTaskById(updatedTask.getId());
        Assertions.assertNotNull(actualUpdatedTask, "Задача не найдена.");
        Assertions.assertEquals(updatedTask, actualUpdatedTask, "Задачи не совпадают.");
        Assertions.assertEquals(1, taskManager.getAllTasks().size(), "Неверное количество задач.");

        List<String> linesFromFileExpected = new ArrayList<>();
        linesFromFileExpected.add("id,type,name,status,description,epic");
        linesFromFileExpected.add("1,TASK,Test addNewTask,IN_PROGRESS,Test addNewTask description Update,");
        linesFromFileExpected.add("2,EPIC,Test addNewEpic,NEW,Test addNewEPic description,");
        linesFromFileExpected.add("3,SUBTASK,Test addNewSubtask,NEW,Test addNewSubtask description,2");

        assertEqualsFileContent(linesFromFileExpected, tempFile);
    }

    @Test
    void updateEpicTest() throws IOException {
        Epic updateEpic = new Epic(epic.getId(), "Test addNewEpic", "Test addNewEPic description Update",
                Status.IN_PROGRESS);
        taskManager.updateEpic(updateEpic);

        Epic actualUpdateEpic = taskManager.getEpicById(updateEpic.getId());
        Assertions.assertNotNull(actualUpdateEpic, "Эпик не найден.");
        Assertions.assertEquals(updateEpic, actualUpdateEpic, "Эпики не совпадают.");
        Assertions.assertEquals(1, taskManager.getAllEpics().size(), "Неверное количество эпиков.");

        List<String> linesFromFileExpected = new ArrayList<>();
        linesFromFileExpected.add("id,type,name,status,description,epic");
        linesFromFileExpected.add("1,TASK,Test addNewTask,NEW,Test addNewTask description,");
        linesFromFileExpected.add("2,EPIC,Test addNewEpic,IN_PROGRESS,Test addNewEPic description Update,");
        linesFromFileExpected.add("3,SUBTASK,Test addNewSubtask,NEW,Test addNewSubtask description,2");

        assertEqualsFileContent(linesFromFileExpected, tempFile);
    }

    @Test
    void updateSubtaskTest() throws IOException {
        Subtask updateSubtask = new Subtask(subtask.getId(), "Test addNewSubtask",
                "Test addNewSubtask description Update", Status.IN_PROGRESS, epic.getId());
        taskManager.updateSubtask(updateSubtask);

        Subtask actualSubtask = taskManager.getSubtaskById(updateSubtask.getId());
        Assertions.assertNotNull(actualSubtask, "Подзадача не найдена.");
        Assertions.assertEquals(updateSubtask, actualSubtask, "Подзадачи не совпадают.");
        Assertions.assertEquals(1, taskManager.getAllSubtasks().size(), "Неверное количество подзадач.");

        List<String> linesFromFileExpected = new ArrayList<>();
        linesFromFileExpected.add("id,type,name,status,description,epic");
        linesFromFileExpected.add("1,TASK,Test addNewTask,NEW,Test addNewTask description,");
        linesFromFileExpected.add("2,EPIC,Test addNewEpic,IN_PROGRESS,Test addNewEPic description,");
        linesFromFileExpected.add("3,SUBTASK,Test addNewSubtask,IN_PROGRESS,Test addNewSubtask description Update,2");

        assertEqualsFileContent(linesFromFileExpected, tempFile);
    }

    @Test
    void deleteTaskByIdTest() throws IOException {
        taskManager.deleteTaskById(task.getId());
        Task deletedTask = taskManager.getTaskById(task.getId());
        Assertions.assertNull(deletedTask, "Задача не была удалена.");

        List<String> linesFromFileExpected = new ArrayList<>();
        linesFromFileExpected.add("id,type,name,status,description,epic");
        linesFromFileExpected.add("2,EPIC,Test addNewEpic,NEW,Test addNewEPic description,");
        linesFromFileExpected.add("3,SUBTASK,Test addNewSubtask,NEW,Test addNewSubtask description,2");

        assertEqualsFileContent(linesFromFileExpected, tempFile);
    }

    @Test
    void deleteEpicByIdTest() throws IOException {
        taskManager.deleteEpicById(epic.getId());
        Epic deletedEpic = taskManager.getEpicById(epic.getId());
        Subtask deletedSubtask = taskManager.getSubtaskById(subtask.getId());
        Assertions.assertNull(deletedEpic, "Эпик не был удален.");
        Assertions.assertNull(deletedSubtask, "Подзадача не была удалена вместе с эпиком.");

        List<String> linesFromFileExpected = new ArrayList<>();
        linesFromFileExpected.add("id,type,name,status,description,epic");
        linesFromFileExpected.add("1,TASK,Test addNewTask,NEW,Test addNewTask description,");

        assertEqualsFileContent(linesFromFileExpected, tempFile);
    }

    @Test
    void deleteSubtaskByIdTest() throws IOException {
        taskManager.deleteSubtaskById(subtask.getId());
        Subtask deletedSubtask = taskManager.getSubtaskById(subtask.getId());
        Assertions.assertNull(deletedSubtask, "Подзадача не была удалена.");

        List<String> linesFromFileExpected = new ArrayList<>();
        linesFromFileExpected.add("id,type,name,status,description,epic");
        linesFromFileExpected.add("1,TASK,Test addNewTask,NEW,Test addNewTask description,");
        linesFromFileExpected.add("2,EPIC,Test addNewEpic,NEW,Test addNewEPic description,");

        assertEqualsFileContent(linesFromFileExpected, tempFile);
    }

    @Test
    void deleteAllTasksTest() throws IOException {
        TaskManager taskManager = new FileBackedTaskManager(Paths.get(tempFile.getPath()));
        Task task1 = new Task("Test addNewTask", "Test addNewTask description");
        Task task2 = new Task("Test addNewTask", "Test addNewTask description");
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.deleteAllTasks();
        List<Task> tasks = taskManager.getAllTasks();
        Assertions.assertTrue(tasks.isEmpty(), "Все задачи должны быть удалены.");

        List<String> linesFromFileExpected = new ArrayList<>();
        linesFromFileExpected.add("id,type,name,status,description,epic");

        assertEqualsFileContent(linesFromFileExpected, tempFile);
    }

    @Test
    void deleteAllEpicsTest() throws IOException {
        TaskManager taskManager = new FileBackedTaskManager(Paths.get(tempFile.getPath()));
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

        List<String> linesFromFileExpected = new ArrayList<>();
        linesFromFileExpected.add("id,type,name,status,description,epic");

        assertEqualsFileContent(linesFromFileExpected, tempFile);
    }

    @Test
    void deleteAllSubtasksTest() throws IOException {
        TaskManager taskManager = new FileBackedTaskManager(Paths.get(tempFile.getPath()));
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

        List<String> linesFromFileExpected = new ArrayList<>();
        linesFromFileExpected.add("id,type,name,status,description,epic");
        linesFromFileExpected.add("1,EPIC,Epic,NEW,Description,");

        assertEqualsFileContent(linesFromFileExpected, tempFile);
    }

    @Test
    void loadFromEmptyFileTest() throws IOException {
        File tempFile = File.createTempFile("taskStorage", ".csv");
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(Paths.get(tempFile.getPath()));

        Assertions.assertTrue(fileBackedTaskManager.getAllTasks().isEmpty(), "Список задач не пустой");
        Assertions.assertTrue(fileBackedTaskManager.getAllEpics().isEmpty(), "Список эпиков не пустой");
        Assertions.assertTrue(fileBackedTaskManager.getAllSubtasks().isEmpty(), "Список подзадач не пустой");
    }

    @Test
    void loadFromFileSomeTasksTest() {
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(Paths.get(tempFile.getPath()));

        Assertions.assertFalse(fileBackedTaskManager.getAllTasks().isEmpty(), "Список задач пустой");
        Assertions.assertFalse(fileBackedTaskManager.getAllEpics().isEmpty(), "Список эпиков пустой");
        Assertions.assertFalse(fileBackedTaskManager.getAllSubtasks().isEmpty(), "Список подзадач пустой");

        Task actualTask = taskManager.getTaskById(task.getId());
        Assertions.assertNotNull(actualTask, "Задача не найдена.");
        Assertions.assertEquals(task, actualTask, "Задачи не совпадают.");

        Epic actualEpic = taskManager.getEpicById(epic.getId());
        Assertions.assertNotNull(actualEpic, "Эпик не найден.");
        Assertions.assertEquals(epic, actualEpic, "Эпики не совпадают.");

        Subtask actualSubtask = taskManager.getSubtaskById(subtask.getId());
        Assertions.assertNotNull(actualSubtask, "Подзадача не найдена.");
        Assertions.assertEquals(subtask, actualSubtask, "Подзадачи не совпадают.");
    }

    private void assertEqualsFileContent(List<String> linesFromFileExpected, File tempFile) throws IOException {
        List<String> linesFromFileActual = new ArrayList<>();
        FileReader reader = new FileReader(tempFile);
        BufferedReader br = new BufferedReader(reader);

        while (br.ready()) {
            String line = br.readLine();
            linesFromFileActual.add(line);
        }

        Assertions.assertEquals(linesFromFileExpected, linesFromFileActual, "Задачи в файле не совпадают.");
    }

}