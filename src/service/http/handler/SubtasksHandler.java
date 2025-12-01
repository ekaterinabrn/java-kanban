package service.http.handler;

//  обработчик  для работы с подзадачами (GET /subtasks, POST /subtasks, DELETE /subtasks)
import com.google.gson.Gson;
import model.Subtask;
import service.TaskManager;

import java.util.List;

public class SubtasksHandler extends BaseTaskHandler<Subtask> {
    public SubtasksHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected Subtask getById(int id) {
        return taskManager.getSubtaskById(id);
    }

    @Override
    protected List<Subtask> getAll() {
        return taskManager.getAllSubtask();
    }

    @Override
    protected Subtask create(Subtask subtask) {
        return taskManager.createSubtask(subtask);
    }

    @Override
    protected void update(Subtask subtask) {
        taskManager.updateSubtask(subtask);
    }

    @Override
    protected void deleteById(int id) {
        taskManager.deleteSubtaskById(id);
    }

    @Override
    protected void deleteAll() {
        taskManager.deleteAllSubtask();
    }

    @Override
    protected Subtask parseFromJson(String json) {
        return gson.fromJson(json, Subtask.class);
    }
}

