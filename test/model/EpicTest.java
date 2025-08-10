package model;

import org.junit.jupiter.api.Test;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EpicTest {

    private final TaskManager taskManager = new InMemoryTaskManager();
    private final Epic epic1 = new Epic(1, Type.EPIC, "Epic1", "Description Epic1", Status.NEW, new ArrayList<>());
    private final Epic epic2 = new Epic(1, Type.EPIC, "Epic2", "Description Epic2", Status.NEW, new ArrayList<>());

    // Эпики равны если равен их идентификаторы равны
    @Test
    public void epicsShouldBeEqualsIfIdEquals() {
        assertEquals(epic1, epic2);
    }

    // Проверка, что эпик нельзя добавить в самого себя в виде подзадачи
    @Test
    public void epicCannotContainItselfAsSubTask() {
        Epic epic = new Epic(1, Type.EPIC, "Epic", "Description", Status.NEW, new ArrayList<>());
        SubTask validSubTask = new SubTask(2, Type.SUBTASK, "SubTaskValid", "Valid", Status.NEW, epic.getId());
        SubTask invalidSubTask = new SubTask(epic.getId(), Type.SUBTASK, "SubTaskInvalid", "Invalid", Status.NEW, epic.getId());

        ArrayList<Integer> subTasks = new ArrayList<>();
        subTasks.add(validSubTask.getId());
        subTasks.add(invalidSubTask.getId());

        taskManager.updateEpic(new Epic(1, Type.EPIC, "Epic", "Description Epic1", Status.NEW, subTasks));

        for (Integer sub : epic.getSubTaskIdList()) {
            assertNotEquals(epic.getId(), sub, "Epic не должен содержать сам себя");
        }

    }


}