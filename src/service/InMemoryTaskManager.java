package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private static int taskId = 0;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subtasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    //Генерация идентификатора задач всех типов
    @Override
    public int getTaskId() {
        ++taskId;
        return taskId;
    }

    // Геттеры для hashmap в которых хранятся задачи всех типов
    @Override
    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    @Override
    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    @Override
    public HashMap<Integer, SubTask> getSubtasks() {
        return subtasks;
    }

    // А. Получение списка задач
    @Override
    public ArrayList<Task> getListOfTasks() {
        return new ArrayList<>(tasks.values());
    }

    //Получение списка эпиков
    @Override
    public ArrayList<Epic> getListOfEpics() {
        return new ArrayList<>(epics.values());
    }

    // Получение списка всех подзадач
    @Override
    public ArrayList<SubTask> getListOfSubTask() {
        return new ArrayList<>(subtasks.values());
    }


    // Б. Удаление всех задач
    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
        }
        tasks.clear();
    }

    // Удаление всех эпиков
    @Override
    public void deleteAllEpic() {
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        epics.clear(); // очищаем список всех эпиков
    }

    // Удаление всех подзадач
    @Override
    public void deleteAllSubTask() {
        for (Integer id : subtasks.keySet()) {
            historyManager.remove(id);
        }

        subtasks.clear(); // очищаем список всех подзадач

        for (Integer id : epics.keySet()) {
            Epic epic = epics.get(id);
            updateEpicStatus(epic); //Обновляем статусы всех эпиков
        }

    }


    //С. Получение задачи по идентификатору
    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id)); // Добавляем в историю
        return tasks.get(id);
    }

    //Получение эпика по идентификатору
    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id)); // Добавляем в историю
        return epics.get(id);
    }

    // Получение подзадачи по идентификатору
    @Override
    public SubTask getSubTask(int id) {
        historyManager.add(subtasks.get(id)); //Добавляем в историю
        return subtasks.get(id);
    }


    // D. Создание задачи
    @Override
    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }

    // Создание эпика
    @Override
    public void createEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    // Создание суб задачи
    @Override
    public void createSubTask(SubTask subTask) {
        subtasks.put(subTask.getId(), subTask); // Сохранение задачи в список всех подзадач
        Epic epic = epics.get(subTask.getEpicId());// Получения эпика текущей подзадачи
        epic.getSubTaskIdList().add(subTask.getId()); // Добавление ID подзадачи в список ID подзадач эпика
        updateEpicStatus(epic); // обновление эпика
    }


    //E. Обновление задачи
    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    //Обновление эпика
    @Override
    public void updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    // Обновление статуса эпика
    public void updateEpicStatus(Epic epic) {
        ArrayList<SubTask> subTaskList = getListOfSubtaskOfEpic(epic);// Список подзадач эпика

        // Если список идентификаторов подзадач пуст,
        // или если список всех подзадач пуст значит статус эпика NEW
        if (subTaskList.isEmpty() || subtasks.isEmpty()) {
            epic.setStatus(Status.NEW);
            return;
        }

        boolean isNew = false;
        boolean isInProgress = false;
        boolean isDone = false;

        for (SubTask subTask : subTaskList) {
            Status status = subTask.getStatus();

            if (status == Status.IN_PROGRESS) {
                isInProgress = true;
            } else if (status == Status.NEW) {
                isNew = true;
            } else {
                isDone = true;
            }

        }

        if (isNew && !isInProgress && !isDone) {
            epic.setStatus(Status.NEW);
        } else if (!isNew && !isInProgress) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }

    }

    // Обновление подзадачи
    @Override
    public void updateSubTask(SubTask subTask) {
        int subTaskId = subTask.getId();
        subtasks.put(subTaskId, subTask);
        updateEpicStatus(epics.get(subTask.getEpicId())); // Обновляем статус эпика
    }

    //F. Удаление по идентификатору
    @Override
    public void deleteTaskById(int taskId) {
        historyManager.remove(taskId);  // Удаление из истории
        tasks.remove(taskId);
    }

    // Удаление эпик по Id
    @Override
    public void deleteEpicById(int epicId) {
        Epic epic = epics.get(epicId);

        for (Integer subId : epic.getSubTaskIdList()) { // Удаляем подзадачи эпика из списка всех подзадач
            historyManager.remove(subId); // Удаление из истории
            subtasks.remove(subId);
        }

        historyManager.remove(epicId);
        epics.remove(epicId);

    }

    // Удаление подзадачи по Id
    @Override
    public void deleteSubTaskById(int subTaskId) {
        SubTask subTask = subtasks.get(subTaskId);
        Epic epic = epics.get(subTask.getEpicId()); // Получение ID эпика текущей подзадачи
        epic.getSubTaskIdList().remove((Integer) subTaskId); // Удаление ID подзадачи из списка ID подзадач текущего эпика
        updateEpicStatus(epic); // Проверка и изменение статуса эпика

        historyManager.remove(subTaskId); // Удаление из истории
        subtasks.remove(subTaskId); // Удаление из списка подзадач
    }


    //получение списка подзадач определенного эпика
    @Override
    public ArrayList<SubTask> getListOfSubtaskOfEpic(Epic epic) {
        ArrayList<SubTask> listOfSunTasksOfEpic = new ArrayList<>();

        for (Integer subTasId : epic.getSubTaskIdList()) {
            listOfSunTasksOfEpic.add(subtasks.get(subTasId));
        }

        return listOfSunTasksOfEpic;
    }

    // Получение истории просмотров
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


}
