package http;

import com.google.gson.Gson;
import model.Status;
import model.Task;
import model.Type;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class HttpTaskManagerTasksTest {

    TaskManager taskManager = Managers.getDefault();
    HttpTaskServer httpTaskServer;
    Gson gson = Managers.getGson();
    Task task;
    HttpClient client = HttpClient.newHttpClient();

    @BeforeEach
    public void setUp() throws IOException {
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

        task = new Task(1, Type.TASK, "Task", "Task description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 0), Duration.ofMinutes(15));

        taskManager.createTask(task);

    }

    @AfterEach
    public void shutDown() {
        taskManager.deleteAllTasks();
        client.close();
        httpTaskServer.stop();
    }

    // Получение задачи по id
    @Test
    public void testGetTask() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/1"))
                .GET().build();

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getTask(1)), response.body(), "Задачи не совпадают");

    }

    // Получение несуществующей задачи
    @Test
    public void testGetNonExistentTask() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/2"))
                .GET().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

    }

    // Получение списка задач
    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks"))
                .GET().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertEquals(gson.toJson(taskManager.getListOfTasks()), response.body(), "Списки задач не совпадают");

    }

    // Добавление задачи
    @Test
    public void testAddTask() throws IOException, InterruptedException {
        Task newTask = new Task(2, Type.TASK, "newTask", "newTask description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 2, 0), Duration.ofMinutes(15));

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(newTask))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> tasksFromManager = taskManager.getListOfTasks();

        assertEquals(200, response.statusCode());
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(newTask, tasksFromManager.getLast());

    }

    // Добавление задачи пересекающейся во времени
    @Test
    public void testAddInteractionTask() throws IOException, InterruptedException {
        Task interactionTask = new Task(2, Type.TASK, "Task", "Task description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 1, 0), Duration.ofMinutes(15));

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(interactionTask))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> tasksFromManager = taskManager.getListOfTasks();

        assertEquals(406, response.statusCode());
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(task, tasksFromManager.getLast(), "Задача не должна быть добавлена");

    }

    // Обновление задачи
    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task updateTask = new Task(1, Type.TASK, "updateTask", "updateTask description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 2, 0), Duration.ofMinutes(15));

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/1"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(updateTask))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> tasksFromManager = taskManager.getListOfTasks();

        assertEquals(200, response.statusCode());
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals(updateTask, tasksFromManager.getLast(), "Задача не обновилась");

    }

    // Обновление несуществующей задачи
    @Test
    public void testUpdateNonExistentTask() throws IOException, InterruptedException {
        Task updateTask = new Task(2, Type.TASK, "updateTask", "updateTask description", Status.NEW,
                LocalDateTime.of(2025, 1, 1, 2, 0), Duration.ofMinutes(15));

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/2"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(updateTask))).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        List<Task> tasksFromManager = taskManager.getListOfTasks();

        assertEquals(404, response.statusCode());
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertNull(taskManager.getTask(2), "Задача не должна быть обновлена или добавлена");

    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/1"))
                .DELETE().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNull(taskManager.getTask(1), "Задача должна быть удалена");

    }

    @Test
    public void testDeleteNonExistingTask() throws IOException, InterruptedException {
        HttpRequest getRequest = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/tasks/2"))
                .DELETE().build();
        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode());

    }

}