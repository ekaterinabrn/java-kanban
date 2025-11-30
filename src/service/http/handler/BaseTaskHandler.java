package service.http.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;
import service.exception.NotFoundException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class BaseTaskHandler<T extends Task> extends BaseHttpHandler {
    protected final TaskManager taskManager;
    protected final Gson gson;

    public BaseTaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            System.out.println("Получен запрос: " + method + " " + path);

            switch (method) {
                case "GET":
                    handleGet(exchange, path);
                    break;
                case "POST":
                    handlePost(exchange);
                    break;
                case "DELETE":
                    handleDelete(exchange, path);
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (NotFoundException e) {
            System.out.println("Ошибка: " + e.getMessage());
            sendNotFound(exchange);
        } catch (RuntimeException e) {
            System.out.println("RuntimeException: " + e.getMessage());
            e.printStackTrace();
            if (e.getMessage() != null && e.getMessage().contains("overlaps")) {
                sendHasInteractions(exchange);
            } else {
                sendInternalError(exchange);
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            e.printStackTrace();
            sendInternalError(exchange);
        }
    }

    protected void handleGet(HttpExchange exchange, String path) throws IOException {
        String[] split = path.split("/");
        if (split.length == 3) {
            // получение по id
            int id = Integer.parseInt(split[2]);
            T task = getById(id);
            String taskJson = gson.toJson(task);
            sendResponse(200, taskJson, exchange);
        } else if (split.length == 2) {
            // получение всех
            List<T> allTasks = getAll();
            String allTasksJson = gson.toJson(allTasks);
            sendResponse(200, allTasksJson, exchange);
        } else {
            handleGetSpecial(exchange, path, split);
        }
    }

    protected void handleGetSpecial(HttpExchange exchange, String path, String[] split) throws IOException {
        sendNotFound(exchange);
    }

    protected void handlePost(HttpExchange exchange) throws IOException {
        InputStream inputStream = exchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        T taskFromJson = parseFromJson(body);
        if (taskFromJson.getId() == 0) {
            // Создание новой задачи
            T created = create(taskFromJson);
            if (created == null) {
                sendHasInteractions(exchange);
                return;
            }
            String createdJson = gson.toJson(created);
            sendResponse(201, createdJson, exchange);
        } else {
            // Обновление существующей задачи
            update(taskFromJson);
            T updated = getById(taskFromJson.getId());
            String updatedJson = gson.toJson(updated);
            sendResponse(201, updatedJson, exchange);
        }
    }

    protected void handleDelete(HttpExchange exchange, String path) throws IOException {
        String[] deleteSplit = path.split("/");
        if (deleteSplit.length == 3) {
            // удаление по id
            int deleteId = Integer.parseInt(deleteSplit[2]);
            deleteById(deleteId);
            sendResponse(200, "{}", exchange);
        } else if (deleteSplit.length == 2) {
            // удаление всех
            deleteAll();
            sendResponse(200, "{}", exchange);
        } else {
            sendNotFound(exchange);
        }
    }

    protected void sendResponse(int code, String response, HttpExchange exchange) throws IOException {
        sendText(exchange, response, code);
    }

    protected abstract T getById(int id);

    protected abstract List<T> getAll();

    protected abstract T create(T task);

    protected abstract void update(T task);

    protected abstract void deleteById(int id);

    protected abstract void deleteAll();

    protected abstract T parseFromJson(String json);
}

