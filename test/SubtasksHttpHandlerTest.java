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

public class SubtasksHttpHandlerTest {

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
    public void testGetAllSubtasksSuccess() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Subtask> subtasksFromManager = taskManager.getAllSubtasks();
        assertNotNull(subtasksFromManager, "Сабтаски не возвращаются");
    }

    @Test
    public void testGetSubtaskByIdSuccess() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Testing epic");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask Test", "Testing subtask", epic.getId(),
                Duration.ofMinutes(5), LocalDateTime.now());
        taskManager.createSubtask(subtask);

        URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Subtask receivedSubtask = gson.fromJson(response.body(), Subtask.class);
        assertEquals(subtask.getId(), receivedSubtask.getId(), "Некорректная сабтаска получена по ID");
    }

    @Test
    public void testAddSubtaskSuccess() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test 2", "Testing epic 2");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask Test 2", "Testing subtask 2", epic.getId(),
                Duration.ofMinutes(5), LocalDateTime.now());
        String subtaskJson = gson.toJson(subtask);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Subtask> subtasksFromManager = taskManager.getAllSubtasks();
        assertEquals(1, subtasksFromManager.size(), "Сабтаска не добавлена");
    }

    @Test
    public void testUpdateSubtaskSuccess() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Testing epic");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask Test", "Testing subtask", epic.getId(),
                Duration.ofMinutes(5), LocalDateTime.now());
        taskManager.createSubtask(subtask);

        subtask.setName("Updated Subtask Test");
        String subtaskJson = gson.toJson(subtask);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        Subtask updatedSubtask = taskManager.getSubtaskById(subtask.getId());
        assertEquals("Updated Subtask Test", updatedSubtask.getName(), "Сабтаска не обновлена");
    }

    @Test
    public void testDeleteSubtaskByIdSuccess() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Testing epic");
        taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask Test", "Testing subtask", epic.getId(),
                Duration.ofMinutes(5), LocalDateTime.now());
        taskManager.createSubtask(subtask);

        URI url = URI.create("http://localhost:8080/subtasks/" + subtask.getId());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Subtask deletedSubtask = taskManager.getSubtaskById(subtask.getId());
        assertEquals(null, deletedSubtask, "Сабтаска не удалена");
    }

    @Test
    public void testDeleteAllSubtasksSuccess() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic Test", "Testing epic");
        taskManager.createEpic(epic);
        Subtask subtask1 = new Subtask("Subtask Test 1", "Testing subtask 1", epic.getId(),
                Duration.ofMinutes(5), LocalDateTime.now());
        Subtask subtask2 = new Subtask("Subtask Test 2", "Testing subtask 2", epic.getId(),
                Duration.ofMinutes(5), LocalDateTime.now().plusDays(1));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Subtask> subtasksFromManager = taskManager.getAllSubtasks();
        assertEquals(0, subtasksFromManager.size(), "Сабтаски не удалены");
    }

    @Test
    public void testGetSubtaskByIdNotFound() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/subtasks/9999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Ожидался 404 Not Found");
    }

    @Test
    public void testDeleteSubtaskByIdNotFound() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/subtasks/9999");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Ожидался 404 Not Found");
    }

    @Test
    public void testUpdateSubtaskNotFound() throws IOException, InterruptedException {
        Subtask subtask = new Subtask("Subtask Test", "Testing subtask", 9999,
                Duration.ofMinutes(5), LocalDateTime.now());
        subtask.setId(9999); // Устанавливаем несуществующий ID
        String subtaskJson = gson.toJson(subtask);

        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode(), "Ожидался 404 Not Found");
    }

    @Test
    public void testDeleteAllSubtasksWhenNoneExist() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Ожидался 201 No Content");
    }

    @Test
    public void testMethodNotAllowed() throws IOException, InterruptedException {
        URI url = URI.create("http://localhost:8080/subtasks");

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
   