import exceptions.ManagerSaveException;
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
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


class FileBackedTaskManagerTest {
    @Test
    void loadFromFileSomeTasksTest() throws IOException {
        File tempFile = File.createTempFile("taskStorage", ".csv");

        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Paths.get(tempFile.getPath()));

        Task task = new Task("Test addNewTask", "Test addNewTask description", Duration.ofMinutes(60),
                LocalDateTime.of(2024, 8, 25, 10, 0));
        fileBackedTaskManager.createTask(task);

        Epic epic = new Epic("Test addNewEpic", "Test addNewEpic description");
        fileBackedTaskManager.createEpic(epic);

        Subtask subtask = new Subtask("Test addNewSubtask", "Test addNewSubtask description",
                epic.getId(), Duration.ofMinutes(90), LocalDateTime.of(2024, 8, 25, 12, 0));
        fileBackedTaskManager.createSubtask(subtask);

        List<String> linesFromFileExpected = new ArrayList<>();
        linesFromFileExpected.add("id,type,name,status,description,epic,duration,startTime");
        linesFromFileExpected.add("1,TASK,Test addNewTask,NEW,Test addNewTask description,,60,2024-08-25T10:00");
        linesFromFileExpected.add("2,EPIC,Test addNewEpic,NEW,Test addNewEpic description,,90,2024-08-25T12:00");  // duration и startTime эпика рассчитываются
        linesFromFileExpected.add("3,SUBTASK,Test addNewSubtask,NEW,Test addNewSubtask description,2,90,2024-08-25T12:00");

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

    @Test
    void loadFromFileWithNonExistentFileTest() {
        // Проверяем, что будет выброшено исключение, если файл не существует
        Assertions.assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager.loadFromFile(Paths.get("non_existent_file.csv"));
        }, "Должно выбрасываться исключение, если файл не существует.");
    }

}