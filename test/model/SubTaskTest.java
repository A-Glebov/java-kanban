package model;

import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class SubTaskTest {

  private final TaskManager taskManager = Managers.getDefault();

    Epic epic = new Epic(1,Type.EPIC, "epic", "epic desc",Status.IN_PROGRESS,
                    LocalDateTime.of(2025, 8, 17, 0, 0),
                Duration.ofMinutes(15), new ArrayList<>());

    SubTask subtask1 = new SubTask(2, Type.SUBTASK, "Subtask1 Epic", "Desc Sub1 Ep", Status.NEW,
                           LocalDateTime.of(2025, 8, 17, 1, 0), Duration.ofMinutes(30), 1);

    SubTask subtask2 = new SubTask(3, Type.SUBTASK, "Subtask2 Epic", "Desc Sub2 Ep", Status.NEW,
                           LocalDateTime.of(2025, 8, 17, 2, 0), Duration.ofMinutes(30), 1);

    // Подзадачи равны, если их идентификаторы равны
    @Test
    void subTasksShouldBeEqualsIfIdEquals() {
        subtask2.setId(2);
        assertEquals(subtask1, subtask2);
    }

    // Подзадачу нельзя сделать своим же эпиком
    @Test
    void subtaskCannotBeItsOwnEpic() {
        Epic epic = new Epic(1,Type.EPIC, "epic", "epic desc",Status.IN_PROGRESS,
                LocalDateTime.of(2025, 8, 17, 0, 0),
                Duration.ofMinutes(15), new ArrayList<>());

        SubTask subTask = new SubTask(2, Type.SUBTASK, "Subtask1 Epic", "Desc Sub1 Ep", Status.NEW,
                LocalDateTime.of(2025, 8, 17, 1, 0), Duration.ofMinutes(30), 1);

        assertNotEquals(subTask.getId(), subTask.getEpicId(), "Subtask не может ссылаться на себя как на эпик");
    }

    @Test
    void subtasksMustHaveSameEpic () {
        taskManager.createEpic(epic);
        taskManager.createSubTask(subtask1);
        taskManager.createSubTask(subtask2);

        int epicIdOfSubtask1 = taskManager.getSubTask(2).getEpicId();
        int epicIdOfSubtask2 = taskManager.getSubTask(3).getEpicId();


        assertSame(epicIdOfSubtask1, epicIdOfSubtask2);

    }


}