package tasks;

import tasks.enums.TaskStatuses;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks = new ArrayList<>();

    // Конструктор для создания нового эпика
    public Epic(String name, String description) {
        super(name, description);
    }

    // Конструктор для обновления эпика
    public Epic(Integer id, String name, String description, TaskStatuses status, ArrayList<Subtask> subtasks) {
        super(id, name, description, status);
        this.subtasks = subtasks;
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    @Override
    public void setStatus(TaskStatuses status) {
    }

    public void setSubtasks(Subtask subtask) {
        this.subtasks.add(subtask);
    }

    public void checkSubtaskStatusAndChangeEpicStatus() {
        int counterNew = 0;
        int counterInProgress = 0;
        int counterDone = 0;
        for (Subtask subtask : subtasks) {
            if (TaskStatuses.NEW.equals(subtask.getStatus())) {
                counterNew++;
            }
            if (TaskStatuses.IN_PROGRESS.equals(subtask.getStatus())) {
                counterInProgress++;
            }
            if (TaskStatuses.DONE.equals(subtask.getStatus())) {
                counterDone++;
            }
        }
        if (counterNew > 0 && counterInProgress == 0 && counterDone == 0) {
            super.setStatus(TaskStatuses.NEW);
        } else if (counterNew == 0 && counterInProgress == 0 && counterDone > 0) {
            super.setStatus(TaskStatuses.DONE);
        } else {
            super.setStatus(TaskStatuses.IN_PROGRESS);
        }
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtasksCount=" + subtasks.size() +
                '}';
    }

}
