package http;

import com.sun.net.httpserver.HttpExchange;
import service.TaskManager;

import java.io.IOException;
import java.util.regex.Pattern;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        String path = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        try {
            if (requestMethod.equals("GET")) {
                if (Pattern.matches("^/history$", path)) {
                    String response = gson.toJson(taskManager.getHistory());
                    sendText(exchange, response);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
