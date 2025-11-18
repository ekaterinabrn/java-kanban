package model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private LocalDateTime endTime;
    private final List<Integer> subtaskIds = new ArrayList<>();

    public List<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);
    }


    public Epic(String name, String description) {
        super(Status.NEW, name, description);
    }

    public Epic(Epic epic) {
        super(epic);
        this.endTime = epic.endTime;
        for (Integer subtaskId : epic.getSubtaskIds()) {
            this.subtaskIds.add(subtaskId);
        }
    }


    public void addSubtaskId(int subtaskId) {
        if (subtaskId != getId()) {
            subtaskIds.add(subtaskId);
        }
    }

    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove(Integer.valueOf(subtaskId));
    }

    public void deleteAllSubtasks() {
        subtaskIds.clear();
    }

    // сеттер для endTime
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "id=" + getId() +
                "subtaskIds=" + subtaskIds +
                ", status=" + getStatus() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }
}
