package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private HashMap<Integer, Task> tasksMap;
    private HashMap<Integer, Epic> epicsMap;
    private HashMap<Integer, Subtask> subtasksMap;

    public TaskManager() {
        this.tasksMap = new HashMap<>();
        this.epicsMap = new HashMap<>();
        this.subtasksMap = new HashMap<>();
    }

    public void createOrUpdateTask(Task task) {
        tasksMap.put(task.getId(), task);
        System.out.println("Задача с id " + task.getId() + " успешно сохранена");
    }

    public void createOrUpdateEpic(Epic epic) {
        epicsMap.put(epic.getId(), epic);
        System.out.println("Эпик с id " + epic.getId() + " успешно сохранен");
    }


    public void createOrUpdateSubtask(Subtask subtask) {
        subtasksMap.put(subtask.getId(), subtask);
        System.out.println("Подзадача с id " + subtask.getId() + " успешно сохранена");
    }

    public Task getTaskById(Integer id) {
        Task task = tasksMap.get(id);
        if (task == null) {
            System.out.println("Задача с id " + id + " не найдена");
        } else {
            System.out.println("Задача с id " + id + " найдена");
        }
        return task;
    }

    public Epic getEpicById(Integer id) {
        Epic epic = epicsMap.get(id);
        if (epic == null) {
            System.out.println("Эпик с id " + id + " не найден");
        } else {
            System.out.println("Эпик с id " + id + " найден");
        }
        return epic;
    }

    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtasksMap.get(id);
        if (subtask == null) {
            System.out.println("Подзадача с id " + id + " не найдена");
        } else {
            System.out.println("Подзадача с id " + id + " найдена");
        }
        return subtask;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.addAll(tasksMap.values());
        tasks.addAll(epicsMap.values());
        tasks.addAll(subtasksMap.values());
        System.out.println("Все задачи успешно возвращены. Количество задач = " + tasks.size());
        return tasks;
    }

    public ArrayList<Subtask> getAllSubtasksByEpicId(Integer id) {
        Epic epic = epicsMap.get(id);
        if (epic == null) {
            System.out.println("Эпик с id " + id + " не найден");
            return null;
        } else {
            System.out.println("Эпик с id " + id + " найден");
            System.out.println("Количество подзадач эпика = " + epic.getSubtasks().size());
            return epic.getSubtasks();
        }
    }


    public void deleteById(Integer id) {
        tasksMap.remove(id);
        epicsMap.remove(id);
        subtasksMap.remove(id);
        System.out.println("Задача с id " + id + " успешно удалена");
    }

    public void deleteAllTasks() {
        tasksMap.clear();
        epicsMap.clear();
        subtasksMap.clear();
        System.out.println("Все задачи успешно удалены");
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "tasksMap=" + tasksMap +
                ", epicsMap=" + epicsMap +
                ", subtasksMap=" + subtasksMap +
                '}';
    }
}
