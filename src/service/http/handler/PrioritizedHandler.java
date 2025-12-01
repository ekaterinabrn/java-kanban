package service.http.handler;

//  обработчик для получения приоритетных задач
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.util.List;

public class PrioritizedHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            switch (method) {
                case "GET":
                    List<Task> prioritized = taskManager.getPrioritizedTasks();
                    String prioritizedJson = gson.toJson(prioritized);
                    sendResponse(200, prioritizedJson, exchange);
                    break;
                default:
                    sendNotFound(exchange);
            }
        } catch (Exception e) {
            sendInternalError(exchange);
        }
    }

    private void sendResponse(int code, String response, HttpExchange exchange) throws IOException {
        sendText(exchange, response, code);
    }
}

