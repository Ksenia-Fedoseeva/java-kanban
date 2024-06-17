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
        removeDuplicateIfExist(task.getId());

        Node newNode = new Node(task, tail, null);
        if (nodeMap.isEmpty()) {
            tail = head = newNode;
        } else {
            tail.setNext(newNode);
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

    public void removeDuplicateIfExist(Integer id) {
        removeTask(id);
    }

    public void removeTask(Integer id) {
        if (!nodeMap.isEmpty()) {
            Node removedNode = nodeMap.remove(id);
            removeNode(removedNode);
        }
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        if (node.prev == null && node.next == null) { // удаляем единственную ноду в списке
            head = null;
            tail = null;
        } else if (node.prev != null && node.next != null) { // удаляем ноду в середине списка
            node.prev.next = node.next;
            node.next.prev = node.prev;
        } else if (node.next == null) { // удаляем ноду в конце списка
            tail = node.prev;
            tail.next = null;
        } else { // удаляем ноду в начале списка
            head = node.next;
            head.prev = null;
        }
    }

    private static class Node {
        private Task task;
        private Node prev;
        private Node next;

        public Node(Task task) {
            this.task = task;
        }

        public Node(Task task, Node prev, Node next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        public Task getTask() {
            return task;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }
    }

}
