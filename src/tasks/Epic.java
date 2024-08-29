package tasks;

import tasks.enums.Status;
import tasks.enums.TasksTypes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, Duration.ZERO, null);
    }

    public Epic(Integer id, String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        super(id, name, description, status, duration, startTime);
        this.endTime = startTime.plusMinutes(duration.toMinutes());
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public void setStatus(Status status) {
        super.setStatus(status);
    }

    public void setStatusNew() {
        super.setStatus(Status.NEW);
    }

    public void addSubtasksId(Integer subtaskId) {
        this.subtasksId.add(subtaskId);
    }

    public void removeSubtaskId(Integer subtaskId) {
        this.subtasksId.remove(subtaskId);
    }

    public void removeAllSubtaskIds() {
        this.subtasksId.clear();
    }

    @Override
    public String toString() {
        return getId() + "," + TasksTypes.EPIC + "," + getName() + "," + getStatus() + "," + getDescription() + "," + ","
                + getDuration().toMinutes() + "," + getStartTime();
    }

}
