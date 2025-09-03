import java.util.HashMap;

public class TaskManager {

    HashMap<Integer, Task> tasks=new HashMap<>();
    HashMap<Integer, Epic> epics=new HashMap<>();
    HashMap<Integer, Subtask> subtask=new HashMap<>();
    void deleteTaskById(Integer id){

    }
    void createTask(Task task){
tasks.put(task.getId(), task);
    }
    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }
    void printAllTask(){
        for(Integer id:tasks.keySet()){

        }
    }
    void generateId(){

    }
}
