package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {


    private final List<Integer> subtaskIds = new ArrayList<>();

    public List<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);
    }


    public Epic(String name, String description) {
        super(Status.NEW, name, description); //статус всегда нью
    }

    public Epic(Epic epic) {
        super(epic.getStatus(), epic.getName(), epic.getDescription());
        setId(epic.getId());
        for (Integer subtaskId : epic.getSubtaskIds()) {
            this.subtaskIds.add(subtaskId);
        }
    }


    public void addSubtaskId(int subtaskId) {
        //  эпик не добавляет сам себя в подзадачи а то тест падал
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
}
