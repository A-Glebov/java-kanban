package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    private static int taskId = 0;

    private final HashMap<Integer, Task> listOfTasks = new HashMap<>();
    private final HashMap<Integer, Epic> listOfEpics = new HashMap<>();
    private final HashMap<Integer, SubTask> listOfSubTasks = new HashMap<>();

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    //Генерация идентификатора задач всех типов
    @Override
    public int getTaskId() {
        ++taskId;
        return taskId;
    }


    // А. Получение списка задач
    @Override
    public HashMap<Integer, Task> getListOfTasks() {
        return listOfTasks;
    }

    //Получение списка эпиков
    @Override
    public HashMap<Integer, Epic> getListOfEpics() {
        return listOfEpics;
    }

    // Получение списка всех подзадач
    @Override
    public HashMap<Integer, SubTask> getListOfSubTask() {
        return listOfSubTasks;
    }


    // Б. Удаление всех задач
    @Override
    public void deleteAllTasks() {
        listOfTasks.clear();
    }

    // Удаление всех эпиков
    @Override
    public void deleteAllEpic() {
        listOfEpics.clear(); // очищаем список всех эпиков
    }

    // Удаление всех подзадач
    @Override
    public void deleteAllSubTask() {
        listOfSubTasks.clear(); // очищаем список всех подзадач

        for (Integer id : listOfEpics.keySet()) {
            Epic epic = listOfEpics.get(id);
            updateEpicStatus(epic); //Обновляем статусы всех эпиков
        }
    }


    //С. Получение задачи по идентификатору
    @Override
    public Task getTask(int id) {
        historyManager.add(listOfTasks.get(id));
        return listOfTasks.get(id);

    }

    //Получение эпика по идентификатору
    @Override
    public Epic getEpic(int id) {
        historyManager.add(listOfEpics.get(id));
        return listOfEpics.get(id);
    }

    // Получение подзадачи по идентификатору
    @Override
    public SubTask getSubTask(int id) {
        historyManager.add(listOfSubTasks.get(id));
        return listOfSubTasks.get(id);
    }


    // D. Создание задачи
    @Override
    public void createTask(Task task) {
        listOfTasks.put(task.getId(), task);
    }

    // Создание эпика
    @Override
    public void createEpic(Epic epic) {
        listOfEpics.put(epic.getId(), epic);
    }

    // Создание суб задачи
    @Override
    public void createSubTask(SubTask subTask) {
        listOfSubTasks.put(subTask.getId(), subTask); // Сохранение задачи в список всех подзадач
        Epic epic = listOfEpics.get(subTask.getEpic().getId());// Получения эпика текущей подзадачи
        epic.getSubTaskIdList().add(subTask.getId()); // Добавление ID подзадачи в список ID подзадач эпика
        updateEpicStatus(epic); // обновление эпика
    }


    //E. Обновление задачи
    @Override
    public void updateTask(Task task) {
        listOfTasks.put(task.getId(), task);
    }

    //Обновление эпика
    @Override
    public void updateEpic(Epic epic) {
        listOfEpics.put(epic.getId(), epic);
    }

    // Обновление статуса эпика
    public void updateEpicStatus(Epic epic) {
        ArrayList<SubTask> subTaskList = getListOfSubtaskOfEpic(epic);// Список подзадач эпика

        // Если список идентификаторов подзадач пуст,
        // или если список всех подзадач пуст значит статус эпика NEW
        if (subTaskList.isEmpty() || listOfSubTasks.isEmpty()) {
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
        listOfSubTasks.put(subTaskId, subTask);
        updateEpicStatus(subTask.getEpic()); // Обновляем статус эпика
    }


    //F. Удаление по идентификатору
    @Override
    public void deleteTaskById(int taskId) {
        listOfTasks.remove(taskId);
    }

    // Удаление эпик по Id
    @Override
    public void deleteEpicById(int epicId) {
        Epic epic = listOfEpics.get(epicId);
        for (Integer subId : epic.getSubTaskIdList()) { // Удаляем подзадачи эпика из списка всех подзадач
            listOfSubTasks.remove(subId);
        }

        listOfEpics.remove(epicId);
    }

    // Удаление подзадачи по Id
    @Override
    public void deleteSubTaskById(Integer subTaskId) {
        SubTask subTask = listOfSubTasks.get(subTaskId);
        Epic epic = subTask.getEpic(); // Получение ID эпика текущей подзадачи
        epic.getSubTaskIdList().remove(subTaskId); // Удаление ID подзадачи из списка ID подзадач текущего эпика
        updateEpicStatus(epic); // Проверка и изменение статуса эпика

        listOfSubTasks.remove(subTaskId); // Удаление из списка подзадач
    }


    //получение списка подзадач определенного эпика
    @Override
    public ArrayList<SubTask> getListOfSubtaskOfEpic(Epic epic) {
        ArrayList<SubTask> listOfSunTasksOfEpic = new ArrayList<>();

        for (Integer subTasId : epic.getSubTaskIdList()) {
            listOfSunTasksOfEpic.add(listOfSubTasks.get(subTasId));
        }

        return listOfSunTasksOfEpic;
    }

    // Получение истории просмотров
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }


}
