package http;

import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.SubTask;
import service.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class EpicHandler extends BaseHttpHandler {

    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        try {
            switch (requestMethod) {
                case "GET" -> {
                    if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
                        String pathId = path.split("/")[2];
                        int id = parseId(pathId);

                        if (id != -1) {
                            Epic epic = taskManager.getEpic(id);

                            if (epic == null) {
                                sendNotFound(exchange, "Эпик с ID=" + id + " не найден");
                            } else {
                                List<SubTask> subtasks = taskManager.getListOfSubtaskOfEpic(epic);
                                String response = gson.toJson(subtasks);
                                sendText(exchange, response);
                            }

                        }

                    }

                    if (Pattern.matches("^/epics$", path)) {
                        String response = gson.toJson(taskManager.getListOfEpics());
                        sendText(exchange, response);
                        return;
                    }

                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epics/", "");
                        int id = parseId(pathId);
                        if (id != -1) {
                            Epic epic = taskManager.getEpic(id);
                            if (epic == null) {
                                sendNotFound(exchange, "Эпик с ID=" + id + " не найден");
                                return;
                            }
                            String response = gson.toJson(epic);
                            sendText(exchange, response);
                        }
                    }

                }

                case "POST" -> {
                    if (Pattern.matches("^/epics$", path)) {
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Epic epic = gson.fromJson(body, Epic.class);
                        taskManager.createEpic(epic);
                        sendCreated(exchange, "Эпик успешно добавлен");

                    }

                }

                case "DELETE" -> {
                    if (Pattern.matches("^/epics/\\d+$", path)) {
                        String pathId = path.replaceFirst("/epics/", "");
                        int id = parseId(pathId);
                        if (id != -1) {
                            taskManager.deleteEpicById(id);
                            sendText(exchange, "Эпик с ID=" + id + " успешно удален");
                        }

                    }

                }

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}