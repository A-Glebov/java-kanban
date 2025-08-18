package service;

import exceptions.ManagerSaveException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest {

    private FileBackedTaskManager fileBackedTaskManager;
    private Task task;
    private Epic epic;
    private SubTask subTask;

    @BeforeEach
    public void init() throws IOException {
        fileBackedTaskManager = Managers.getFileBacked();

    }

    @Test
    public void savingAndLoadingEmptyFile() throws IOException {
        fileBackedTaskManager.save();
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(fileBackedTaskManager.file);
        assertTrue(fileBackedTaskManager.file.exists(), "Файл не существует");
        assertTrue(fileBackedTaskManager.getListOfTasks().isEmpty(), "Файл не пустой");

    }

    @Test
    public void savingAndLoadingMultipleTasks() throws IOException {

        task = new Task(1, Type.TASK, "Task", "Task description", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 1, 0), Duration.ofMinutes(15));

        epic = new Epic(2, Type.EPIC, "Epic", "Description Epic", Status.NEW,
                LocalDateTime.of(2025, 5, 9, 12, 0), Duration.ofMinutes(20),
                new ArrayList<>());

        subTask = new SubTask(3, Type.SUBTASK, "Subtask Epic", "Description Sub Ep",
                Status.NEW, LocalDateTime.of(2025, 5, 15, 12, 53),
                Duration.ofMinutes(30), epic.getId());

        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.createEpic(epic);
        fileBackedTaskManager.createSubTask(subTask);

        fileBackedTaskManager.save();

        FileBackedTaskManager fileBackedTaskManagerFromFile = FileBackedTaskManager
                .loadFromFile(fileBackedTaskManager.file);

        assertEquals(fileBackedTaskManager.getTask(1), fileBackedTaskManagerFromFile.getTask(1), "Задачи не совпадают");
        assertEquals(fileBackedTaskManager.getEpic(2), fileBackedTaskManagerFromFile.getEpic(2), "Эпики не совпадают");
        assertEquals(fileBackedTaskManager.getSubTask(3), fileBackedTaskManagerFromFile.getSubTask(3), "Подзадачи не совпадают");

    }

    @Test
    public void testManagerSaveExceptionSaveToNonExistentFile() {
        fileBackedTaskManager.file = new File("C:\\non-existent-file.csv");

        assertThrows(ManagerSaveException.class, () -> fileBackedTaskManager
                .save(), "Сохранение данных в несуществующий файл должно приводить к исключению");

    }

    @Test
    public void testManagerSaveExceptionLoadFromNonExistentFile() {
        File nonExistentFile = new File("C:\\non-existent-file.csv");

        assertThrows(ManagerSaveException.class, () -> fileBackedTaskManager = FileBackedTaskManager
                .loadFromFile(nonExistentFile), "Загрузка данных из несуществующего файла должно приводить к исключению");

    }


}