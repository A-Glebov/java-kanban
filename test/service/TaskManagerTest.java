package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected Epic epic;
    protected SubTask subtask;

    @BeforeEach
    public void init() throws IOException {
        task = new Task(1, Type.TASK, "Task", "Task description", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 1, 0), Duration.ofMinutes(15));
        epic = new Epic(2, Type.EPIC, "Epic", "Description Epic", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 12, 0), Duration.ofMinutes(20),
                new ArrayList<>());
        subtask = new SubTask(3, Type.SUBTASK, "Subtask Epic", "Description Sub Ep",
                Status.NEW, LocalDateTime.of(2025, 5, 15, 12, 53),
                Duration.ofMinutes(30), epic.getId());

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask);

    }

    @Test
    public void getListOfTasksAllTypes() {
        assertEquals(1, taskManager.getListOfTasks().size(), "Список задач пуст");
        assertEquals(1, taskManager.getListOfEpics().size(), "Список эпиков пуст");
        assertEquals(1, taskManager.getListOfSubTask().size(), "Список подзадач пуст");

        taskManager.deleteAllTasks();
        taskManager.deleteAllEpic();
        taskManager.deleteAllSubTask();

    }

    @Test
    void deleteAllTasksOfAllTypes() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpic();
        taskManager.deleteAllSubTask();

        assertTrue(taskManager.getListOfTasks().isEmpty(), "Список задач не пуст");
        assertTrue(taskManager.getListOfEpics().isEmpty(), "Список эпиков не пуст");
        assertTrue(taskManager.getListOfSubTask().isEmpty(), "Список подзадач не пуст");
    }

    @Test
    void getTaskOfAllTypes() {
        Task savedTask = taskManager.getTask(1);
        Epic savedEpic = taskManager.getEpic(2);
        SubTask savedSubtask = taskManager.getSubTask(3);

        assertNotNull(savedTask, "Задача не найдена.");
        assertNotNull(savedEpic, "Эпик не найден");
        assertNotNull(savedSubtask, "Эпик не найден");

        assertEquals(task, savedTask, "Задачи не совпадают");
        assertEquals(epic, savedEpic, "Эпики не совпадают");
        assertEquals(subtask, savedSubtask, "Подзадачи не совпадают");

    }

    @Test
    void getTaskWithNonExistentIdOfAllTypes() {
        Task savedTask = taskManager.getTask(4);
        Epic savedEpic = taskManager.getEpic(4);
        SubTask savedSubtask = taskManager.getSubTask(4);

        assertNull(savedTask, "Задача не должна быть найдена.");
        assertNull(savedEpic, "Эпик не должен быть найден");
        assertNull(savedSubtask, "Подзадача не должна быть найдена");

    }

    @Test
    void getTaskOfAllTypesFromEmptyList() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpic();
        taskManager.deleteAllSubTask();

        Task savedTask = taskManager.getTask(1);
        Epic savedEpic = taskManager.getEpic(2);
        SubTask savedSubtask = taskManager.getSubTask(3);

        assertNull(savedTask, "Задача не должна быть найдена.");
        assertNull(savedEpic, "Эпик не должен быть найден");
        assertNull(savedSubtask, "Подзадача не должна быть найдена");

    }


    @Test
    public void fieldsBeEqualsAfterAddTaskInManager() {
        Task savedTask = taskManager.getTask(1);

        assertEquals(task.getId(), savedTask.getId(), "Идентификаторы должны быть равны");
        assertEquals(task.getType(), savedTask.getType(), "Типы должны быть равны");
        assertEquals(task.getName(), savedTask.getName(), "Имена должны быть равны");
        assertEquals(task.getDescription(), savedTask.getDescription(), "Описания должны быть равны");
        assertEquals(task.getStatus(), savedTask.getStatus(), "Статусы должны быть равны");
        assertEquals(task.getStartTime(), savedTask.getStartTime(), "Время начала должно быть равно");
        assertEquals(task.getDuration(), savedTask.getDuration(), "Продолжительность должна быть равна");

    }

    // Тест, в котором проверяется неизменность эпика (по всем полям) при добавлении эпика в менеджер
    @Test
    public void fieldsBeEqualsAfterAddEpicInManager() {
        Epic savedEpic = taskManager.getEpic(2);

        assertEquals(epic.getId(), savedEpic.getId(), "Идентификаторы должны быть равны");
        assertEquals(epic.getType(), savedEpic.getType(), "Типы должны быть равны");
        assertEquals(epic.getName(), savedEpic.getName(), "Имена должны быть равны");
        assertEquals(epic.getDescription(), savedEpic.getDescription(), "Описания должны быть равны");
        assertEquals(epic.getStatus(), savedEpic.getStatus(), "Статусы должны быть равны");
        assertEquals(epic.getStartTime(), savedEpic.getStartTime(), "Время начала должно быть равно");
        assertEquals(epic.getDuration(), savedEpic.getDuration(), "Продолжительность должна быть равна");
        assertEquals(epic.getSubTaskIdList(), savedEpic.getSubTaskIdList(), "Списки подзадач должны быть равны");

    }

    //проверка на неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void fieldsBeEqualsAfterAddSubTaskInManager() {
        SubTask savedSubtask = taskManager.getSubTask(subtask.getId());

        assertEquals(subtask.getId(), savedSubtask.getId(), "Идентификаторы должны быть равны");
        assertEquals(subtask.getType(), savedSubtask.getType(), "Типы должны быть равны");
        assertEquals(subtask.getName(), savedSubtask.getName(), "Имена должны быть равны");
        assertEquals(subtask.getDescription(), savedSubtask.getDescription(), "Описания должны быть равны");
        assertEquals(subtask.getStatus(), savedSubtask.getStatus(), "Статусы должны быть равны");
        assertEquals(subtask.getStartTime(), savedSubtask.getStartTime(), "Время начала должно быть равно");
        assertEquals(subtask.getDuration(), savedSubtask.getDuration(), "Продолжительность должна быть равна");
        assertEquals(subtask.getEpicId(), savedSubtask.getEpicId(), "Эпики должны быть равны");

    }

    @Test
    public void deleteTasksOfAllTypesById() {
        taskManager.deleteSubTaskById(3);
        taskManager.deleteEpicById(2);
        taskManager.deleteTaskById(1);

        assertNull(taskManager.getSubTask(3), "Подзадача не удалена");
        assertNull(taskManager.getEpic(2), "Эпик не удален");
        assertNull(taskManager.getTask(1), "Задача не удалена");

    }

    @Test
    public void deleteTasksOfAllTypesByNonExistentId() {
        taskManager.deleteTaskById(4);
        assertEquals(1, taskManager.getListOfTasks().size(), "Размер списка задач не изменился");

        taskManager.deleteEpicById(4);
        assertEquals(1, taskManager.getListOfEpics().size(), "Размер списка эпиков не изменился");

        taskManager.deleteSubTaskById(4);
        assertEquals(1, taskManager.getListOfSubTask().size(), "Размер списка подзадач не изменился");

    }

    @Test
    public void deleteTasksOfAllTypesByIdFromEmptyList() {
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpic();
        taskManager.deleteAllSubTask();

        taskManager.deleteTaskById(4);
        assertEquals(0, taskManager.getListOfTasks().size(), "Размер списка задач не изменился");

        taskManager.deleteEpicById(4);
        assertEquals(0, taskManager.getListOfEpics().size(), "Размер списка эпиков не изменился");

        taskManager.deleteSubTaskById(4);
        assertEquals(0, taskManager.getListOfSubTask().size(), "Размер списка подзадач не изменился");

    }

    @Test
    public void taskShouldBeFirstInPriority() {
        Task newTask = new Task(4, Type.TASK, "NewTask", "NewTask description", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 0, 0), Duration.ofMinutes(15));
        List<Task> tasksByPriority = taskManager.getPrioritizedTasks(newTask);

        assertEquals(tasksByPriority.getFirst(), newTask, "Задача должна получить наивысший приоритет");

    }

    @Test
    public void taskShouldBeLastInPriority() {
        Task newTask = new Task(4, Type.TASK, "NewTask", "NewTask description", Status.NEW,
                LocalDateTime.of(2025, 5, 16, 0, 0), Duration.ofMinutes(15));
        List<Task> tasksByPriority = taskManager.getPrioritizedTasks(newTask);

        assertEquals(tasksByPriority.getLast(), newTask, "Задача должна получить низший приоритет");

    }

    @Test
    public void taskShouldBeSecondInPriority() {
        Task newTask = new Task(4, Type.TASK, "NewTask", "NewTask description", Status.NEW,
                LocalDateTime.of(2025, 5, 12, 0, 0), Duration.ofMinutes(15));
        List<Task> tasksByPriority = taskManager.getPrioritizedTasks(newTask);

        assertEquals(tasksByPriority.get(1), newTask, "Задача должна быть второй по приоритету");

    }

    @Test
    public void taskTimeSameOfExistingTask() {
        Task newTask = new Task(4, Type.TASK, "NewTask", "NewTask description", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 1, 0), Duration.ofMinutes(15));
        taskManager.createTask(newTask);

        assertEquals(1, taskManager.getListOfTasks().size(), "Список не должен увеличиваться");
        assertNull(taskManager.getTask(4), "Задача не должна быть добавлена");

    }

    @Test
    public void startTimeNewTaskInMiddleSavedTask() {
        Task newTask = new Task(4, Type.TASK, "NewTask", "NewTask description", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 1, 10), Duration.ofMinutes(15));
        taskManager.createTask(newTask);

        assertEquals(1, taskManager.getListOfTasks().size(), "Список не должен увеличиваться");
        assertNull(taskManager.getTask(4), "Задача не должна быть добавлена");

    }

    @Test
    public void EndTimeNewTaskInMiddleSavedTask() {
        Task newTask = new Task(4, Type.TASK, "NewTask", "NewTask description", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 0, 50), Duration.ofMinutes(15));
        taskManager.createTask(newTask);

        assertEquals(1, taskManager.getListOfTasks().size(), "Список не должен увеличиваться");
        assertNull(taskManager.getTask(4), "Задача не должна быть добавлена");

    }


}
