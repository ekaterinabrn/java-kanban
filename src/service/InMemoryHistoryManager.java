package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();
    private static final int MAX_SIZE = 10;


    // Если задача уже есть в истории, она удаляется и добавляется в конец
    @Override
    public void add(Task task) {
        if (task != null) {
            history.removeIf(t -> t.getId() == task.getId()); // удаляем дубликат если есть
            history.add(task);
            if (history.size() > MAX_SIZE) {
                history.remove(0);
            }
        }
    }


    @Override
    public void remove(int id) {
        history.removeIf(task -> task.getId() == id);
    }

    @Override
    public List<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
