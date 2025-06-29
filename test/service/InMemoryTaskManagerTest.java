package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager taskManager;
    Task task;
    Epic epic;
    SubTask subTask;

    @BeforeEach
    void init() {
        taskManager = Managers.getDefault();
        task = new Task(taskManager.getTaskId(), "Task", "Task description", Status.NEW);
        taskManager.createTask(task);
        epic = new Epic(taskManager.getTaskId(), "Epic", "Description Epic", Status.NEW, new ArrayList<>());
        taskManager.createEpic(epic);
        subTask = new SubTask(taskManager.getTaskId(), "Subtask, Epic", "Description Sub Ep", Status.NEW, epic);
        taskManager.createSubTask(subTask);

    }

    // InMemoryTaskManager добавляет задачи разного типа и может найти их по id;
    @Test
    void addNewTask() {
        int taskId = task.getId();
        final Task savedTask = taskManager.getTask(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final HashMap<Integer, Task> tasks = taskManager.getListOfTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(taskId), "Задачи не совпадают.");


    }

    // InMemoryTaskManager добавляет задачи разного типа и может найти их по id;
    @Test
    void addNewEpic() {
        int epicId = epic.getId();
        final Epic savedEpic = taskManager.getEpic(epicId);

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(epic, savedEpic, "Задачи не совпадают.");

        final HashMap<Integer, Epic> epics = taskManager.getListOfEpics();
        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(epicId), "Задачи не совпадают.");

    }

    // InMemoryTaskManager добавляет задачи разного типа и может найти их по id;
    @Test
    void addNewSubtask() {
        int epicId = epic.getId();
        int subTaskId = subTask.getId();

        final SubTask savedSubTask = taskManager.getSubTask(subTaskId);

        // для задач
        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(subTask, savedSubTask, "Задачи не совпадают.");

        final HashMap<Integer, SubTask> subTasks = taskManager.getListOfSubTask();
        assertNotNull(subTasks, "Задачи не возвращаются.");
        assertEquals(1, subTasks.size(), "Неверное количество задач.");
        assertEquals(subTask, subTasks.get(subTaskId), "Задачи не совпадают.");

    }

    // Тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    void fieldsBeEqualsAfterAddTaskInManager() {
        Task addedTask = taskManager.getTask(task.getId());

        assertEquals(task.getId(), addedTask.getId(), "Идентификаторы должны быть равны");
        assertEquals(task.getName(), addedTask.getName(), "Имена должны быть равны");
        assertEquals(task.getDescription(), addedTask.getDescription(), "Описания должны быть равны");
        assertEquals(task.getStatus(), addedTask.getStatus(), "Статусы должны быть равны");

    }

    // Тест, в котором проверяется неизменность эпика (по всем полям) при добавлении эпика в менеджер
    @Test
    void fieldsBeEqualsAfterAddEpicInManager() {
        Epic addedEpic = taskManager.getEpic(epic.getId());

        assertEquals(epic.getId(), addedEpic.getId(), "Идентификаторы должны быть равны");
        assertEquals(epic.getName(), addedEpic.getName(), "Имена должны быть равны");
        assertEquals(epic.getDescription(), addedEpic.getDescription(), "Описания должны быть равны");
        assertEquals(epic.getStatus(), addedEpic.getStatus(), "Статусы должны быть равны");
        assertEquals(epic.getSubTaskIdList(), addedEpic.getSubTaskIdList(), "Списки подзадач должны быть равны");

    }

    //проверка на неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    void fieldsBeEqualsAfterAddSubTaskInManager() {
        SubTask addedTask = taskManager.getSubTask(subTask.getId());

        assertEquals(subTask.getId(), addedTask.getId(), "Идентификаторы должны быть равны");
        assertEquals(subTask.getName(), addedTask.getName(), "Имена должны быть равны");
        assertEquals(subTask.getDescription(), addedTask.getDescription(), "Описания должны быть равны");
        assertEquals(subTask.getStatus(), addedTask.getStatus(), "Статусы должны быть равны");
        assertEquals(subTask.getEpic(), addedTask.getEpic(), "Эпики должны быть равны");

    }

    // задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера;
    @Test
    void manualAndAutoGeneratedIdsDoNotConflict() {
        int manualId = 100;
        // Создаем задачу с заданным ID
        Task manualTask = new Task(manualId, "manualTask", "Description", Status.NEW);
        taskManager.createTask(manualTask);
        assertNotEquals(task, manualTask, "ID не должны совпадать");
    }

}