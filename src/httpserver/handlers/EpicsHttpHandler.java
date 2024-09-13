package httpserver.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EpicsHttpHandler extends BaseHttpHandler {

    public EpicsHttpHandler(TaskManager taskManager) {
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
            ArrayList<Epic> epicsList = taskManager.getAllEpics();
            sendOk(exchange, epicsList);
        } else if (pathParts.length == 3) {
            Integer epicsId = Integer.valueOf(pathParts[2]);
            Epic epic = taskManager.getEpicById(epicsId);
            if (epic != null) {
                sendOk(exchange, epic);
            } else {
                sendNotFound(exchange);
            }
        } else if (pathParts.length == 4) {
            Integer epicsId = Integer.valueOf(pathParts[2]);
            Epic epic = taskManager.getEpicById(epicsId);
            ArrayList<Subtask> subtasksList = taskManager.getAllSubtasksByEpicId(epicsId);
            if (epic != null) {
                sendOk(exchange, subtasksList);
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
                Epic epic = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), "UTF-8"), Epic.class);
                if (epic.getId() != null) {
                    try {
                        taskManager.updateEpic(epic);
                        sendNoContent(exchange);
                    } catch (IllegalArgumentException e) {
                        sendNotFound(exchange);
                    }
                } else {
                    taskManager.createEpic(epic);
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
            Integer epicsId = Integer.valueOf(pathParts[2]);
            Epic epic = taskManager.getEpicById(epicsId);
            if (epic != null) {
                taskManager.deleteEpicById(epicsId);
                sendOk(exchange, epic);
            } else {
                sendNotFound(exchange);
            }
        } else if (pathParts.length == 2) {
            taskManager.deleteAllEpics();
            sendNoContent(exchange);
        } else {
            sendNotFound(exchange);
        }
    }
}
