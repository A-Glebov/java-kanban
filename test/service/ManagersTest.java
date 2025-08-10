package service;

import model.Status;
import model.Task;
import model.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    // Проверяем что Manager возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    private TaskManager taskManager;
    private Task task;

    @BeforeEach
    public void init() {
        taskManager = Managers.getDefault();
        task = new Task(1, Type.TASK, "Task", "Task description", Status.NEW);
        taskManager.createTask(task);
    }

    @Test
    public void getDefault() {
        assertNotNull(taskManager, "Объект не создан");
        assertNotNull(taskManager.getTask(1), "Менеджер не возвращает задачу");
    }

    @Test
    public void getDefaultHistory() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertNotNull(historyManager, "Объект не создан");
        taskManager.getTask(1);
        assertNotNull(historyManager.getHistory());
    }

}