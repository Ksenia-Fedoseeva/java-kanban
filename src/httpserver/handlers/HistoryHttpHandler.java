package httpserver.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHttpHandler extends BaseHttpHandler {

    public HistoryHttpHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            String[] pathParts = path.split("/");

            switch (method) {
                case "GET":
                    handleGetMethod(exchange, pathParts);
                    break;
                default:
                    sendMethodNotAllowed(exchange);
                    break;
            }
        } catch (Exception e) {
            sendInternalServerError(exchange, e);
        }

    }

    private void handleGetMethod(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 2) {
            List<Task> tasksList = taskManager.getHistory();
            sendOk(exchange, tasksList);
        } else {
            sendNotFound(exchange);
        }
    }
}
