package javacourse;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.Managers;
import service.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        // Создание задач
        Task task1 = new Task(Status.NEW, "Задача 1", "Описание задачи 1");
        Task task2 = new Task(Status.NEW, "Задача 2", "Описание задачи 2");

        Task createdTask1 = manager.createTask(task1);
        Task createdTask2 = manager.createTask(task2);

        // Создание эпика
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic createdEpic1 = manager.createEpic(epic1);

        // Создание подзадач
        Subtask subtask1 = new Subtask(Status.NEW, "Подзадача 1", "Описание подзадачи 1", createdEpic1.getId());
        Subtask subtask2 = new Subtask(Status.NEW, "Подзадача 2", "Описание подзадачи 2", createdEpic1.getId());

        Subtask createdSubtask1 = manager.createSubtask(subtask1);
        Subtask createdSubtask2 = manager.createSubtask(subtask2);


        System.out.println("Просмотр задач для формирования истории");
        manager.getTaskById(createdTask1.getId());
        manager.getEpicById(createdEpic1.getId());
        manager.getSubtaskById(createdSubtask1.getId());
        manager.getTaskById(createdTask2.getId());
        manager.getSubtaskById(createdSubtask2.getId());

        // Вывод всех задач
        printAllTasks(manager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Печатаем все задачи");
        System.out.println("Задачи:");
        for (Task task : manager.getAllTask()) {
            System.out.println(task);
        }

        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);
            for (Task task : manager.getEpicSubtask(epic.getId())) {
                System.out.println(" " + task);
            }
        }

        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtask()) {
            System.out.println(subtask);
        }

        System.out.println("История просмотров:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
