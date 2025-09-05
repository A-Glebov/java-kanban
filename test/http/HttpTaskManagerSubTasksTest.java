package http;

import com.google.gson.Gson;
import model.*;
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

public class HttpTaskManagerSubTasksTest {

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

    // Получение подзадачи по id
    @Test
    public void testGetSubTask() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks/2"))
                .GET().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getSubTask(2)), response.body(), "Подзадачи не совпадают");

    }

    // Получение несуществующей подзадачи
    @Test
    public void testGetNonExistentSubTask() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks/3"))
                .GET().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

    }

    // Получение списка подзадач
    @Test
    public void testGetSubTasks() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks"))
                .GET().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getListOfSubTask()), response.body(), "Списки подзадач не совпадают");

    }

    // Добавление задачи
    @Test
    public void testAddSubTask() throws IOException, InterruptedException {
        SubTask newSubtask = new SubTask(3, Type.SUBTASK, "newSubtask", "Description newSub",
                Status.NEW, LocalDateTime.of(2025, 1, 1, 2, 0),
                Duration.ofMinutes(30), 1);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newSubtask))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<SubTask> tasksFromManager = taskManager.getListOfSubTask();

        assertEquals(200, response.statusCode());
        assertEquals(2, tasksFromManager.size(), "Некорректное количество подзадач");
        assertEquals(newSubtask, tasksFromManager.getLast(), "Подзадачи не совпадают");

    }

    // Добавление подзадачи пересекающейся во времени
    @Test
    public void testAddInteractionSubTask() throws IOException, InterruptedException {
        SubTask interactionSubtask = new SubTask(3, Type.SUBTASK, "InteractionSub", "Desc SubEp",
                Status.NEW, LocalDateTime.of(2025, 1, 1, 1, 0),
                Duration.ofMinutes(30), 1);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(interactionSubtask))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<SubTask> tasksFromManager = taskManager.getListOfSubTask();

        assertEquals(406, response.statusCode());
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(subtask, tasksFromManager.getLast(), "Задача не должна быть добавлена");

    }

    // Обновление подзадачи
    @Test
    public void testUpdateSubTask() throws IOException, InterruptedException {
        SubTask updateSubtask = new SubTask(2, Type.SUBTASK, "updateSubtask", "Desc updateSub",
                Status.NEW, LocalDateTime.of(2025, 1, 1, 1, 0),
                Duration.ofMinutes(30), 1);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks/2"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(updateSubtask))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<SubTask> tasksFromManager = taskManager.getListOfSubTask();

        assertEquals(200, response.statusCode());
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(updateSubtask, tasksFromManager.getLast(), "Подзадача не обновилась");

    }

    // Обновление несуществующей подзадачи
    @Test
    public void testUpdateNonExistentSubTask() throws IOException, InterruptedException {
        SubTask subtask = new SubTask(3, Type.SUBTASK, "Subtask", "Desc Sub",
                Status.NEW, LocalDateTime.of(2025, 1, 1, 1, 0),
                Duration.ofMinutes(30), 1);

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks/3"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subtask))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<SubTask> tasksFromManager = taskManager.getListOfSubTask();

        assertEquals(404, response.statusCode());
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertNull(taskManager.getTask(2), "Задача не должна быть обновлена или добавлена");

    }

    // Удаление задачи
    @Test
    public void testDeleteSubTask() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks/2"))
                .DELETE().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNull(taskManager.getTask(2), "Задача должна быть удалена");

    }

    // Удаление несуществующей задачи
    @Test
    public void testDeleteNonExistingTask() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/subtasks/3"))
                .DELETE().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

    }

}

