package http;


import com.sun.net.httpserver.HttpServer;
import model.*;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class HttpTaskServer {

    public static final int PORT = 8080;
    private static final String URL = "http://localhost:";
    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TaskHandler(taskManager));
        httpServer.createContext("/subtasks", new SubTaskHandler(taskManager));
        httpServer.createContext("/epics", new EpicHandler(taskManager));
        httpServer.createContext("/history", new HistoryHandler(taskManager));
        httpServer.createContext("/prioritized", new PriorityHandler(taskManager));

    }

    public void start() {
        System.out.println("Started HttpTaskServer " + PORT);
        System.out.println(URL + PORT);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановили сервер на порту " + PORT);
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);

        Task task = new Task(1, Type.TASK, "Task", "Task description", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 1, 0), Duration.ofMinutes(15));
        Task task4 = new Task(4, Type.TASK, "Task4", "Task4 description", Status.NEW,
                LocalDateTime.of(2025, 5, 10, 1, 0), Duration.ofMinutes(15));

        Epic epic = new Epic(2, Type.EPIC, "Epic", "Description Epic", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 12, 0), Duration.ofMinutes(20),
                new ArrayList<>());

        SubTask subTask = new SubTask(3, Type.SUBTASK, "Subtask Epic", "Description Sub Ep",
                Status.NEW, LocalDateTime.of(2025, 5, 15, 12, 53),
                Duration.ofMinutes(30), 2);
        SubTask subTask5 = new SubTask(5, Type.SUBTASK, "Subtask Epic", "Description Sub Ep",
                Status.NEW, LocalDateTime.of(2025, 5, 16, 12, 53),
                Duration.ofMinutes(30), 2);

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);
        taskManager.createSubTask(subTask5);
        taskManager.createTask(task4);
        httpTaskServer.start();

        //httpTaskServer.stop();
    }

}
