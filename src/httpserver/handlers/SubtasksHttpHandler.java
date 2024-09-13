package httpserver.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class SubtasksHttpHandler extends BaseHttpHandler {

    public SubtasksHttpHandler(TaskManager taskManager) {
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
                case "POST":
                    handlePostMethod(exchange, pathParts);
                    break;
                case "DELETE":
                    handleDeleteMethod(exchange, pathParts);
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
            ArrayList<Subtask> subtasksList = taskManager.getAllSubtasks();
            sendOk(exchange, subtasksList);
        } else if (pathParts.length == 3) {
            Integer subtaskId = Integer.valueOf(pathParts[2]);
            Subtask subtask = taskManager.getSubtaskById(subtaskId);
            if (subtask != null) {
                sendOk(exchange, subtask);
            } else {
                sendNotFound(exchange);
            }
        } else {
            sendNotFound(exchange);
        }
    }

    private void handlePostMethod(HttpExchange exchange, String[] pathParts) throws IOException {
        try {
            if (pathParts.length == 2) {
                Subtask subtask = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), "UTF-8"),
                        Subtask.class);
                if (subtask.getId() != null) {
                    try {
                        taskManager.updateSubtask(subtask);
                        sendNoContent(exchange);
                    } catch (IllegalArgumentException e) {
                        sendNotFound(exchange);
                    }
                } else {
                    taskManager.createSubtask(subtask);
                    sendNoContent(exchange);
                }
            } else {
                sendNotFound(exchange);
            }
        } catch (IllegalArgumentException e) {
            sendNotAcceptable(exchange, e);
        }
    }

    private void handleDeleteMethod(HttpExchange exchange, String[] pathParts) throws IOException {
        if (pathParts.length == 3) {
            Integer subtaskId = Integer.valueOf(pathParts[2]);
            Subtask subtask = taskManager.getSubtaskById(subtaskId);
            if (subtask != null) {
                taskManager.deleteSubtaskById(subtaskId);
                sendOk(exchange, subtask);
            } else {
                sendNotFound(exchange);
            }
        } else if (pathParts.length == 2) {
            taskManager.deleteAllSubtasks();
            sendNoContent(exchange);
        } else {
            sendNotFound(exchange);
        }
    }
}
