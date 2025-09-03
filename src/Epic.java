import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{
    private final List<Integer> subtaskId = new ArrayList<>();
    public Epic(int id, Status status, String name, String description) {
        super(id, status, name, description);
    }

    Epic createEpic(Epic newEpic){}
}
