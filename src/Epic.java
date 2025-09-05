import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {


    //private final List<Integer> subtaskId = new ArrayList<>();
    private List<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(Status.NEW, name, description); //статус всегда нью
    }

   public List<Integer> getSubtaskId() {
       List<Integer> subtaskId = new ArrayList<>();
       for(Subtask s : subtasks){
           subtaskId.add(s.getId());
       }

       return subtaskId;
       }

   public void addSubtask(Subtask s) {
       subtasks.add(s);
   }

   public void deleteSubtask(Subtask s) {
       subtasks.remove(s);
   }


//    если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
//    если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
//    во всех остальных случаях статус должен быть IN_PROGRESS.
    Status getEpicStatus() {
        if (subtasks.isEmpty()) {
            return Status.NEW;
        }

        for (Subtask s : subtasks) {
            if (s.getStatus() != Status.DONE) {
                return Status.IN_PROGRESS;
            }
        }
        return Status.DONE;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                "subtaskId=" + getSubtaskId() +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
