package tasks;

import tasks.enums.TaskStatuses;

public class Subtask extends Task {
    private Epic epic;

    // Конструктор для создания новой подзадачи
    public Subtask(String name, String description, Epic epic) {
        super(name, description);
        this.epic = epic;
        this.epic.setSubtasks(this);
    }

    // Конструктор для обновления подзадачи
    public Subtask(Integer id, String name, String description, TaskStatuses status, Epic epic) {
        super(id, name, description, status);
        this.epic = epic;
        this.epic.setSubtasks(this);
    }

    @Override
    public void setStatus(TaskStatuses status) {
        super.setStatus(status);
        epic.checkSubtaskStatusAndChangeEpicStatus();
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + (epic != null ? epic.getId() : "No Epic") +
                '}';
    }

}
