package service;

import model.Node;
import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    // Список для хранения истории просмотров
    private final Map<Integer, Node> nodes = new HashMap<>();
    private Node first;
    private Node last;

    // Создание Node
    private void linkLast(Task task) {
        Node newNode = new Node(last, task, null);

        if (first == null) {
            first = newNode;
            first.prev = null;
        } else {
            last.next = newNode;
        }

        last = newNode;

    }

    // Удаление Node
    private void removeNode(Node node) {

        if (node == null) {
            return;
        }

        if (node.prev == null) {
            first = first.next;

            if (first == null) {
                last = null;
            } else {
                first.prev = null;
            }

        } else if (node.next == null) {
            last = last.prev;
            last.next = null;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

    }

    // Получение списка задач из Node
    private List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        Node currentNode = first;

        while (currentNode != null) {
            history.add(currentNode.task);
            currentNode = currentNode.next;
        }

        return history;

    }

    // Метод для обновления истории просмотров
    @Override
    public void add(Task task) {
        remove(task.getId());
        linkLast(task);
        nodes.put(task.getId(), last);
    }

    // Удаление задачи по id
    @Override
    public void remove(int id) {
        if (nodes.get(id) == null) {
            return;
        }
        Node node = nodes.remove(id);
        removeNode(node);
    }

    //Метод для получения истории просмотров задач
    @Override
    public List<Task> getHistory() {
        return this.getTasks();
    }

}
