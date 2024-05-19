package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static Integer counterId = 0;
    private HashMap<Integer, Task> tasksMap;
    private HashMap<Integer, Epic> epicsMap;
    private HashMap<Integer, Subtask> subtasksMap;

    private void incrementCounterId() {
        counterId++;
    }

    public TaskManager() {
        this.tasksMap = new HashMap<>();
        this.epicsMap = new HashMap<>();
        this.subtasksMap = new HashMap<>();
    }

    public void createTask(Task task) {
        incrementCounterId();
        task.setId(counterId);
        tasksMap.put(task.getId(), task);
    }

    public void createEpic(Epic epic) {
        incrementCounterId();
        epic.setId(counterId);
        epicsMap.put(epic.getId(), epic);
    }


    public void createSubtask(Subtask subtask) {
        incrementCounterId();
        subtask.setId(counterId);
        subtasksMap.put(subtask.getId(), subtask);
        Epic epic = epicsMap.get(subtask.getEpicId());
        epic.addSubtasksId(subtask.getId());
        updateEpicStatusBasedOnSubtasks(subtask.getEpicId());
    }

    public void updateTask(Task task) {
        if (tasksMap.containsKey(task.getId())) {
            tasksMap.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epicsMap.containsKey(epic.getId())) {
            epicsMap.put(epic.getId(), epic);
        }
    }


    public void updateSubtask(Subtask subtask) {
        if (subtasksMap.containsKey(subtask.getId())) {
            subtasksMap.put(subtask.getId(), subtask);
            updateEpicStatusBasedOnSubtasks(subtask.getEpicId());
        }
    }

    public Task getTaskById(Integer id) {
        return tasksMap.get(id);
    }

    public Epic getEpicById(Integer id) {
        return epicsMap.get(id);
    }

    public Subtask getSubtaskById(Integer id) {
        return subtasksMap.get(id);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicsMap.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasksMap.values());
    }

    public ArrayList<Integer> getAllSubtasksByEpicId(Integer id) {
        Epic epic = epicsMap.get(id);
        if (epic == null) {
            return null;
        } else {
            return epic.getSubtasksId();
        }
    }

    public void deleteTaskById(Integer id) {
        tasksMap.remove(id);
    }

    public void deleteEpicById(Integer id) {
        if (epicsMap.containsKey(id)) {
            Epic epic = epicsMap.remove(id);
            ArrayList<Integer> subtasksId = epic.getSubtasksId();
            for (Integer idSubtask : subtasksId) {
                subtasksMap.remove(idSubtask);
            }
        }
    }

    public void deleteSubtaskById(Integer id) {
        if (subtasksMap.containsKey(id)) {
            Subtask subtask = subtasksMap.remove(id);
            Epic epic = epicsMap.get(subtask.getEpicId());
            epic.removeSubtaskId(id);
            updateEpicStatusBasedOnSubtasks(subtask.getEpicId());
        }
    }

    public void deleteAllTasks() {
        tasksMap.clear();
        epicsMap.clear();
        subtasksMap.clear();
        System.out.println("Все задачи успешно удалены");
    }

    public void updateEpicStatusBasedOnSubtasks(Integer epicId) {
        int counterNew = 0;
        int counterInProgress = 0;
        int counterDone = 0;
        Epic epic = epicsMap.get(epicId);
        ArrayList<Integer> subtasksId = epic.getSubtasksId();

        for (Integer idSubtask : subtasksId) {
            Subtask subtask = subtasksMap.get(idSubtask);
            if (Status.NEW.equals(subtask.getStatus())) {
                counterNew++;
            }
            if (Status.IN_PROGRESS.equals(subtask.getStatus())) {
                counterInProgress++;
            }
            if (Status.DONE.equals(subtask.getStatus())) {
                counterDone++;
            }
        }

        if (counterNew > 0 && counterInProgress == 0 && counterDone == 0) {
            epic.setStatus(Status.NEW);
        } else if (counterNew == 0 && counterInProgress == 0 && counterDone > 0) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

}
