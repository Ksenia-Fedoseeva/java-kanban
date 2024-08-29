package tasks;

import tasks.enums.Status;
import tasks.enums.TasksTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task> {
    private Integer id;
    private String name;
    private String description;
    private Status status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String description, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(Integer id, String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
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

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public static Task fromString(String value) {
        int idIndex = 0;
        int typeIndex = 1;
        int nameIndex = 2;
        int statusIndex = 3;
        int descriptionIndex = 4;
        int epicIdIndex = 5;
        int durationIndex = 6;
        int startTimeIndex = 7;

        String[] taskFields = value.split(",");
        TasksTypes type = TasksTypes.valueOf(taskFields[typeIndex]);

        Integer id = Integer.valueOf(taskFields[idIndex]);
        String name = taskFields[nameIndex];
        String description = taskFields[descriptionIndex];
        Status status = Status.valueOf(taskFields[statusIndex]);

        Duration duration = Duration.ofMinutes(Long.parseLong(taskFields[durationIndex]));
        LocalDateTime startTime = LocalDateTime.parse(taskFields[startTimeIndex]);

        if (TasksTypes.TASK.equals(type)) {
            return new Task(id, name, description, status, duration, startTime);
        } else if (TasksTypes.EPIC.equals(type)) {
            return new Epic(id, name, description, status, duration, startTime);
        } else {
            return new Subtask(id, name, description, status, Integer.valueOf(taskFields[epicIdIndex]), duration, startTime);
        }
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration.toMinutes());
    }

    @Override
    public int compareTo(Task otherTask) {
        return this.startTime.compareTo(otherTask.getStartTime());
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
        return id + "," + TasksTypes.TASK + "," + name + "," + status + "," + description + "," + "," + duration.toMinutes()
                + "," + startTime;
    }

}
