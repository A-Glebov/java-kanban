import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // Создаем менеджер
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task(taskManager.getTaskId(), "Task 1", "Task 1 description", Status.NEW);
        Task task2 = new Task(taskManager.getTaskId(), "Task 2", "Task 2 description", Status.NEW);

        Epic epic1 = new Epic(taskManager.getTaskId(), "Epic1", "Description Epic1", Status.NEW, new ArrayList<>());
        SubTask subTask1Ep1 = new SubTask(taskManager.getTaskId(), "Subtask1, Epic1", "Description Sub1 Ep1", Status.NEW, epic1.getId());
        SubTask subTask2Ep1 = new SubTask(taskManager.getTaskId(), "Subtask2, Epic1", "Description Sub2 Ep1", Status.NEW, epic1.getId());
        SubTask subTask3Ep1 = new SubTask(taskManager.getTaskId(), "Subtask3, Epic1", "Description Sub3 Ep1", Status.NEW, epic1.getId());

        Epic epic2 = new Epic(taskManager.getTaskId(), "Epic2", "Description Epic2", Status.NEW, new ArrayList<>());

        // Создание задач, подзадач, эпиков в менеджере
        taskManager.createTask(task1);
        taskManager.createTask(task2);

        taskManager.createEpic(epic1);
        taskManager.createSubTask(subTask1Ep1);
        taskManager.createSubTask(subTask2Ep1);
        taskManager.createSubTask(subTask3Ep1);

        taskManager.createEpic(epic2);

        //Запрашиваем задачи по ID
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getSubTask(4);

        // Печатаем задачи всех типов и историю просмотров
        printAllTasks(taskManager);
        printHistory(taskManager);

        taskManager.getTask(1);
        taskManager.getEpic(3);
        taskManager.getSubTask(4);
        taskManager.getEpic(7);

        // Печатаем задачи всех типов и историю просмотров
        printAllTasks(taskManager);
        printHistory(taskManager);

        // Удаляем задачу, которая есть в истории
        taskManager.deleteTaskById(1);
        System.out.println("Удалили task1, которая есть в истории");
        printHistory(taskManager);

        // Удаляем эпик с тремя подзадачами
        taskManager.deleteEpicById(3);
        System.out.println("Удалили epic1 с тремя задачами");
        printHistory(taskManager);

    }


    // Печать задач всех типов
    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getListOfTasks()) {
            System.out.println(task);
        }

        System.out.println("Эпики:");
        for (Epic epic : manager.getListOfEpics()) {
            System.out.println(epic);

            for (SubTask task : manager.getListOfSubtaskOfEpic(epic)) {
                System.out.println("--> " + task);
            }

        }

        System.out.println("Подзадачи:");
        for (SubTask subtask : manager.getListOfSubTask()) {
            System.out.println(subtask);
        }
        System.out.println();

    }

    // Печать истории
    private static void printHistory(TaskManager manager) {
        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println();

    }


}
