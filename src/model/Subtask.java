package model;

public class Subtask extends Task {

    private int epicId;

    public Subtask(Status status, String name, String description, int epicId) {
        super(status, name, description);
        this.epicId = epicId;
    }

    public Subtask(Subtask subtask) {
        super(subtask.getStatus(), subtask.getName(), subtask.getDescription());
        setId(subtask.getId());
        this.epicId = subtask.getEpicId();
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        // Проверяем, что подзадача не ссылается сама на себя
        if (epicId != getId()) {
            this.epicId = epicId;
        }
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
//тип подзадача

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }
}