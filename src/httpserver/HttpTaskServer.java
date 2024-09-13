package httpserver;

import com.sun.net.httpserver.HttpServer;
import httpserver.handlers.*;
import manager.Managers;
import manager.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private static final int PORT = 8080;
    private HttpServer httpServer;

    public static void main(String[] args) {
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        httpTaskServer.start(Managers.getDefault());
    }

    public void start(TaskManager taskManager) {
        try {
            httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TasksHttpHandler(taskManager));
            httpServer.createContext("/subtasks", new SubtasksHttpHandler(taskManager));
            httpServer.createContext("/epics", new EpicsHttpHandler(taskManager));
            httpServer.createContext("/history", new HistoryHttpHandler(taskManager));
            httpServer.createContext("/prioritized", new PrioritizedHttpHandler(taskManager));
            httpServer.start();
        } catch (IOException e) {
            System.out.println("Ошибка " + e.getMessage());
        }
    }

    public void stop() {
        if (httpServer != null) {
            httpServer.stop(0);
        }
    }
}