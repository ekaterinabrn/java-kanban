package service.http.handler;


import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler implements HttpHandler {
    protected void sendText(HttpExchange h, String text, int statusCode) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        h.sendResponseHeaders(statusCode, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        sendText(h, text, 200);
    }

    protected void sendNotFound(HttpExchange h) throws IOException {
        String response = "{\"message\":\"Not Found\"}";
        sendText(h, response, 404);
    }

    protected void sendHasInteractions(HttpExchange h) throws IOException {
        String response = "{\"message\":\"Имеется пересечение с существующими задачами\"}";
        sendText(h, response, 406);
    }

    protected void sendInternalError(HttpExchange h) throws IOException {
        String response = "{\"message\":\"Internal Server Error\"}";
        sendText(h, response, 500);
    }

    protected void sendCreated(HttpExchange h, String text) throws IOException {
        sendText(h, text, 201);
    }
}

