package service;

import exceptions.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @Test
    @BeforeEach
    @Override
    public void init() throws IOException {
        taskManager = Managers.getFileBacked();
        super.init();

    }

    @Test
    public void savingAndLoadingEmptyFile() throws IOException {
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpic();
        taskManager.deleteAllSubTask();

        taskManager.save();
        taskManager = FileBackedTaskManager.loadFromFile(taskManager.file);
        assertTrue(taskManager.file.exists(), "Файл не существует");
        assertTrue(taskManager.getListOfTasks().isEmpty(), "Файл не пустой");

    }

    @Test
    public void savingAndLoadingMultipleTasks() throws IOException {
        taskManager.save();

        FileBackedTaskManager fileBackedTaskManagerFromFile = FileBackedTaskManager
                .loadFromFile(taskManager.file);

        assertEquals(taskManager.getTask(1), fileBackedTaskManagerFromFile.getTask(1), "Задачи не совпадают");
        assertEquals(taskManager.getEpic(2), fileBackedTaskManagerFromFile.getEpic(2), "Эпики не совпадают");
        assertEquals(taskManager.getSubTask(3), fileBackedTaskManagerFromFile.getSubTask(3), "Подзадачи не совпадают");

    }

    @Test
    public void testManagerSaveExceptionLoadFromNonExistentFile() {
        File nonExistentFile = new File("non-existent-file.csv");
        assertThrows(ManagerSaveException.class, () -> taskManager = FileBackedTaskManager
                .loadFromFile(nonExistentFile), "Загрузка данных из несуществующего файла должно приводить к исключению");

    }

}