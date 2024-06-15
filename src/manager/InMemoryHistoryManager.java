package manager;

import customсollection.CustomLinkedList;
import tasks.Task;

import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList history = new CustomLinkedList();

    @Override
    public void add(Task task) {
        if (task != null) {
            history.linkLast(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return history.getTasks();
    }

    @Override
    public void remove(Integer id) {
        history.removeTask(id);
    }

}
