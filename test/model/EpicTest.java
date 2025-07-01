package model;

import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class EpicTest {

    TaskManager taskManager = new InMemoryTaskManager();
    Epic epic1 = new Epic(1, "Epic1", "Description Epic1", Status.NEW, new ArrayList<>());
    Epic epic2 = new Epic(1, "Epic2", "Description Epic2", Status.NEW, new ArrayList<>());

    // Эпики равны если равен их идентификаторы равны
    @Test
    void epicsShouldBeEqualsIfIdEquals() {
        assertEquals(epic1, epic2);
    }

    // Проверка, что эпик нельзя добавить в самого себя в виде подзадачи
    @Test
    void epicCannotContainItselfAsSubTask() {
        Epic epic = new Epic(1, "Epic", "Description", Status.NEW, new ArrayList<>());
        SubTask validSubTask = new SubTask(2, "SubTaskValid", "Valid", Status.NEW, epic.getId());
        SubTask invalidSubTask = new SubTask(epic.getId(), "SubTaskInvalid", "Invalid", Status.NEW, epic.getId());

        ArrayList<Integer> subTasks = new ArrayList<>();
        subTasks.add(validSubTask.getId());
        subTasks.add(invalidSubTask.getId());

        taskManager.updateEpic(new Epic(1, "Epic", "Description Epic1", Status.NEW, subTasks));

        for (Integer sub : epic.getSubTaskIdList()) {
            assertNotEquals(epic.getId(), sub, "Epic не должен содержать сам себя");
        }

    }


}