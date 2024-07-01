package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class InMemoryTaskManager implements TaskManager {
    private Integer counterId = 0;
    protected HashMap<Integer, Task> tasksMap;
    protected HashMap<Integer, Epic> epicsMap;
    protected HashMap<Integer, Subtask> subtasksMap;
    private HistoryManager historyManager;

    protected void incrementCounterId() {
        counterId++;
    }

    public InMemoryTaskManager() {
        this.tasksMap = new HashMap<>();
        this.epicsMap = new HashMap<>();
        this.subtasksMap = new HashMap<>();
        this.historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void createTask(Task task) {
        incrementCounterId();
        task.setId(counterId);
        tasksMap.put(task.getId(), task);
    }

    @Override
    public void createEpic(Epic epic) {
        incrementCounterId();
        epic.setId(counterId);
        epicsMap.put(epic.getId(), epic);
    }


    @Override
    public void createSubtask(Subtask subtask) {
        incrementCounterId();
        subtask.setId(counterId);
        subtasksMap.put(subtask.getId(), subtask);
        Epic epic = epicsMap.get(subtask.getEpicId());
        epic.addSubtasksId(subtask.getId());
        updateEpicStatusBasedOnSubtasks(subtask.getEpicId());
    }

    @Override
    public void updateTask(Task task) {
        if (tasksMap.containsKey(task.getId())) {
            tasksMap.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicsMap.containsKey(epic.getId())) {
            epicsMap.put(epic.getId(), epic);
        }
    }


    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasksMap.containsKey(subtask.getId())) {
            subtasksMap.put(subtask.getId(), subtask);
            updateEpicStatusBasedOnSubtasks(subtask.getEpicId());
        }
    }

    @Override
    public Task getTaskById(Integer id) {
        Task task = tasksMap.get(id);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicById(Integer id) {
        Epic epic = epicsMap.get(id);
        historyManager.add(epic);
        return epic;
    }

    @Override
    public Subtask getSubtaskById(Integer id) {
        Subtask subtask = subtasksMap.get(id);
        historyManager.add(subtask);
        return subtask;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasksMap.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epicsMap.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasksMap.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksByEpicId(Integer id) {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        Epic epic = epicsMap.get(id);
        if (epic != null) {
            for (Integer subtaskId : epic.getSubtasksId()) {
                subtasks.add(subtasksMap.get(subtaskId));
            }
        }
        return subtasks;
    }

    @Override
    public void deleteTaskById(Integer id) {
        tasksMap.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicById(Integer id) {
        if (epicsMap.containsKey(id)) {
            Epic epic = epicsMap.remove(id);
            ArrayList<Integer> subtasksId = epic.getSubtasksId();
            for (Integer idSubtask : subtasksId) {
                subtasksMap.remove(idSubtask);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteSubtaskById(Integer id) {
        if (subtasksMap.containsKey(id)) {
            Subtask subtask = subtasksMap.remove(id);
            Epic epic = epicsMap.get(subtask.getEpicId());
            epic.removeSubtaskId(id);
            updateEpicStatusBasedOnSubtasks(subtask.getEpicId());
            historyManager.remove(id);
        }
    }

    private void deleteInHistoryByIds(Set<Integer> keySet) {
        for (Integer key : keySet) {
            historyManager.remove(key);
        }
    }

    @Override
    public void deleteAllTasks() {
        deleteInHistoryByIds(tasksMap.keySet());
        tasksMap.clear();
    }

    @Override
    public void deleteAllEpics() {
        deleteInHistoryByIds(epicsMap.keySet());
        epicsMap.clear();
        deleteInHistoryByIds(subtasksMap.keySet());
        subtasksMap.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        deleteInHistoryByIds(subtasksMap.keySet());
        subtasksMap.clear();
        for (Epic epic : epicsMap.values()) {
            epic.removeAllSubtaskIds();
            epic.setStatusNew();
        }
    }

    protected void updateEpicStatusBasedOnSubtasks(Integer epicId) {
        int counterNew = 0;
        int counterDone = 0;
        Epic epic = epicsMap.get(epicId);
        ArrayList<Integer> subtasksId = epic.getSubtasksId();

        for (Integer idSubtask : subtasksId) {
            Subtask subtask = subtasksMap.get(idSubtask);
            if (Status.NEW.equals(subtask.getStatus())) {
                counterNew++;
            }
            if (Status.DONE.equals(subtask.getStatus())) {
                counterDone++;
            }
        }

        if (counterNew == subtasksId.size()) {
            epic.setStatus(Status.NEW);
        } else if (counterDone == subtasksId.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

}
