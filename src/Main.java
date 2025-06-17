import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        // ТЕСТИРОВАНИЕ

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

        System.out.println(taskManager.getListOfTasks());
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfSubTask());
        System.out.println();

        //Обновление статусов
        taskManager.updateTask(new Task(task1.getId(), "Task 1", "Task 1 description", Status.IN_PROGRESS)); //обновили статус задачи 1
        taskManager.updateTask(new Task(task2.getId(), "Task 2", "Task 2 description", Status.DONE)); //обновили статус задачи 2
        taskManager.updateSubTask(new SubTask(subTask1Ep1.getId(), "Subtask1, Epic1", "Description Sub1 Ep1", Status.IN_PROGRESS, epic1)); //обновили статус подзадачи 1 эпика 1
        taskManager.updateSubTask(new SubTask(subTask1Ep2.getId(), "Subtask1, Epic2", "Description Sub2 Ep2", Status.DONE, epic2)); // обновили статус подзадачи 2 эпика 2

        System.out.println(taskManager.getListOfTasks());
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfSubTask());
        System.out.println();

        //Удаление задач по ID
        taskManager.deleteTaskById(2); // удалили задачу2
        taskManager.deleteEpicById(6); //удалили эпик2
        taskManager.deleteSubTaskById(4); //удалили подзадачу 1 эпика 1

        System.out.println(taskManager.getListOfTasks());
        System.out.println(taskManager.getListOfEpics());
        System.out.println(taskManager.getListOfSubTask());
        System.out.println();

        // Тестирование удаления всех подзадач и обновления статуса эпиков
        taskManager.deleteAllSubTask();
        System.out.println(taskManager.getListOfSubTask());
        System.out.println(taskManager.getListOfEpics());

    }

}
