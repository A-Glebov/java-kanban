package http;

import com.google.gson.Gson;
import model.Status;
import model.Task;
import model.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpTaskManagerHistoryTest {

    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer httpTaskServer;
    Gson gson = Managers.getGson();
    HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    public void setUp() throws IOException {
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        Task task1 = new Task(1, Type.TASK, "Task1", "Task description1", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 0), Duration.ofMinutes(15));
        Task task2 = new Task(2, Type.TASK, "Task2", "Task description2", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 2, 0), Duration.ofMinutes(15));
        Task task3 = new Task(3, Type.TASK, "Task3", "Task description3", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 3, 0), Duration.ofMinutes(15));

        taskManager.createTask(task3);
        taskManager.createTask(task2);
        taskManager.createTask(task1);

    }

    // Получение списка задач по приоритету
    @Test
    public void getHistory() throws IOException, InterruptedException {
        HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/1"))
                .GET().build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());

        HttpRequest request2 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/2"))
                .GET().build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        HttpRequest request3 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/3"))
                .GET().build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response1.statusCode());
        assertEquals(200, response2.statusCode());
        assertEquals(200, response3.statusCode());

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/history"))
                .GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getHistory()), response.body(), "Списки задач не совпадают");

    }

    @AfterEach
    public void shutDown() {
        taskManager.deleteAllTasks();
        client.close();
        httpTaskServer.stop();
    }

}
