package service;

import model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    //удаление задачи из истории по id
    void remove(int id);

    List<Task> getHistory();
}
