import manager.InMemoryTaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.enums.Status;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

    @Test
    void updateEpicStatusBasedOnSubtasksTest() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getId(), Duration.ofMinutes(20),
                LocalDateTime.now().plusDays(1));
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getId(), Duration.ofMinutes(90),
                LocalDateTime.now().plusDays(2));
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic createdEpic = taskManager.getEpicById(epic.getId());
        Assertions.assertEquals(Status.NEW, createdEpic.getStatus(), "Статус эпика должен быть NEW.");

        subtask1.setStatus(Status.DONE);
        subtask2.setStatus(Status.DONE);
        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        Epic updatedEpic1 = taskManager.getEpicById(epic.getId());
        Assertions.assertEquals(Status.DONE, updatedEpic1.getStatus(), "Статус эпика должен быть DONE.");

        Subtask subtask3 = new Subtask("Subtask 3", "Description 3", epic.getId(), Duration.ofMinutes(20),
                LocalDateTime.now().plusDays(3));
        taskManager.createSubtask(subtask3);
        Epic updatedEpic2 = taskManager.getEpicById(epic.getId());
        Assertions.assertEquals(Status.IN_PROGRESS, updatedEpic2.getStatus(), "Статус эпика должен быть IN_PROGRESS.");
    }

    @Test
    void updateEpicEvaluationTest() {
        Epic epic = new Epic("Epic", "Description");
        taskManager.createEpic(epic);

        Subtask subtask1 = new Subtask("Subtask 1", "Description 1", epic.getId(),
                Duration.ofMinutes(60), LocalDateTime.now().plusDays(1));
        Subtask subtask2 = new Subtask("Subtask 2", "Description 2", epic.getId(),
                Duration.ofMinutes(20), LocalDateTime.now().plusDays(2));

        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);

        Epic createdEpic = taskManager.getEpicById(epic.getId());

        Assertions.assertEquals(subtask1.getStartTime(), createdEpic.getStartTime(),
                "Время начала эпика должно совпадать с самым ранним временем начала подзадач.");
        Assertions.assertEquals(Duration.ofMinutes(80), createdEpic.getDuration(),
                "Продолжительность эпика должна быть суммой продолжительностей подзадач.");
        Assertions.assertEquals(subtask2.getEndTime(), createdEpic.getEndTime(),
                "Время окончания эпика должно совпадать с самым поздним временем окончания подзадач.");
    }

}