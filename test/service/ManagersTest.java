package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    // Проверяем что Manager возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    TaskManager taskManager;
    Task task;

    @BeforeEach
    void init() {
        taskManager = Managers.getDefault();
        task = new Task(1, "Task", "Task description", Status.NEW);
        taskManager.createTask(task);
    }

    @Test
    void getDefault() {
        assertNotNull(taskManager, "Объект не создан");
        assertNotNull(taskManager.getTask(1), "Менеджер не возвращает задачу");
    }

    @Test
    void getDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Объект не создан");
        taskManager.getTask(1);
        assertNotNull(historyManager.getHistory());
    }
}