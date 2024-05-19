package tasks;

import tasks.enums.Status;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();

    // Конструктор для создания нового эпика
    public Epic(String name, String description) {
        super(name, description);
    }

    // Конструктор для обновления эпика
    public Epic(Integer id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    @Override
    public void setStatus(Status status) {
    }

    public void addSubtasksId(Integer subtaskId) {
        this.subtasksId.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        this.subtasksId.remove(subtaskId);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtasksId" + subtasksId +
                '}';
    }

}
