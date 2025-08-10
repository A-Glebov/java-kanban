import model.*;
import service.FileBackedTaskManager;
import service.Managers;

import java.io.IOException;
import java.util.ArrayList;

import static service.FileBackedTaskManager.loadFromFile;

public class Main {

    public static void main(String[] args) throws IOException {

        // Пользовательский сценарий
        FileBackedTaskManager fileBackedTaskManager = Managers.getFileBacked();

        Task task = new Task(1, Type.TASK, "Task", "Task description", Status.NEW);
        Epic epic = new Epic(2, Type.EPIC, "Epic", "Description Epic", Status.NEW, new ArrayList<>());
        SubTask subTask = new SubTask(3, Type.SUBTASK, "Subtask Epic", "Description Sub Ep", Status.NEW, epic.getId());

        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.createEpic(epic);
        fileBackedTaskManager.createSubTask(subTask);

        fileBackedTaskManager.save();

        FileBackedTaskManager fileBackedTaskManagerFromFile = loadFromFile(fileBackedTaskManager.file);

        System.out.println(fileBackedTaskManagerFromFile.getTask(1));
        System.out.println(fileBackedTaskManagerFromFile.getEpic(2));
        System.out.println(fileBackedTaskManagerFromFile.getSubTask(3));

    }


}
