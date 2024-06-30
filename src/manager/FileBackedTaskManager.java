package manager;

import exceptions.ManagerSaveException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.*;
import java.nio.file.Path;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private Path taskStorage;

    public FileBackedTaskManager(Path taskStorage) {
        this.taskStorage = taskStorage;
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskById(Integer id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicById(Integer id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    private void save() {
        try {
            FileWriter writer = new FileWriter(taskStorage.toFile(), false);
            writer.write("id,type,name,status,description,epic");
            for (Task task : getAllTasks()) {
                writer.write("\n" + task.toString());
            }
            for (Epic epic : getAllEpics()) {
                writer.write("\n" + epic.toString());
            }
            for (Subtask subtask : getAllSubtasks()) {
                writer.write("\n" + subtask.toString());
            }
            writer.close();
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage(), e);
        }
    }

    public static FileBackedTaskManager loadFromFile(Path path) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(path);
        try {
            FileReader reader = new FileReader(path.toFile());
            BufferedReader br = new BufferedReader(reader);

            while (br.ready()) {
                String line = br.readLine();
                if (line.startsWith("id")) {
                    continue;
                }
                fileBackedTaskManager.writeTaskForLoadFromFile(Task.fromString(line));
            }

            br.close();
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage(), e);
        }
        return fileBackedTaskManager;
    }

    private void writeTaskForLoadFromFile(Task task) {
        if (task != null) {
            if (task instanceof Epic) {
                super.createEpicForLoadFromFile((Epic) task);
            } else if (task instanceof Subtask) {
                super.createSubtaskForLoadFromFile((Subtask) task);
            } else {
                super.createTaskForLoadFromFile(task);
            }
        }
    }

}
