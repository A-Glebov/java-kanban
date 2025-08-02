package service;

import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest {

    File file;
    FileBackedTaskManager fileBackedTaskManager;
    Task task;
    Epic epic;
    SubTask subTask;

    @BeforeEach
    void init() throws IOException {
        file = File.createTempFile("file-backed-task-manager", ".csv");
        fileBackedTaskManager = new FileBackedTaskManager(file);

    }

    @Test
    void savingAndLoadingEmptyFile() {
        fileBackedTaskManager.save();
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        assertTrue(file.exists(), "Файл не существует");
        assertTrue(fileBackedTaskManager.getListOfTasks().isEmpty(), "Файл не пустой");

    }

    @Test
    void savingAndLoadingMultipleTasks() {
        task = new Task(1, Type.TASK, "Task", "Task description", Status.NEW);
        epic = new Epic(2, Type.EPIC, "Epic", "Description Epic", Status.NEW, new ArrayList<>());
        subTask = new SubTask(3, Type.SUBTASK, "Subtask Epic", "Description Sub Ep", Status.NEW, epic.getId());

        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.createEpic(epic);
        fileBackedTaskManager.createSubTask(subTask);

        fileBackedTaskManager.save();

        FileBackedTaskManager fileBackedTaskManagerFromFile = FileBackedTaskManager.loadFromFile(file);

        assertEquals(fileBackedTaskManager.getTask(1), fileBackedTaskManagerFromFile.getTask(1), "Задачи не совпадают");
        assertEquals(fileBackedTaskManager.getEpic(2), fileBackedTaskManagerFromFile.getEpic(2), "Эпики не совпадают");
        assertEquals(fileBackedTaskManager.getSubTask(3), fileBackedTaskManagerFromFile.getSubTask(3), "Подзадачи не совпадают");

    }

}