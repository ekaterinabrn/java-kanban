package service;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    // Методы для работы с тасками
    Task createTask(Task newTask);

    void updateTask(Task updateTask);

    List<Task> getAllTask();

    void deleteAllTask();

    void deleteTaskById(int id);

    Task getTaskById(int id);

    // Методы для работы с эпиками
    Epic createEpic(Epic newEpic);

    Epic getEpicById(int id);

    List<Epic> getAllEpics();

    void deleteAllEpics();

    void deleteEpicById(int id);

    void updateEpic(Epic epic);

    // Методы для работы с сабтаск
    Subtask createSubtask(Subtask subtasks);

    void updateSubtask(Subtask subtasks);

    Subtask getSubtaskById(int id);

    void deleteSubtaskById(int id);

    void deleteAllSubtask();

    List<Subtask> getAllSubtask();

    List<Subtask> getEpicSubtask(int epicId);

    //  получение истории просмотров
    List<Task> getHistory();

    // получение задач в порядке приоритета
    List<Task> getPrioritizedTasks();
}