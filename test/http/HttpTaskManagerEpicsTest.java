package http;

import com.google.gson.Gson;
import model.Epic;
import model.Status;
import model.SubTask;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class HttpTaskManagerEpicsTest {

    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer httpTaskServer;
    Gson gson = Managers.getGson();
    Epic epic;
    SubTask subtask;
    HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    public void setUp() throws IOException {
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        epic = new Epic(1, Type.EPIC, "Epic", "Description Epic", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 0), Duration.ofMinutes(15),
                new ArrayList<>());
        subtask = new SubTask(2, Type.SUBTASK, "Subtask Epic", "Description Sub Ep",
                Status.NEW, LocalDateTime.of(2025, 1, 1, 1, 0),
                Duration.ofMinutes(30), 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);

    }

    @AfterEach
    public void shutDown() {
        taskManager.deleteAllEpic();
        taskManager.deleteAllSubTask();
        client.close();
        httpTaskServer.stop();
    }

    // Получение эпика по id
    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics/1"))
                .GET().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getEpic(1)), response.body(), "Эпики не совпадают");

    }

    // Получение несуществующего эпика
    @Test
    public void testGetNonExistentEpic() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics/3"))
                .GET().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

    }

    // Получение списка эпиков
    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics"))
                .GET().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getListOfEpics()), response.body(), "Списки эпиков не совпадают");

    }

    // Получение списка подзадач эпика
    @Test
    public void testGetListSubtasksOfEpic() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics/1/subtasks"))
                .GET().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getListOfSubtaskOfEpic(taskManager.getEpic(1))),
                response.body(), "Списки эпиков не совпадают");

    }

    // Добавление эпика
    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        Epic newEpic = new Epic(3, Type.EPIC, "newEpic", "Description newEpic", Status.NEW,
                LocalDateTime.of(2025, 1, 2, 1, 0), Duration.ofMinutes(15),
                new ArrayList<>());

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newEpic))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Epic> tasksFromManager = taskManager.getListOfEpics();

        assertEquals(201, response.statusCode());
        assertEquals(2, tasksFromManager.size(), "Некорректное количество эпиков");
        assertEquals(newEpic, tasksFromManager.getLast(), "Эпики не совпадают");

    }

    // Удаление эпика
    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/epics/1"))
                .DELETE().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNull(taskManager.getEpic(1), "Задача должна быть удалена");

    }

}
