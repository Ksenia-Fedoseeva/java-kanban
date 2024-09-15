package httpserver.handlers;

import adapter.gson.DurationTypeAdapter;
import adapter.gson.LocalDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.BadRequestException;
import manager.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public abstract class BaseHttpHandler implements HttpHandler {
    protected final TaskManager taskManager;
    protected final Gson gson;

    protected BaseHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
    }

    protected <T> T convertJsonToObject(HttpExchange exchange, Class<T> classOfT) throws IOException {
        String jsonString = readText(exchange);
        if (!validateJsonRequest(jsonString)) {
            throw new BadRequestException("Пришло пустое тело запроса");
        }
        return jsonToObject(jsonString, classOfT);
    }

    protected String readText(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected boolean validateJsonRequest(String jsonString) {
        return jsonString != null && !jsonString.isBlank() && !jsonString.equals("{}");
    }

    protected <T> T jsonToObject(String jsonString, Class<T> classOfT) {
        return gson.fromJson(jsonString, classOfT);
    }

    protected void sendOk(HttpExchange exchange, Object responseObject) throws IOException {
        try (exchange) {
            String responseString = gson.toJson(responseObject);
            byte[] resp = responseString.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(200, resp.length);
            exchange.getResponseBody().write(resp);
        }
    }

    protected void sendNoContent(HttpExchange exchange) throws IOException {
        try (exchange) {
            exchange.sendResponseHeaders(201, 0);
        }
    }

    protected void sendBadRequest(HttpExchange exchange, BadRequestException exception) throws IOException {
        try (exchange) {
            System.out.println("Ошибка во время выполнения запроса - " + exception.getMessage());
            exception.printStackTrace();

            String responseString = "{\"errorMessage\" : \"" + exception.getMessage() + "\"}";
            byte[] resp = responseString.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(400, resp.length);
            exchange.getResponseBody().write(resp);
        }
    }

    protected void sendNotFound(HttpExchange exchange) throws IOException {
        try (exchange) {
            exchange.sendResponseHeaders(404, 0);
        }
    }

    protected void sendMethodNotAllowed(HttpExchange exchange) throws IOException {
        try (exchange) {
            exchange.sendResponseHeaders(405, 0);
        }
    }

    protected void sendInternalServerError(HttpExchange exchange, Exception exception) throws IOException {
        try (exchange) {
            System.out.println("Ошибка во время выполнения запроса - " + exception.getMessage());
            exception.printStackTrace();

            String responseString = "{\"errorMessage\" : \"" + exception.getMessage() + "\"}";
            byte[] resp = responseString.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(500, resp.length);
            exchange.getResponseBody().write(resp);
        }
    }

    protected void sendNotAcceptable(HttpExchange exchange, Exception exception) throws IOException {
        try (exchange) {
            System.out.println("Ошибка во время выполнения запроса - " + exception.getMessage());
            exception.printStackTrace();

            String responseString = "{\"errorMessage\" : \"" + exception.getMessage() + "\"}";
            byte[] resp = responseString.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(406, resp.length);
            exchange.getResponseBody().write(resp);
        }
    }

}
