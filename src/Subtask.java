public class Subtask extends Task{

    private final int epicId;

    public Subtask(int id, Status status, String name, String description, int epicId) {
        super(id, status, name, description);
        this.epicId = epicId;
    }
    public int getEpicId() {
        return epicId;
    }


}
