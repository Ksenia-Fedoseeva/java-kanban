package tasks;

import tasks.enums.Status;
import tasks.enums.TasksTypes;

public class Subtask extends Task {
    private Integer epicId;

    // Конструктор для создания новой подзадачи
    public Subtask(String name, String description, Integer epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    // Конструктор для обновления подзадачи
    public Subtask(Integer id, String name, String description, Status status, Integer epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return getId() + "," + TasksTypes.SUBTASK + "," + getName() + "," + getStatus() + "," + getDescription() + ","
                + getEpicId();
    }

}
