package model;

public class Subtask extends Task {

    private int epicId;

    public Subtask(Status status, String name, String description, int epicId) {
        super(status, name, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }


    @Override
    public String toString() {
        return "model.Subtask{" +
                "id=" + getId() +
                ", status=" + getStatus() +
                ", name='" + getName() + '\'' +
                "description='" + getDescription() + '\'' +
                ", epicId=" + getEpicId() +
                '}';
    }
}
