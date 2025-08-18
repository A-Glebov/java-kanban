package model;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

   private final Task task1 = new Task(1, Type.TASK, "Task", "Task description", Status.NEW,
           LocalDateTime.of(2025,5,9,1,0), Duration.ofMinutes(15));
   private final Task task2 = new Task(1, Type.TASK, "Task2", "Task description2", Status.IN_PROGRESS,
           LocalDateTime.of(2025,5,9,2,30), Duration.ofMinutes(15));

    // Задачи равны если их идентификаторы равны
    @Test
    void tasksShouldBeEqualsIfIdEquals() {
        assertEquals(task1, task2);
    }

}