package http;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;
import java.util.regex.Pattern;

public class PriorityHandler extends BaseHttpHandler {
    public PriorityHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        try {
            if (requestMethod.equals("GET")) {
                if (Pattern.matches("^/prioritized$", path)) {
                    String response = gson.toJson(taskManager.getPrioritizedTasks());
                    sendText(exchange, response);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
