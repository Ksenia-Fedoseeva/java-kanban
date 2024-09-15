package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
        if (hasTimeConflict(task)) {
            throw new IllegalArgumentException("Задача пересекается по времени с другой задачей.");
        }
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
        if (hasTimeConflict(subtask)) {
            throw new IllegalArgumentException("Задача пересекается по времени с другой задачей.");
        }
        incrementCounterId();
        subtask.setId(counterId);
        subtasksMap.put(subtask.getId(), subtask);
        Epic epic = epicsMap.get(subtask.getEpicId());
        epic.addSubtasksId(subtask.getId());
        updateEpicStatusBasedOnSubtasks(subtask.getEpicId());
        updateEpicEvaluation(subtask.getEpicId());
    }

    @Override
    public void updateTask(Task task) {
        if (hasTimeConflict(task)) {
            throw new IllegalArgumentException("Задача пересекается по времени с другой задачей.");
        }
        if (tasksMap.containsKey(task.getId())) {
            tasksMap.put(task.getId(), task);
        } else {
            throw new IllegalArgumentException("Задача с ID " + task.getId() + " не найдена.");
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epicsMap.containsKey(epic.getId())) {
            epicsMap.put(epic.getId(), epic);
        } else {
            throw new IllegalArgumentException("Эпик с ID " + epic.getId() + " не найден.");
        }
    }


    @Override
    public void updateSubtask(Subtask subtask) {
        if (hasTimeConflict(subtask)) {
            throw new IllegalArgumentException("Задача пересекается по времени с другой задачей.");
        }
        if (subtasksMap.containsKey(subtask.getId())) {
            subtasksMap.put(subtask.getId(), subtask);
            updateEpicStatusBasedOnSubtasks(subtask.getEpicId());
            updateEpicEvaluation(subtask.getEpicId());
        } else {
            throw new IllegalArgumentException("Подзадача с ID " + subtask.getId() + " не найдена.");
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
        Epic epic = epicsMap.get(id);
        if (epic != null) {
            return epic.getSubtasksId().stream()
                    .map(subtasksMap::get)
                    .collect(Collectors.toCollection(ArrayList::new));

            }
        return new ArrayList<>();
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
            epic.getSubtasksId().forEach(subtasksMap::remove);
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
            updateEpicEvaluation(subtask.getEpicId());
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
        epicsMap.values().forEach(epic -> {
            epic.removeAllSubtaskIds();
            epic.setStatusNew();
        });
    }

    protected void updateEpicStatusBasedOnSubtasks(Integer epicId) {
        Epic epic = epicsMap.get(epicId);
        List<Subtask> subtasks = epic.getSubtasksId().stream()
                .map(subtasksMap::get)
                .collect(Collectors.toList());

        long countNew = subtasks.stream().filter(subtask -> Status.NEW.equals(subtask.getStatus())).count();
        long countDone = subtasks.stream().filter(subtask -> Status.DONE.equals(subtask.getStatus())).count();

        if (countNew == subtasks.size()) {
            epic.setStatus(Status.NEW);
        } else if (countDone == subtasks.size()) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

    protected void updateEpicEvaluation(Integer epicId) {
        Epic epic = epicsMap.get(epicId);

        if (epic.getSubtasksId().isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(Duration.ZERO);
            epic.setEndTime(null);
            return;
        }

        LocalDateTime earliestStartTime = null;
        LocalDateTime latestEndTime = null;
        Duration totalDuration = Duration.ZERO;

        for (Integer subtaskId : epic.getSubtasksId()) {
            Subtask subtask = subtasksMap.get(subtaskId);

            LocalDateTime subtaskEndTime = subtask.getEndTime();

            if (earliestStartTime == null || subtask.getStartTime().isBefore(earliestStartTime)) {
                earliestStartTime = subtask.getStartTime();
            }
            if (latestEndTime == null || subtaskEndTime.isAfter(latestEndTime)) {
                latestEndTime = subtaskEndTime;
            }

            totalDuration = totalDuration.plus(subtask.getDuration());
        }

        epic.setStartTime(earliestStartTime);
        epic.setDuration(totalDuration);
        epic.setEndTime(latestEndTime);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        TreeSet<Task> prioritizedTasks = new TreeSet<>();

        for (Task task : tasksMap.values()) {
            if (task.getStartTime() != null) {
                prioritizedTasks.add(task);
            }
        }

        for (Subtask subtask : subtasksMap.values()) {
            if (subtask.getStartTime() != null) {
                prioritizedTasks.add(subtask);
            }
        }

        return new ArrayList<>(prioritizedTasks);
    }

    private boolean hasTimeConflict(Task newTask) {
        LocalDateTime newTaskStart = newTask.getStartTime();
        LocalDateTime newTaskEnd = newTask.getEndTime();

        return getPrioritizedTasks().stream()
                .anyMatch(existingTask -> {
                    if (newTask.equals(existingTask)) {
                        return false;
                    }
                    LocalDateTime existingStart = existingTask.getStartTime();
                    LocalDateTime existingEnd = existingTask.getEndTime();

                    return newTaskStart.isBefore(existingEnd) && newTaskEnd.isAfter(existingStart);
                });
    }

}
