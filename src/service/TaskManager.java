package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    //Генерация идентификатора задач всех типов
    int getTaskId();

    // Геттеры для hashmap в которых хранятся задачи всех типов
    HashMap<Integer, Task> getTasks();

    HashMap<Integer, Epic> getEpics();

    HashMap<Integer, SubTask> getSubtasks();

    // А. Получение списка задач
    ArrayList<Task> getListOfTasks();

    //Получение списка эпиков
    ArrayList<Epic> getListOfEpics();

    // Получение списка всех подзадач
    ArrayList<SubTask> getListOfSubTask();

    // Б. Удаление всех задач
    void deleteAllTasks();

    // Удаление всех эпиков
    void deleteAllEpic();

    // Удаление всех подзадач
    void deleteAllSubTask();

    //С. Получение задачи по идентификатору
    Task getTask(int id);

    //Получение эпика по идентификатору
    Epic getEpic(int id);

    // Получение подзадачи по идентификатору
    SubTask getSubTask(int id);

    // D. Создание задачи
    void createTask(Task task);

    // Создание эпика
    void createEpic(Epic epic);

    // Создание суб задачи
    void createSubTask(SubTask subTask);

    //E. Обновление задачи
    void updateTask(Task task);

    //Обновление эпика
    void updateEpic(Epic epic);

    // Обновление подзадачи
    void updateSubTask(SubTask subTask);

    //F. Удаление по идентификатору
    void deleteTaskById(int taskId);

    // Удаление эпик по Id
    void deleteEpicById(int epicId);

    // Удаление подзадачи по Id
    void deleteSubTaskById(int subTaskId);

    //получение списка подзадач определенного эпика
    ArrayList<SubTask> getListOfSubtaskOfEpic(Epic epic);

    // Получение истории просмотров
    List<Task> getHistory();
}
