package tasks;

import tasks.enums.Status;
import tasks.enums.TasksTypes;

import java.util.Objects;

public class Task {
    private Integer id;
    private String name;
    private String description;
    private Status status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
    }

    public Task(Integer id, String name, String description, Status status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static Task fromString(String value) {
        int idIndex = 0;
        int typeIndex = 1;
        int nameIndex = 2;
        int statusIndex = 3;
        int descriptionIndex = 4;
        int epicIdIndex = 5;

        String[] taskFields = value.split(",");
        TasksTypes type = TasksTypes.valueOf(taskFields[typeIndex]);

        Integer id = Integer.valueOf(taskFields[idIndex]);
        String name = taskFields[nameIndex];
        String description = taskFields[descriptionIndex];
        Status status = Status.valueOf(taskFields[statusIndex]);

        if (TasksTypes.TASK.equals(type)) {
            return new Task(id, name, description, status);
        } else if (TasksTypes.EPIC.equals(type)) {
            return new Epic(id, name, description, status);
        } else {
            return new Subtask(id, name, description, status, Integer.valueOf(taskFields[epicIdIndex]));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + "," + TasksTypes.TASK + "," + name + "," + status + "," + description + ",";
    }

}
