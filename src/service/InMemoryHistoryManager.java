package service;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    // Список для хранения истории просмотров
    private final List<Task> history = new ArrayList<>();

    // Метод для обновления истории просмотров
    @Override
    public void add(Task task) {
        if (history.size() == 10) {
            history.removeFirst();
        }
        history.add(task);
    }

    //Метод для получения истории просмотров задач
    @Override
    public List<Task> getHistory() {
        return this.history;
    }

}
