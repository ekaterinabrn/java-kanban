package service.http.handler;

// обработчик для работы с задачами (GET /tasks, POST /tasks, DELETE /tasks)
import com.google.gson.Gson;
import model.Task;
import service.TaskManager;

import java.util.List;

public class TaskHandler extends BaseTaskHandler<Task> {
    public TaskHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected Task getById(int id) {
        return taskManager.getTaskById(id);
    }

    @Override
    protected List<Task> getAll() {
        return taskManager.getAllTask();
    }

    @Override
    protected Task create(Task task) {
        return taskManager.createTask(task);
    }

    @Override
    protected void update(Task task) {
        taskManager.updateTask(task);
    }

    @Override
    protected void deleteById(int id) {
        taskManager.deleteTaskById(id);
    }

    @Override
    protected void deleteAll() {
        taskManager.deleteAllTask();
    }

    @Override
    protected Task parseFromJson(String json) {
        return gson.fromJson(json, Task.class);
    }
}

