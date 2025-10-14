package service;

import model.Node;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {

    private Node first;
    private Node last;
    private final Map<Integer, Node> idNode = new HashMap<>();

    private Node linkLast(Task task) {
        final Node oldTail = last;
        final Node newNode = new Node(oldTail, task, null);
        last = newNode;
        if (oldTail == null)
            first = newNode;
        else
            oldTail.next = newNode;
        
        return newNode;
    }

    private void removeNode(Node node) {
        if (node == null) {
            return;
        }
        
        Node prevNode = node.prev;
        Node nextNode = node.next;
        
        if (prevNode == null) {
            first = nextNode;
        } else {
            prevNode.next = nextNode;
        }
        
        if (nextNode == null) {
            last = prevNode;
        } else {
            nextNode.prev = prevNode;
        }
    }

    private List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        Node current = first;
        while (current != null) {
            tasks.add(current.data);
            current = current.next;
        }
        return tasks;
    }

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        int taskId = task.getId();
        Node existingNode = idNode.get(taskId);
        // удаляем её старый просмотр если задача есть
        if (existingNode != null) {
            removeNode(existingNode);
        }
        Node newNode = linkLast(task);
        idNode.put(taskId, newNode);

    }


    @Override
    public void remove(int id) {

        Node node = idNode.get(id);

        if (node != null) {
            removeNode(node);
            idNode.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}








