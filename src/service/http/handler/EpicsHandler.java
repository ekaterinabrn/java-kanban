package service.http.handler;

// обработчик запросов для работы с эпиками (GET /epics, POST /epics, DELETE /epics, GET /epics/{id}/subtasks)
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.Subtask;
import service.TaskManager;

import java.io.IOException;
import java.util.List;

public class EpicsHandler extends BaseTaskHandler<Epic> {
    public EpicsHandler(TaskManager taskManager, Gson gson) {
        super(taskManager, gson);
    }

    @Override
    protected void handleGetSpecial(HttpExchange exchange, String path, String[] split) throws IOException {
        if (split.length == 4 && split[3].equals("subtasks")) {
            // получение подзадач эпика
            int epicId = Integer.parseInt(split[2]);
            List<Subtask> subtasks = taskManager.getEpicSubtask(epicId);
            String subtasksJson = gson.toJson(subtasks);
            sendResponse(200, subtasksJson, exchange);
        } else {
            sendNotFound(exchange);
        }
    }

    @Override
    protected Epic getById(int id) {
        return taskManager.getEpicById(id);
    }

    @Override
    protected List<Epic> getAll() {
        return taskManager.getAllEpics();
    }

    @Override
    protected Epic create(Epic epic) {
        return taskManager.createEpic(epic);
    }

    @Override
    protected void update(Epic epic) {
        taskManager.updateEpic(epic);
    }

    @Override
    protected void deleteById(int id) {
        taskManager.deleteEpicById(id);
    }

    @Override
    protected void deleteAll() {
        taskManager.deleteAllEpics();
    }

    @Override
    protected Epic parseFromJson(String json) {
        return gson.fromJson(json, Epic.class);
    }
}

