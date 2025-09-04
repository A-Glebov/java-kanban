package http;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class TaskHandler extends BaseHttpHandler {

    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();
        try {
            switch (requestMethod) {
                case "GET" -> {
                    if (Pattern.matches("^/tasks$", path)) {
                        String response = gson.toJson(taskManager.getListOfTasks());
                        sendText(exchange, response);
                        return;
                    }

                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/", "");
                        int id = parseId(pathId);

                        if (id != -1) {
                            Task task = taskManager.getTask(id);
                            if (task == null) {
                                sendNotFound(exchange, "Задача с ID=" + id + " не найдена");
                                return;
                            }
                            String response = gson.toJson(task);
                            sendText(exchange, response);
                        }

                    }

                }

                case "POST" -> {
                    // Добавление новой задачи
                    if (Pattern.matches("^/tasks$", path)) {
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Task task = gson.fromJson(body, Task.class);
                        taskManager.createTask(task);

                        if (taskManager.getListOfTasks().contains(task)) {
                            sendText(exchange, "Задача успешно добавлена");
                        } else {
                            sendHasInteractions(exchange, "Задача пересекается во времени");
                        }

                    }

                    //UPDATE TASK
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/", "");
                        int id = parseId(pathId);
                        if (id != -1) {
                            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            Task task = gson.fromJson(body, Task.class);

                            if (taskManager.getTask(id) != null) {
                                taskManager.updateTask(task);
                                sendText(exchange, "Задача успешно обновлена");
                            } else {
                                sendNotFound(exchange, "Задача с ID=" + id + " не найдена");
                            }

                        }
                    }

                }

                case "DELETE" -> {
                    if (Pattern.matches("^/tasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/tasks/", "");
                        int id = parseId(pathId);

                        if (id != -1 && taskManager.getTask(id) != null) {
                            taskManager.deleteTaskById(id);
                            sendText(exchange, "Задача с ID=" + id + " успешно удалена");
                        } else {
                            sendNotFound(exchange, "Задача с ID=" + id + " не найдена");
                        }

                    }

                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
