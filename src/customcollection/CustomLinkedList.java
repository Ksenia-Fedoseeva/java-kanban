package customcollection;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomLinkedList {
    private Node tail;
    private HashMap<Integer, Node> nodeMap = new HashMap<>();

    public void linkLast(Task task) {
        Integer taskId = task.getId();
        if (nodeMap.containsKey(taskId)) {
            removeNode(nodeMap.get(taskId));
            if (nodeMap.size() == 1) {
                tail = null;
            }
        }
        Node newNode = new Node(task);
        if (tail == null) {
            tail = newNode;
        } else {
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
        }
        nodeMap.put(task.getId(), newNode);
    }

    public List<Task> getTasks() {
        List<Task> tasksList = new ArrayList<>();
        Node currentNode = tail;
        while (currentNode != null) {
            tasksList.add(currentNode.getTask());
            currentNode = currentNode.getPrev();
        }
        return tasksList;
    }

    public void removeTask(Integer id) {
        Node removedNode = nodeMap.remove(id);
        removeNode(removedNode);
        if (nodeMap.isEmpty()) {
            tail = null;
        }
    }

    private void removeNode(Node node) {
        if (node != null) {
            Node prevNode = node.getPrev();
            Node nextNode = node.getNext();
            if (prevNode != null) {
                prevNode.setNext(nextNode);
            }
            if (nextNode != null) {
                nextNode.setPrev(prevNode);
            }
        }
    }

}
