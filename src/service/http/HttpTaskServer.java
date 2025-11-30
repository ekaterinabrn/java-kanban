package service.http;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.InMemoryTaskManager;
import service.TaskManager;
import service.http.handler.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private HttpServer server;
    private final TaskManager taskManager;
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();

    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public static Gson getGson() {
        return gson;
    }

    public void start() throws IOException {
        server = HttpServer.create();
        InetSocketAddress address = new InetSocketAddress(PORT);
        server.bind(address, 0);

        TaskHandler taskHandler = new TaskHandler(taskManager, gson);
        server.createContext("/tasks", taskHandler);

        EpicsHandler epicsHandler = new EpicsHandler(taskManager, gson);
        server.createContext("/epics", epicsHandler);

        SubtasksHandler subtasksHandler = new SubtasksHandler(taskManager, gson);
        server.createContext("/subtasks", subtasksHandler);

        HistoryHandler historyHandler = new HistoryHandler(taskManager, gson);
        server.createContext("/history", historyHandler);

        PrioritizedHandler prioritizedHandler = new PrioritizedHandler(taskManager, gson);
        server.createContext("/prioritized", prioritizedHandler);

        server.start();
        System.out.println("Сервер запущен, порт:  " + PORT);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
            System.out.println("Сервер был остановлен");
        }
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = new InMemoryTaskManager();
        
        Task task1 = new Task(Status.NEW, "Задача 1", "Описание задачи 1");
        task1.setStartTime(LocalDateTime.now().plusHours(1));
        task1.setDuration(Duration.ofMinutes(30));
        Task createdTask1 = taskManager.createTask(task1);
        
        Task task2 = new Task(Status.IN_PROGRESS, "Задача 2", "Описание задачи 2");
        task2.setStartTime(LocalDateTime.now().plusHours(2));
        task2.setDuration(Duration.ofMinutes(45));
        Task createdTask2 = taskManager.createTask(task2);
        
        Epic epic1 = new Epic("Эпик 1", "Описание эпика 1");
        Epic createdEpic1 = taskManager.createEpic(epic1);
        
        Subtask subtask1 = new Subtask(Status.NEW, "Подзадача 1", "Описание подзадачи 1", createdEpic1.getId());
        subtask1.setStartTime(LocalDateTime.now().plusHours(3));
        subtask1.setDuration(Duration.ofMinutes(20));
        taskManager.createSubtask(subtask1);
        
        Subtask subtask2 = new Subtask(Status.DONE, "Подзадача 2", "Описание подзадачи 2", createdEpic1.getId());
        subtask2.setStartTime(LocalDateTime.now().plusHours(4));
        subtask2.setDuration(Duration.ofMinutes(25));
        taskManager.createSubtask(subtask2);
        
        taskManager.getTaskById(createdTask1.getId());
        taskManager.getEpicById(createdEpic1.getId());
        taskManager.getTaskById(createdTask2.getId());
        
        System.out.println("Создано тестовых данных:");
        System.out.println("- Задач: 2 (ID: " + createdTask1.getId() + ", " + createdTask2.getId() + ")");
        System.out.println("- Эпиков: 1 (ID: " + createdEpic1.getId() + ")");
        System.out.println("- Подзадач: 2 (для эпика " + createdEpic1.getId() + ")");
        System.out.println("- История просмотров: 3 задачи");
        
        HttpTaskServer server = new HttpTaskServer(taskManager);
        server.start();
    }
}

