package service;

import model.Status;
import model.Task;
import model.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    public void init() {
        historyManager = Managers.getDefaultHistory();

        task1 = new Task(1, Type.TASK, "Task1", "Task description", Status.NEW);
        task2 = new Task(2, Type.TASK, "Task2", "Task description", Status.NEW);
        task3 = new Task(3, Type.TASK, "Task3", "Task description", Status.NEW);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

    }

    // Задачи добавляются в историю
    @Test
    public void isTaskAddedToHistory() {
        assertEquals(3, historyManager.getHistory().size());
    }

    // Размер не увеличился при добавлении той же задачи
    @Test
    public void sizeHistoryDoesNotIncreaseWhenAddingSameElement() {
        historyManager.add(task2);
        assertEquals(3, historyManager.getHistory().size());
    }

    // Задача добавилась в конец
    @Test
    public void taskShouldBeAddedToEndOfList() {
        historyManager.add(task2);
        assertEquals(task2, historyManager.getHistory().getLast());
    }

    // В истории не должно быть повторяющихся задач
    @Test
    public void shouldNotBeIdenticalTasksInHistory() {
        List<Task> list = new ArrayList<>();
        historyManager.add(task2);

        for (Task task : historyManager.getHistory()) {
            if (task.equals(task2)) {
                list.add(task);
            }
        }

        assertEquals(1, list.size());

    }

    // Удаление первого элемента истории
    @Test
    public void deleteFirst() {
        historyManager.remove(1);
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task2, historyManager.getHistory().getFirst());
        assertEquals(task3, historyManager.getHistory().getLast());

    }

    // Удаление элемента из середины истории
    @Test
    public void deleteFromMiddle() {
        historyManager.remove(2);
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().getFirst());
        assertEquals(task3, historyManager.getHistory().getLast());

    }

    // Удаление последнего элемента истории
    @Test
    public void deleteLast() {
        historyManager.remove(3);
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().getFirst());
        assertEquals(task2, historyManager.getHistory().getLast());

    }

}