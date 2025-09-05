package http;

import com.sun.net.httpserver.HttpExchange;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class SubTaskHandler extends BaseHttpHandler {

    public SubTaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        try {
            switch (requestMethod) {
                case "GET" -> {
                    if (Pattern.matches("^/subtasks$", path)) {
                        String response = gson.toJson(taskManager.getListOfSubTask());
                        sendText(exchange, response);
                        return;
                    }

                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id = parseId(pathId);
                        if (id != -1) {
                            SubTask subtask = taskManager.getSubTask(id);
                            if (subtask == null) {
                                sendNotFound(exchange, "Подзадача с ID=" + id + " не найдена");
                                return;
                            }
                            String response = gson.toJson(subtask);
                            sendText(exchange, response);
                        }
                    }

                }

                case "POST" -> {
                    // Добавление новой задачи
                    if (Pattern.matches("^/subtasks$", path)) {
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        SubTask subtask = gson.fromJson(body, SubTask.class);
                        taskManager.createSubTask(subtask);

                        if (taskManager.getListOfSubTask().contains(subtask)) {
                            sendText(exchange, "Подзадача успешно добавлена");
                        } else {
                            sendHasInteractions(exchange, "Подзадача пересекается во времени");
                        }
                        return;
                    }

                    //Обновление подзадачи
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id = parseId(pathId);

                        if (id != -1) {
                            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                            SubTask subtask = gson.fromJson(body, SubTask.class);

                            if (taskManager.getSubtasks().containsKey(id)) {
                                taskManager.updateSubTask(subtask);
                                sendText(exchange, "Подзадача успешно обновлена");
                            } else {
                                sendNotFound(exchange, "Подзадача с ID=" + id + " не найдена");
                            }
                        }

                    }

                }

                case "DELETE" -> {
                    if (Pattern.matches("^/subtasks/\\d+$", path)) {
                        String pathId = path.replaceFirst("/subtasks/", "");
                        int id = parseId(pathId);

                        if (id != -1 && taskManager.getSubtasks().containsKey(id)) {
                            taskManager.deleteTaskById(id);
                            sendText(exchange, "Подзадача с ID=" + id + " успешно удалена");
                        } else {
                            sendNotFound(exchange, "Подзадача с ID=" + id + " не найдена");
                        }

                    }

                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
