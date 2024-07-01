package tasks;

import tasks.enums.Status;
import tasks.enums.TasksTypes;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(Integer id, String name, String description, Status status) {
        super(id, name, description, status);
    }

    public ArrayList<Integer> getSubtasksId() {
        return subtasksId;
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
        return getId() + "," + TasksTypes.EPIC + "," + getName() + "," + getStatus() + "," + getDescription() + ",";
    }

}
