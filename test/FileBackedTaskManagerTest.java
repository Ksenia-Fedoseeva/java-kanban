import manager.FileBackedTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


class FileBackedTaskManagerTest {
    @Test
    void loadFromFileSomeTasksTest() throws IOException {
        File tempFile = File.createTempFile("taskStorage", ".csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Paths.get(tempFile.getPath()));
        Task task = new Task("Test addNewTask", "Test addNewTask description");
        fileBackedTaskManager.createTask(task);

        Epic epic = new Epic("Test addNewEpic", "Test addNewEPic description");
        fileBackedTaskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description", epic.getId());
        fileBackedTaskManager.createSubtask(subtask);

        List<String> linesFromFileExpected = new ArrayList<>();
        linesFromFileExpected.add("id,type,name,status,description,epic");
        linesFromFileExpected.add("1,TASK,Test addNewTask,NEW,Test addNewTask description,");
        linesFromFileExpected.add("2,EPIC,Test addNewEpic,NEW,Test addNewEPic description,");
        linesFromFileExpected.add("3,SUBTASK,Test addNewSubtask,NEW,Test addNewSubtask description,2");

        assertEqualsFileContent(linesFromFileExpected, tempFile);

        FileBackedTaskManager fileBackedTaskManagerFromFile = FileBackedTaskManager.loadFromFile(Paths.get(tempFile.getPath()));

        Assertions.assertFalse(fileBackedTaskManagerFromFile.getAllTasks().isEmpty(), "Список задач пустой");
        Assertions.assertFalse(fileBackedTaskManagerFromFile.getAllEpics().isEmpty(), "Список эпиков пустой");
        Assertions.assertFalse(fileBackedTaskManagerFromFile.getAllSubtasks().isEmpty(), "Список подзадач пустой");

        Assertions.assertEquals(fileBackedTaskManager.getAllTasks(), fileBackedTaskManagerFromFile.getAllTasks(),
                "Задачи не совпадают.");

        Assertions.assertEquals(fileBackedTaskManager.getAllEpics(), fileBackedTaskManagerFromFile.getAllEpics(),
                "Эпики не совпадают.");

        Assertions.assertEquals(fileBackedTaskManager.getAllSubtasks(), fileBackedTaskManagerFromFile.getAllSubtasks(),
                "Подзадачи не совпадают.");
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