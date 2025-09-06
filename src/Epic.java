import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(Status.NEW, name, description); //статус всегда нью
    }
public List<Subtask> getSubtasks(){return subtasks;}
   public List<Integer> getSubtaskId() {
       List<Integer> subtaskId = new ArrayList<>();
       for(Subtask s : subtasks){
           subtaskId.add(s.getId());
       }

       return subtaskId;
       }

   public void addSubtask(Subtask s) {
       subtasks.add(s);
       updateStatus();
   }

   public void deleteSubtask(Subtask s) {
       subtasks.remove(s);
       updateStatus();
   }
    public void deleteAllSubtasks() {
        subtasks.clear();  // Очищает список субтасок эпика
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
    public void updateStatus() {
        this.setStatus(getEpicStatus());
    }
    public void updateSubtask(Subtask subtasks) {
        for (int i = 0; i < this.subtasks.size(); i++) {
            if (this.subtasks.get(i).getId() == subtasks.getId()) {
                this.subtasks.set(i, subtasks);
                updateStatus();
                return;
            }
        }}

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                "subtaskId=" + getSubtaskId() +
                ", status=" + getStatus() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }
}
