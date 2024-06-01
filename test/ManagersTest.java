import manager.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ManagersTest {

    @Test
    void getDefaultTest() {
        TaskManager taskManager = Managers.getDefault();
        Assertions.assertNotNull(taskManager, "Объект TaskManager равен null.");
        Assertions.assertInstanceOf(InMemoryTaskManager.class, taskManager,
                "Тип данных должен быть InMemoryTaskManager.");
    }

    @Test
    void getDefaultHistoryTest() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Assertions.assertNotNull(historyManager, "Объект HistoryManager равен null.");
        Assertions.assertInstanceOf(InMemoryHistoryManager.class, historyManager,
                "Тип данных должен быть InMemoryHistoryManager.");
    }

}
