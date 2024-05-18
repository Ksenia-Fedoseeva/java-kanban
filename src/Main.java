import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.enums.TaskStatuses;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Переезд", "Купить коробки");
        taskManager.createOrUpdateTask(task1);
        Task task2 = new Task("Отдых", "Организовать прогулку на велосипедах");
        taskManager.createOrUpdateTask(task2);

        Epic epic1 = new Epic("Отпуск", "Летим в Турцию");
        taskManager.createOrUpdateEpic(epic1);

        Subtask subtask1 = new Subtask("Купить билеты", "На авиасейлз", epic1);
        taskManager.createOrUpdateSubtask(subtask1);
        Subtask subtask2 = new Subtask("Купить чемодан", "Определиться с размером", epic1);
        taskManager.createOrUpdateSubtask(subtask2);

        Epic epic2 = new Epic("Готовка", "Варим суп");
        taskManager.createOrUpdateEpic(epic2);

        Subtask subtask3 = new Subtask("Купить продукты на суп", "Сходить в магнит и овощной", epic2);
        taskManager.createOrUpdateSubtask(subtask3);

        System.out.println(taskManager);
    }

}
