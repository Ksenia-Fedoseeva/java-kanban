package tasks;

import tasks.enums.TaskStatuses;

import java.util.Objects;

public class Task {
    public static Integer counterId = 1;
    private final Integer id;
    private String name;
    private String description;
    private TaskStatuses status;

    // Конструктор для создания новой задачи
    public Task(String name, String description) {
        this.id = counterId;
        counterId++;
        this.name = name;
        this.description = description;
        this.status = TaskStatuses.NEW;
    }

    // Конструктор для обновления задачи
    public Task(Integer id, String name, String description, TaskStatuses status) {
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

    public TaskStatuses getStatus() {
        return status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(TaskStatuses status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
