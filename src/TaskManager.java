import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private int nextId = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtask = new HashMap<>();

    public int generateNextId() {
        return nextId++;
    }


    public Task createTask(Task task) {
        task.setId(generateNextId());
        tasks.put(task.getId(), task);
        return task;
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public List<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    void deleteAllTask() {
        tasks.clear();
    }

    public void deleteTuskById(int id) {
        tasks.remove(id);
    }

    Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateNextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    Epic getEpicById(int id) {
        return epics.get(id);
    }


    public List<Epic> getAllEpic() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        for(Epic e:epics.values()){
            if(e!=null){e.deleteAllSubtasks();}
           
        }
        subtask.clear();
        epics.clear();
    }

    public void deleteEpicsById(int id) {
        Epic epic = epics.remove(id);
        List<Subtask> subtasks = epic.getSubtasks();
        for (Subtask s : subtasks) {
            subtask.remove(s.getId());
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);
            epic.updateStatus();
        }
    }

    public Subtask createSubtask(Subtask subtasks) {
        subtasks.setId(generateNextId());
        subtask.put(subtasks.getId(), subtasks);
        if (epics.containsKey(subtasks.getEpicId())) {
            epics.get(subtasks.getEpicId()).addSubtask(subtasks);
        }
        return subtasks;
    }

    public void updateSubtask(Subtask subtasks) {
if(subtask.containsKey(subtasks.getId())){
    subtask.put(subtasks.getId(),subtasks);
    Epic e=epics.get(subtasks.getEpicId());
    if(e!=null){
        e.updateSubtask(subtasks);
    }
}
    }

    Subtask getSubtaskById(int id) {
        return subtask.get(id);
    }

    public void deleteSubtaskById(int id) {
       Subtask sub=subtask.get(id);
        if (sub!=null) {
            subtask.remove(id);
        Epic e =epics.get(sub.getEpicId());
        if (e!=null){
            e.deleteSubtask(sub);
        }}
    }

    public void deleteAllSubTusk() {
        for (Epic e : epics.values()) {
            if (e != null) {
                e.deleteAllSubtasks();
                e.updateStatus();
            }
        }
        subtask.clear();
    }

    List<Subtask> getAllSubtask() {
        return new ArrayList<>(subtask.values());
    }

    List<Subtask> getEpicSubtask(int epicId) {
        Epic e = epics.get(epicId);
        if (e != null) {
            List<Subtask> containsInEpic = new ArrayList<>(e.getSubtasks());
            return containsInEpic;
        } else {
            return new ArrayList<>();  //    хочется бросить ошибку или исклбчение
        }

    }


}


