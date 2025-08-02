package service;

import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;

public class FileBackedTaskManager extends InMemoryTaskManager {
    File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static void main(String[] args) throws IOException {

        // Пользовательский сценарий
        File file = File.createTempFile("file-backed-task-manager", ".csv");
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        Task task = new Task(1, Type.TASK, "Task", "Task description", Status.NEW);
        Epic epic = new Epic(2, Type.EPIC, "Epic", "Description Epic", Status.NEW, new ArrayList<>());
        SubTask subTask = new SubTask(3, Type.SUBTASK, "Subtask Epic", "Description Sub Ep", Status.NEW, epic.getId());

        fileBackedTaskManager.createTask(task);
        fileBackedTaskManager.createEpic(epic);
        fileBackedTaskManager.createSubTask(subTask);

        fileBackedTaskManager.save();

        FileBackedTaskManager fileBackedTaskManagerFromFile = loadFromFile(file);

        System.out.println(fileBackedTaskManagerFromFile.getTask(1));
        System.out.println(fileBackedTaskManagerFromFile.getEpic(2));
        System.out.println(fileBackedTaskManagerFromFile.getSubTask(3));

    }

    static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(file);

        try {
            String allTasks = Files.readString(file.toPath());
            String[] tasks = allTasks.split("\n");

            for (int i = 1; i < tasks.length; i++) {
                Task task = fileBackedTaskManager.fromString(tasks[i]);

                switch (task.getType()) {
                    case TASK -> fileBackedTaskManager.createTask(task);
                    case EPIC -> fileBackedTaskManager.createEpic((Epic) task);
                    case SUBTASK -> fileBackedTaskManager.createSubTask((SubTask) task);
                }

            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка во время чтения из файла");
        }

        return fileBackedTaskManager;

    }


    public void save() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(getListOfTasks());
        allTasks.addAll(getListOfEpics());
        allTasks.addAll(getListOfSubTask());

        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epic" + '\n');

            for (Task task : allTasks) {
                fileWriter.write(toString(task) + '\n');
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при записи в файл");
        }

    }

    String toString(Task task) {
        String result = String.format("%s,%s,%s,%s,%s,", task.getId(), task.getType(), task.getName(), task.getDescription(), task.getStatus());

        if (task.getType() == Type.SUBTASK) {
            result = result + String.format("%s,", ((SubTask) task).getEpicId());
        }

        return result;
    }

    Task fromString(String value) {
        String[] values = value.split(",");
        int id = Integer.parseInt(values[0]);
        Type type = Type.valueOf(values[1]);
        String name = values[2];
        String description = values[3];
        Status status = Status.valueOf(values[4]);

        if (type == Type.TASK) {
            return new Task(id, type, name, description, status);
        } else if (type == Type.EPIC) {
            return new Epic(id, type, name, description, status, new ArrayList<>());
        } else if (type == Type.SUBTASK) {
            int epicId = Integer.parseInt(values[5]);
            return new SubTask(id, type, name, description, status, epicId);
        } else {
            return null;
        }

    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpic() {
        super.deleteAllEpic();
        save();
    }

    @Override
    public void deleteAllSubTask() {
        super.deleteAllSubTask();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(int taskId) {
        super.deleteTaskById(taskId);
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteSubTaskById(int subTaskId) {
        super.deleteSubTaskById(subTaskId);
        save();
    }

}
