package javacourse;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

public class Main {
    /*ВООБЩЕ ЕСЛИ Я ВЕРНО ПОНЯЛА:  с мейн - это было ДОП задание, не основное, вот вставка из тз:
Дополнительное задание. Реализуем пользовательский сценарий
Если у вас останется время, вы можете выполнить дополнительное задание. Реализуйте в классе Main опциональный пользовательский сценарий:
Создайте две задачи, эпик с тремя подзадачами и эпик без подзадач.
Запросите созданные задачи несколько раз в разном порядке.
После каждого запроса выведите историю и убедитесь, что в ней нет повторов.
Удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться.
Удалите эпик с тремя подзадачами и убедитесь, что из истории удалился как сам эпик, так и все его подзадачи.
Обратите внимание, что выполнение этого задания необязательно.  --!!!Посмотрим на эту строку
*/
    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        // тз: Создайте две задачи-создание двух задач
        Task task1 = new Task(Status.NEW, "Задача 1", "Описание задачи 1");
        Task task2 = new Task(Status.NEW, "Задача 2", "Описание задачи 2");

        Task createdTask1 = manager.createTask(task1);
        Task createdTask2 = manager.createTask(task2);

        //тз: эпик с тремя подзадачами- создание эпика с тремя подзадачами
        Epic epic1 = new Epic("Эпик 1", "Эпик с тремя подзадачами");
        Epic createdEpic1 = manager.createEpic(epic1);

        Subtask subtask1 = new Subtask(Status.NEW, "Подзадача 1", "Описание подзадачи 1", createdEpic1.getId());
        Subtask subtask2 = new Subtask(Status.NEW, "Подзадача 2", "Описание подзадачи 2", createdEpic1.getId());
        Subtask subtask3 = new Subtask(Status.NEW, "Подзадача 3", "Описание подзадачи 3", createdEpic1.getId());

        Subtask createdSubtask1 = manager.createSubtask(subtask1);
        Subtask createdSubtask2 = manager.createSubtask(subtask2);
        Subtask createdSubtask3 = manager.createSubtask(subtask3);

        // создание  эпика без подзадач
        Epic epic2 = new Epic("Эпик 2", "Эпик без подзадач");
        Epic createdEpic2 = manager.createEpic(epic2);

        System.out.println("=== Просмотр задач для формирования истории ===");
        
        // тз:  Запросите созданные задачи несколько раз в разном порядке.- Запросы в разном порядке с повторными запросами
        manager.getTaskById(createdTask1.getId());
        manager.getEpicById(createdEpic1.getId());
        manager.getSubtaskById(createdSubtask1.getId());
        manager.getTaskById(createdTask2.getId());
        manager.getSubtaskById(createdSubtask2.getId());
        manager.getEpicById(createdEpic2.getId());
        manager.getSubtaskById(createdSubtask3.getId());
        

        manager.getTaskById(createdTask1.getId());
        manager.getEpicById(createdEpic1.getId());
        manager.getSubtaskById(createdSubtask1.getId());

        System.out.println("История после всех запросов");
        printAllTasks(manager);

        // Удаление задачи из истории
        System.out.println("Удаление задачи  из истории");
        manager.deleteTaskById(createdTask1.getId());
        System.out.println("История после удаления задачи");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }

        // Удаление эпика с подзадачами из истории
        System.out.println("Удаление эпика  с подзадачами из истории ");
        manager.deleteEpicById(createdEpic1.getId());
        System.out.println("История после удаления эпика и его подзадач:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    private static void printAllTasks(TaskManager manager) {

        System.out.println("История просмотров:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
