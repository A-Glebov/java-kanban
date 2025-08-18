package model;

import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class EpicTest {

    private final TaskManager taskManager = Managers.getDefault();

    private Epic epic;
    private SubTask subtask1;
    private SubTask subtask2;

    @Test
    public void epicStatusMustBeNew() {
        epic = new Epic(1, Type.EPIC, "epic", "epic desc", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 8, 17, 0, 0),
                Duration.ofMinutes(15), new ArrayList<>());

        subtask1 = new SubTask(2, Type.SUBTASK, "Subtask1 Epic", "Desc Sub1 Ep", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 1, 0), Duration.ofMinutes(30), 1);

        subtask2 = new SubTask(3, Type.SUBTASK, "Subtask2 Epic", "Desc Sub2 Ep", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 2, 0), Duration.ofMinutes(30), 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);

        assertSame(Status.NEW, epic.status);

    }

    @Test
    public void epicStatusMustBeDone() {
        epic = new Epic(1, Type.EPIC, "epic", "epic desc", Status.IN_PROGRESS,
                LocalDateTime.of(2025, 8, 17, 0, 0),
                Duration.ofMinutes(15), new ArrayList<>());

        subtask1 = new SubTask(2, Type.SUBTASK, "Subtask1 Epic", "Desc Sub1 Ep", Status.DONE,
                LocalDateTime.of(2025, 8, 17, 1, 0), Duration.ofMinutes(30), 1);

        subtask2 = new SubTask(3, Type.SUBTASK, "Subtask2 Epic", "Desc Sub2 Ep", Status.DONE,
                LocalDateTime.of(2025, 8, 17, 2, 0), Duration.ofMinutes(30), 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);

        assertSame(Status.DONE, epic.status);

    }

    @Test
    public void epicStatusMustBeInProgress() {
        epic = new Epic(1, Type.EPIC, "epic", "epic desc", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 0, 0),
                Duration.ofMinutes(15), new ArrayList<>());

        subtask1 = new SubTask(2, Type.SUBTASK, "Subtask1 Epic", "Desc Sub Ep", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 1, 0), Duration.ofMinutes(30), 1);

        subtask2 = new SubTask(3, Type.SUBTASK, "Subtask2 Epic", "Desc Sub Ep", Status.DONE,
                LocalDateTime.of(2025, 8, 17, 2, 0), Duration.ofMinutes(30), 1);

        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);

        assertSame(Status.IN_PROGRESS, epic.status);

        taskManager.updateEpic(new Epic(1, Type.EPIC, "epic", "epic desc", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 0, 0), Duration.ofMinutes(15),
                new ArrayList<>()));

        taskManager.updateSubTask(new SubTask(2, Type.SUBTASK, "Subtask Epic", "Desc Sub Ep",
                Status.IN_PROGRESS, LocalDateTime.of(2025, 8, 18, 1, 0),
                Duration.ofMinutes(30), 1));

        taskManager.updateSubTask(new SubTask(3, Type.SUBTASK, "Subtask Epic", "Desc Sub Ep",
                Status.IN_PROGRESS, LocalDateTime.of(2025, 8, 17, 1, 0),
                Duration.ofMinutes(30), 1));

        assertSame(Status.IN_PROGRESS, epic.status);

    }

    // Эпики равны если равен их идентификаторы равны
    @Test
    public void epicsShouldBeEqualsIfIdEquals() {
        final Epic epic1 = new Epic(1, Type.EPIC, "epic1", "epic desc", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 0, 0), Duration.ofMinutes(15),
                new ArrayList<>());

        final Epic epic2 = new Epic(1, Type.EPIC, "epic2", "epic1 desc", Status.DONE,
                LocalDateTime.of(2025, 8, 18, 0, 0), Duration.ofMinutes(10),
                new ArrayList<>());

        assertEquals(epic1, epic2);

    }

    // Проверка, что эпик нельзя добавить в самого себя в виде подзадачи
    @Test
    public void epicCannotContainItselfAsSubTask() {
        Epic epic = new Epic(1, Type.EPIC, "Epic", "Description", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 1, 30), Duration.ofMinutes(15),
                new ArrayList<>());

        SubTask validSubTask = new SubTask(2, Type.SUBTASK, "SubTaskValid", "Valid", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 1, 0),
                Duration.ofMinutes(30), epic.getId());

        SubTask invalidSubTask = new SubTask(epic.getId(), Type.SUBTASK, "SubTaskInvalid", "Invalid",
                Status.NEW, LocalDateTime.of(2025, 8, 17, 2, 0),
                Duration.ofMinutes(30), epic.getId());

        ArrayList<Integer> subTasks = new ArrayList<>();
        subTasks.add(validSubTask.getId());
        subTasks.add(invalidSubTask.getId());

        taskManager.updateEpic(new Epic(1, Type.EPIC, "Epic", "Description", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 1, 30), Duration.ofMinutes(15),
                subTasks));

        for (Integer sub : epic.getSubTaskIdList()) {
            assertNotEquals(epic.getId(), sub, "Epic не должен содержать сам себя");
        }

    }


}