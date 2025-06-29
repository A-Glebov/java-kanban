package model;

import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {

    TaskManager taskManager = new InMemoryTaskManager();

    Epic epic1 = new Epic(taskManager.getTaskId(), "Epic1", "Description Epic1", Status.NEW, new ArrayList<>());
    SubTask subTask1 = new SubTask(1, "Subtask1, Epic1", "Description Sub1 Ep1", Status.NEW, epic1);
    SubTask subTask2 = new SubTask(1, "Subtask2, Epic1", "Description Sub2 Ep1", Status.NEW, epic1);

    // Подзадачи равны, если их идентификаторы равны
    @Test
    void subTasksShouldBeEqualsIfIdEquals() {
        assertEquals(subTask1, subTask2);
    }

    // Подзадачу нельзя сделать своим же эпиком
    @Test
    void subtaskCannotBeItsOwnEpic() {
        Epic epic = new Epic(taskManager.getTaskId(), "Epic", "Description", Status.NEW, new ArrayList<>());
        SubTask subTask = new SubTask(taskManager.getTaskId(), "subTask", "Description", Status.NEW, epic);
        assertNotEquals(subTask.getId(), subTask.getEpic().getId(), "Subtask не может ссылаться на себя как на эпик");
    }


}