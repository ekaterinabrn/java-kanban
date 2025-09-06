package model;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {


    private final List<Integer> subtaskIds = new ArrayList<>();

   public List<Integer> getSubtaskIds() {
       return new ArrayList<>(subtaskIds);
    } // не убрала дубль не хватает ума как тогда переделать в менеджере)) могу стереть из менеджера метод


    public Epic(String name, String description) {
        super(Status.NEW, name, description); //статус всегда нью
    }


    public void addSubtaskId(int subtaskId) {
        subtaskIds.add(subtaskId);

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
