import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
private int nextId;
    HashMap<Integer, Task> tasks=new HashMap<>();
    HashMap<Integer, Epic> epics=new HashMap<>();
    HashMap<Integer, Subtask> subtask=new HashMap<>();
    public int generateNextId(){
        return nextId++;
    }
//    void createTask(Task task){
//tasks.put(task.getId(), task);
//    }
    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

   public List<Task> getAllTask(){
        return new ArrayList<>(tasks.values());
   }
    void deleteAllTask(){
        tasks.clear();
    }
    Task getTaskById(int id){
       return tasks.get(id) ;
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

}
//Методы для каждого из типов задач(Задача/Эпик/Подзадача):
//a. Получение списка всех задач.
//b. Удаление всех задач.
//c. Получение по идентификатору.
//d. Создание. Сам объект должен передаваться в качестве параметра. done
//e. Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
//f. Удаление по идентификатору.
//Дополнительные методы:
//a. Получение списка всех подзадач определённого эпика.
//Управление статусами осуществляется по следующему правилу:
//a. Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче.
//По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.