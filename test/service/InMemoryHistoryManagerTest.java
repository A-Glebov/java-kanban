package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager;
    Task task1;
    Task task2;
    Task task3;

    @BeforeEach
    void init() {
        historyManager = Managers.getDefaultHistory();

        task1 = new Task(1, "Task1", "Task description", Status.NEW);
        task2 = new Task(2, "Task2", "Task description", Status.NEW);
        task3 = new Task(3, "Task3", "Task description", Status.NEW);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

    }

    // Задачи добавляются в историю
    @Test
    void isTaskAddedToHistory() {
        assertEquals(3, historyManager.getHistory().size());
    }

    // Размер не увеличился при добавлении той же задачи
    @Test
    void sizeHistoryDoesNotIncreaseWhenAddingSameElement() {
        historyManager.add(task2);
        assertEquals(3, historyManager.getHistory().size());
    }

    // Задача добавилась в конец
    @Test
    void taskShouldBeAddedToEndOfList() {
        historyManager.add(task2);
        assertEquals(task2, historyManager.getHistory().getLast());
    }

    // В истории не должно быть повторяющихся задач
    @Test
    void shouldNotBeIdenticalTasksInHistory() {
        List<Task> list = new ArrayList<>();
        historyManager.add(task2);
        for (Task task : historyManager.getHistory()) {
            if (task.equals(task2))
                list.add(task);
        }
        assertEquals(1, list.size());

    }

    // Задачи должны удаляться из истории
    @Test
    void shouldBeDeleteFromHistory() {
        historyManager.remove(2);
        historyManager.remove(3);

        assertEquals(1, historyManager.getHistory().size()); // Проверка по размеру
        assertEquals(task1, historyManager.getHistory().getLast()); // Проверка, что нужные элементы остались

    }


}