package httpserver.handlers;

import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TasksHttpHandler extends BaseHttpHandler {

    public TasksHttpHandler(TaskManager taskManager) {
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
            ArrayList<Task> tasksList = taskManager.getAllTasks();
            sendOk(exchange, tasksList);
        } else if (pathParts.length == 3) {
            Integer taskId = Integer.valueOf(pathParts[2]);
            Task task = taskManager.getTaskById(taskId);
            if (task != null) {
                sendOk(exchange, task);
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
                Task task = gson.fromJson(new InputStreamReader(exchange.getRequestBody(), "UTF-8"), Task.class);
                if (task.getId() != null) {
                    try {
                        taskManager.updateTask(task);
                        sendNoContent(exchange);
                    } catch (IllegalArgumentException e) {
                        sendNotFound(exchange);
                    }
                } else {
                    taskManager.createTask(task);
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
            Integer taskId = Integer.valueOf(pathParts[2]);
            Task task = taskManager.getTaskById(taskId);
            if (task != null) {
                taskManager.deleteTaskById(taskId);
                sendOk(exchange, task);
            } else {
                sendNotFound(exchange);
            }
        } else if (pathParts.length == 2) {
            taskManager.deleteAllTasks();
            sendNoContent(exchange);
        } else {
            sendNotFound(exchange);
        }
    }
}