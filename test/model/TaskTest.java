package model;

import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    TaskManager taskManager = new InMemoryTaskManager();

    Task task1 = new Task(1, "Task 1", "Task 1 description", Status.NEW);
    Task task2 = new Task(1, "Task 2", "Task 2 description", Status.IN_PROGRESS);

    // Задачи равны если их идентификаторы равны
    @Test
    void tasksShouldBeEqualsIfIdEquals() {
        assertEquals(task1, task2);
    }

}