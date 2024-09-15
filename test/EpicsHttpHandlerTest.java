import adapter.gson.DurationTypeAdapter;
import adapter.gson.LocalDateTimeTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import httpserver.HttpTaskServer;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.enums.Status;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EpicsHttpHandlerTest {

    private TaskManager taskManager;
    private HttpTaskServer httpTaskServer;
    private Gson gson;
    private HttpClient client;

    @BeforeEach
    void init() {
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer();
        httpTaskServer.start(taskManager);
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationTypeAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void stopServer() {
        httpTaskServer.stop();
    }

    @Test
    public void testGetAllEpicsSuccess() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Epic> epicsFromManager = taskManager.getAllEpics();
        assertNotNull(epicsFromManager, "Эпики не возвращаются");
    }

    @Test
    public void testGetEpicByIdSuccess() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Testing epic");
        taskManager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask 1", "Description", epic.getId(), Duration.ofMinutes(60),
                LocalDateTime.now());
        taskManager.createSubtask(subtask);

        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Epic receivedEpic = gson.fromJson(response.body(), Epic.class);

        assertEquals(epic.getId(), receivedEpic.getId(), "Некорректный эпик получен по ID");

        assertNotNull(receivedEpic.getStartTime(), "startTime у эпика должен быть заполнен");
        assertNotNull(receivedEpic.getEndTime(), "endTime у эпика должен быть заполнен");
    }

    @Test
    public void testDeleteEpicByIdSuccess() throws IOException, InterruptedException {
        Epic epic = new Epic(1, "Epic 1", "Testing epic 1", Status.NEW, Duration.ofMinutes(5),
                LocalDateTime.now());
        taskManager.createEpic(epic);

        URI url = URI.create("http://localhost:8080/epics/" + epic.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Epic deletedEpic = taskManager.getEpicById(epic.getId());
        assertEquals(null, deletedEpic, "Эпик не удален");
    }

    @Test
    public void testDeleteAllEpicsSuccess() throws IOException, InterruptedException {
        Epic epic1 = new Epic("Epic 1", "Testing epic 1");
        Epic epic2 = new Epic("Epic 2", "Testing epic 2");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);

        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Epic> epicsFromManager = taskManager.getAllEpics();
        assertEquals(0, epicsFromManager.size(), "Эпики не удалены");
    }

    @Test
    public void testGetAllSubtasksByEpicIdSuccess() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Testing epic");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description", epic.getId(), Duration.ofMinutes(60), LocalDateTime.now());
        Subtask subtask2 = new Subtask("Subtask 2", "Description", epic.getId(), Duration.ofMinutes(45), LocalDateTime.now().plusHours(1));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        URI url = URI.create("http://localhost:8080/epics/" + epic.getId() + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Ожидался код 200 при запросе сабтасков по ID эпика");

        Subtask[] subtasks = gson.fromJson(response.body(), Subtask[].class);

        assertEquals(2, subtasks.length, "Количество сабтасков должно быть равно 2");

        assertEquals(epic.getId(), subtasks[0].getEpicId(), "ID эпика в сабтаске должен совпадать");
        assertEquals(epic.getId(), subtasks[1].getEpicId(), "ID эпика в сабтаске должен совпадать");
    }

    @Test
    public void testGetEpicByIdNotFound() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/epics/9999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Ожидался 404 Not Found");
    }

    @Test
    public void testDeleteEpicByIdNotFound() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/epics/9999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Ожидался 404 Not Found");
    }

    @Test
    public void testMethodNotAllowed() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(405, response.statusCode(), "Ожидался 405 Method Not Allowed");
    }

    @Test
    public void testAddTaskInvalidData() throws IOException, InterruptedException {
        String invalidTaskJson = "{}";

        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(invalidTaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode(), "Ожидался 400 Bad Request");
    }
}