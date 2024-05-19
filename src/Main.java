import manager.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

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

        Epic epic2 = new Epic("Готовка", "Варим суп");
        taskManager.createEpic(epic2);

        Subtask subtask3 = new Subtask("Купить продукты на суп", "Сходить в магнит и овощной", epic2.getId());
        taskManager.createSubtask(subtask3);

        System.out.println(taskManager);
    }

}
