import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Переезд", "Купить коробки");
        taskManager.createTask(task1);
        Task task2 = new Task("Отдых", "Организовать прогулку на велосипедах");
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Отпуск", "Летим в Турцию");
        taskManager.createEpic(epic1);

        Subtask subtask1 = new Subtask("Купить билеты", "На авиасейлз", epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Купить чемодан", "Определиться с размером", epic1.getId());
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("Купить крем спф", "Определиться с брендом", epic1.getId());
        taskManager.createSubtask(subtask3);

        Epic epic2 = new Epic("Готовка", "Варим суп");
        taskManager.createEpic(epic2);

        // Запросила созданные задачи несколько раз в разном порядке.
        taskManager.getTaskById(task1.getId());
        taskManager.getEpicById(epic1.getId());
        taskManager.getSubtaskById(subtask1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getSubtaskById(subtask2.getId());
        taskManager.getEpicById(epic2.getId());
        taskManager.getSubtaskById(subtask3.getId());

        // После каждого запроса вывожу историю и убеждаюсь, что в ней нет повторов.
        printHistory(taskManager.getHistory());

        // Удаляю задачу, которая есть в истории, и проверяю, что при печати она не будет выводиться.
        taskManager.deleteTaskById(task1.getId());
        printHistory(taskManager.getHistory());

        // Удаляю эпик с тремя подзадачами и убеждаюсь, что из истории удалился как сам эпик, так и все его подзадачи.
        taskManager.deleteEpicById(epic1.getId());
        printHistory(taskManager.getHistory());
    }

    private static void printHistory(List<Task> history) {
        System.out.println("History:");
        for (Task task : history) {
            System.out.println(task);
        }
    }
}
