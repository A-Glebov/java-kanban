package service;

import model.Status;
import model.Task;
import model.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ManagersTest {

    // Проверяем что Manager возвращает проинициализированные и готовые к работе экземпляры менеджеров;
    private TaskManager taskManager;

    @BeforeEach
    public void init() {
        taskManager = Managers.getDefault();

        Task task = new Task(1, Type.TASK, "Task", "Task description", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 1, 0), Duration.ofMinutes(15));

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