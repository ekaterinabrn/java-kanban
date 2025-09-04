import java.util.ArrayList;
import java.util.List;

public class Epic extends Task{


    private final List<Integer> subtaskId = new ArrayList<>();
    public Epic(  String name, String description) {
        super( Status.NEW, name, description); //статус всегда нью
    }
    public List<Integer> getSubtaskId() {
        return subtaskId;
    }
    public void addSubtask(int subTaskId){
        subtaskId.add(subTaskId);
    }
    public void deleteSubtask(int subTuskId){
        subtaskId.remove(Integer.valueOf(subTuskId));
    }

//    Для эпиков:
//    если у эпика нет подзадач или все они имеют статус NEW, то статус должен быть NEW.
//    если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
//    во всех остальных случаях статус должен быть IN_PROGRESS.
   void updateEpicStatus(List<Subtask> subtasks){
        if (subtasks.isEmpty()){
            setStatus(Status.NEW);
            return;}
            boolean isAllNewSubtusk=true;
            boolean isAllDone=true;
        }


    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                "subtaskId=" + subtaskId +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
