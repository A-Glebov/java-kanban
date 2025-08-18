package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest {

    private TaskManager taskManager;

    private Task task1;
    private Task task2;
    private Task task;

    private Epic epic;
    private SubTask subtask1;
    private SubTask subtask2;
    private SubTask subTask;

    @BeforeEach
    public void init() {
        taskManager = Managers.getDefault();

        task = new Task(1, Type.TASK, "Task", "Task description", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 1, 0), Duration.ofMinutes(15));

        epic = new Epic(2, Type.EPIC, "Epic", "Description Epic", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 12, 0), Duration.ofMinutes(20),
                new ArrayList<>());

        subTask = new SubTask(3, Type.SUBTASK, "Subtask Epic", "Description Sub Ep",
                Status.NEW, LocalDateTime.of(2025, 5, 15, 12, 53),
                Duration.ofMinutes(30), epic.getId());

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubTask(subTask);

    }

    @Test
    public void addNewTask() {
        int taskId = task.getId();
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(taskId), "Задачи не совпадают.");

    }

    // InMemoryTaskManager добавляет задачи разного типа и может найти их по id;
    @Test
    public void addNewEpic() {
        int epicId = epic.getId();
        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final HashMap<Integer, Epic> epics = taskManager.getEpics();
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(epicId), "Задачи не совпадают.");

    }

    // InMemoryTaskManager добавляет задачи разного типа и может найти их по id;
    @Test
    public void addNewSubtask() {
        int subTaskId = subTask.getId();
        final SubTask savedSubTask = taskManager.getSubTask(subTaskId);

        // для задач
        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");

        final HashMap<Integer, SubTask> subTasks = taskManager.getSubtasks();

        assertNotNull(subTasks, "Задачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTask, subTasks.get(subTaskId), "Задачи не совпадают.");

    }

    // Тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void fieldsBeEqualsAfterAddTaskInManager() {
        Task addedTask = taskManager.getTask(task.getId());

        assertEquals(task.getId(), addedTask.getId(), "Идентификаторы должны быть равны");
        assertEquals(task.getType(), addedTask.getType(), "Типы должны быть равны");
        assertEquals(task.getName(), addedTask.getName(), "Имена должны быть равны");
        assertEquals(task.getDescription(), addedTask.getDescription(), "Описания должны быть равны");
        assertEquals(task.getStatus(), addedTask.getStatus(), "Статусы должны быть равны");
        assertEquals(task.getStartTime(), addedTask.getStartTime(), "Время начала должно быть равно");
        assertEquals(task.getDuration(), addedTask.getDuration(), "Продолжительность должна быть равна");

    }

    // Тест, в котором проверяется неизменность эпика (по всем полям) при добавлении эпика в менеджер
    @Test
    public void fieldsBeEqualsAfterAddEpicInManager() {
        Epic addedEpic = taskManager.getEpic(epic.getId());

        assertEquals(epic.getId(), addedEpic.getId(), "Идентификаторы должны быть равны");
        assertEquals(epic.getType(), addedEpic.getType(), "Типы должны быть равны");
        assertEquals(epic.getName(), addedEpic.getName(), "Имена должны быть равны");
        assertEquals(epic.getDescription(), addedEpic.getDescription(), "Описания должны быть равны");
        assertEquals(epic.getStatus(), addedEpic.getStatus(), "Статусы должны быть равны");
        assertEquals(epic.getStartTime(), addedEpic.getStartTime(), "Время начала должно быть равно");
        assertEquals(epic.getDuration(), addedEpic.getDuration(), "Продолжительность должна быть равна");
        assertEquals(epic.getSubTaskIdList(), addedEpic.getSubTaskIdList(), "Списки подзадач должны быть равны");

    }

    //проверка на неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void fieldsBeEqualsAfterAddSubTaskInManager() {
        SubTask addedTask = taskManager.getSubTask(subTask.getId());

        assertEquals(subTask.getId(), addedTask.getId(), "Идентификаторы должны быть равны");
        assertEquals(subTask.getType(), addedTask.getType(), "Типы должны быть равны");
        assertEquals(subTask.getName(), addedTask.getName(), "Имена должны быть равны");
        assertEquals(subTask.getDescription(), addedTask.getDescription(), "Описания должны быть равны");
        assertEquals(subTask.getStatus(), addedTask.getStatus(), "Статусы должны быть равны");
        assertEquals(subTask.getStartTime(), addedTask.getStartTime(), "Время начала должно быть равно");
        assertEquals(subTask.getDuration(), addedTask.getDuration(), "Продолжительность должна быть равна");
        assertEquals(subTask.getEpicId(), addedTask.getEpicId(), "Эпики должны быть равны");

    }

    // задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    @Test
    public void manualAndAutoGeneratedIdsDoNotConflict() {
        Task manualTask = new Task(100, Type.TASK, "Task", "Task description", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 1, 0), Duration.ofMinutes(15));

        taskManager.createTask(manualTask);

        task = new Task(taskManager.getTaskId(), Type.TASK, "Task", "Task description", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 1, 0), Duration.ofMinutes(15));

        assertNotEquals(task, manualTask, "ID не должны совпадать");

    }

    // Эпик не должен содержать удаленных подзадач
    @Test
    public void epicShouldNotContainDeletedSubtasks() {
        taskManager.deleteSubTaskById(subTask.getId());
        assertTrue(epic.getSubTaskIdList().isEmpty());
    }

    @Test
    public void taskTimeSameOfExistingTask() {

        task1 = new Task(1, Type.TASK, "task1", "Desc task1", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 1, 0), Duration.ofMinutes(30));

        task2 = new Task(2, Type.TASK, "task2", "Desc task2", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 1, 0), Duration.ofMinutes(30));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        assertFalse(taskManager.getTasks().containsKey(2), "Задача не должна быть добавлена");

    }

    @Test
    public void startTimeTaskInMiddleOfExistingTask() {
        task1 = new Task(1, Type.TASK, "task1", "Desc task1", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 1, 0), Duration.ofMinutes(30));

        task2 = new Task(2, Type.TASK, "task2", "Desc task2", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 1, 15), Duration.ofMinutes(30));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        assertFalse(taskManager.getTasks().containsKey(2), "Задача не должна быть добавлена");
    }

    @Test
    public void endTimeTaskInMiddleOfExistingTask() {
        task1 = new Task(1, Type.TASK, "task1", "Desc task1", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 1, 0), Duration.ofMinutes(30));

        task2 = new Task(2, Type.TASK, "task2", "Desc task2", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 0, 45), Duration.ofMinutes(30));

        taskManager.createTask(task1);
        taskManager.createTask(task2);

        assertFalse(taskManager.getTasks().containsKey(2), "Задача не должна быть добавлена");
    }


    @Test
    public void subtaskTimeSameOfExistingSubtask() {

        epic = new Epic(1, Type.EPIC, "epic", "epic desc", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 0, 0),
                Duration.ofMinutes(15), new ArrayList<>());


        subtask1 = new SubTask(2, Type.SUBTASK, "Subtask1 Epic", "Desc Sub Ep", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 1, 0), Duration.ofMinutes(30), 1);

        subtask2 = new SubTask(4, Type.SUBTASK, "Subtask2 Epic", "Desc Sub Ep", Status.DONE,
                LocalDateTime.of(2025, 8, 17, 1, 0), Duration.ofMinutes(30), 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);

        assertFalse(taskManager.getSubtasks().containsKey(4), "Задача не должна быть добавлена");

    }


    @Test
    public void startTimeSubtaskInMiddleOfExistingSubtask() {
        epic = new Epic(1, Type.EPIC, "epic", "epic desc", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 0, 0),
                Duration.ofMinutes(15), new ArrayList<>());


        subtask1 = new SubTask(2, Type.SUBTASK, "Subtask1 Epic", "Desc Sub Ep", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 1, 0), Duration.ofMinutes(30), 1);

        subtask2 = new SubTask(4, Type.SUBTASK, "Subtask2 Epic", "Desc Sub Ep", Status.DONE,
                LocalDateTime.of(2025, 8, 17, 1, 15), Duration.ofMinutes(30), 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);

        assertFalse(taskManager.getSubtasks().containsKey(4), "Задача не должна быть добавлена");
    }

    @Test
    public void endTimeSubtaskInMiddleOfExistingSubtask() {
        epic = new Epic(1, Type.EPIC, "epic", "epic desc", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 0, 0),
                Duration.ofMinutes(15), new ArrayList<>());


        subtask1 = new SubTask(2, Type.SUBTASK, "Subtask1 Epic", "Desc Sub Ep", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 1, 0), Duration.ofMinutes(30), 1);

        subtask2 = new SubTask(4, Type.SUBTASK, "Subtask2 Epic", "Desc Sub Ep", Status.DONE,
                LocalDateTime.of(2025, 8, 17, 0, 45), Duration.ofMinutes(30), 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);

        assertFalse(taskManager.getSubtasks().containsKey(4), "Задача не должна быть добавлена");
    }


}