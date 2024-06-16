package customcollection;

import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomLinkedList {
    private Node head;
    private Node tail;
    private HashMap<Integer, Node> nodeMap = new HashMap<>();

    public void linkLast(Task task) {
        Integer taskId = task.getId();
        checkDuplicate(taskId);

        Node newNode = new Node(task, tail, null);
        if (nodeMap.isEmpty()) {
            tail = head = newNode;
        } else {
            tail.setNext(newNode);
            if (nodeMap.size() == 1) {
                head = tail;
            }
            tail = newNode;
        }
        nodeMap.put(task.getId(), newNode);
    }

    private void checkDuplicate(Integer taskId) {
        if (nodeMap.containsKey(taskId)) {
            removeNode(nodeMap.get(taskId));
            if (nodeMap.size() == 1) {
                tail = null;
                head = null;
                nodeMap.clear();
            }
        }
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
    }

    private void removeNode(Node node) {
        if (node != null) {
            Node prevNode = node.getPrev();
            Node nextNode = node.getNext();
            if (prevNode != null) {
                prevNode.setNext(nextNode);
            } else {
                // Если prevNode == null, то мы работаем с головой и голову надо обновить
                head = nextNode;
            }
            if (nextNode != null) {
                nextNode.setPrev(prevNode);
            } else {
                // Если nextNode == null, то мы работаем с хвостом и хвост надо обновить
                tail = prevNode;
            }
        }
        if (tail != null && tail.getPrev() == null && tail.getNext() == null) {
            head = tail;
            return;
        }
        if (head != null && head.getPrev() == null && head.getNext() == null) {
            tail = head;
        }
    }

}
