import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // Создаем менеджер
        InMemoryTaskManager taskManager = getInMemoryTaskManager();
        // Печатаем задачи всех типов и историю просмотров
        printAllTasks(taskManager);

    }

    // ТЕСТИРОВАНИЕ
    private static InMemoryTaskManager getInMemoryTaskManager() {

        InMemoryTaskManager taskManager = new InMemoryTaskManager();

        Task task1 = new Task(taskManager.getTaskId(), "Task 1", "Task 1 description", Status.NEW);
        Task task2 = new Task(taskManager.getTaskId(), "Task 2", "Task 2 description", Status.NEW);

        Epic epic1 = new Epic(taskManager.getTaskId(), "Epic1", "Description Epic1", Status.NEW, new ArrayList<>());
        SubTask subTask1Ep1 = new SubTask(taskManager.getTaskId(), "Subtask1, Epic1", "Description Sub1 Ep1", Status.NEW, epic1);
        SubTask subTask2Ep1 = new SubTask(taskManager.getTaskId(), "Subtask2, Epic1", "Description Sub2 Ep1", Status.NEW, epic1);

        Epic epic2 = new Epic(taskManager.getTaskId(), "Epic2", "Description Epic2", Status.NEW, new ArrayList<>());
        SubTask subTask1Ep2 = new SubTask(taskManager.getTaskId(), "Subtask1, Epic2", "Description Sub2 Ep1", Status.NEW, epic2);

        // Создание задач, подзадач, эпиков в менеджере
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createEpic(epic1);
        taskManager.createSubTask(subTask1Ep1);
        taskManager.createSubTask(subTask2Ep1);
        taskManager.createEpic(epic2);
        taskManager.createSubTask(subTask1Ep2);

        //Запрашиваем задачи по ID
        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getEpic(3);
        taskManager.getSubTask(4);
        taskManager.getTask(1);
        return taskManager;
    }

    // Печатаем задачи всех типов и историю просмотров
    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getListOfTasks().values()) {
            System.out.println(task);
        }

        System.out.println("Эпики:");
        for (Epic epic : manager.getListOfEpics().values()) {
            System.out.println(epic);

            for (SubTask task : manager.getListOfSubtaskOfEpic(epic)) {
                System.out.println("--> " + task);
            }
        }

        System.out.println("Подзадачи:");
        for (SubTask subtask : manager.getListOfSubTask().values()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }


}
