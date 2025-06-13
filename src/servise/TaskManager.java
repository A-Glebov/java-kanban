package servise;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private static int taskId;

    HashMap<Integer, Task> listOfTasks = new HashMap<>();
    HashMap<Integer, Epic> listOfEpics = new HashMap<>();
    HashMap<Integer, SubTask> listOfSubTasks = new HashMap<>();

    //Генерация идентификатора задач всех типов
    public int getTaskId() {
        ++taskId;
        return taskId;
    }


    // А. Получение списка задач
    public HashMap<Integer, Task> getListOfTasks() {
        return listOfTasks;
    }

    //Получение списка эпиков
    public HashMap<Integer, Epic> getListOfEpics() {
        return listOfEpics;
    }

    // Получение списка всех подзадач
    public HashMap<Integer, SubTask> getListOfSubTask() {
        return listOfSubTasks;
    }


    // Б. Удаление всех задач
    public void deleteAllTasks() {
        listOfTasks.clear();
    }

    // Удаление всех эпиков
    public void deleteAllEpic() {
        listOfSubTasks.clear(); // очищаем список всех подзадач
        listOfEpics.clear(); // очищаем список всех эпиков
    }

    // Удаление всех подзадач
    public void deleteAllSubTask() {
        listOfSubTasks.clear(); // очищаем список всех подзадач
        listOfEpics.clear(); // очищаем список всех эпиков
    }


    //С. Получение задачи по идентификатору
    public Task getTask(int id) {
        return listOfTasks.get(id);
    }

    //Получение эпика по идентификатору
    public Epic getEpic(int id) {
        return listOfEpics.get(id);
    }

    // Получение подзадачи по идентификатору
    public SubTask getSubTask(int id) {
        return listOfSubTasks.get(id);
    }


    // D. Создание задачи
    public void createTask(Task task) {
        listOfTasks.put(task.getId(), task);
    }

    // Создание эпика
    public void createEpic(Epic epic) {
        listOfEpics.put(epic.getId(), epic);
    }

    // Создание суб задачи
    public void createSubTask(SubTask subTask) {
        listOfSubTasks.put(subTask.getId(), subTask); // Сохранение задачи в список всех подзадач
        Epic epic = listOfEpics.get(subTask.getEpic().getId());// Получения эпика текущей подзадачи
        epic.getSubTaskIdList().add(subTask.getId()); // Добавление ID подзадачи в список ID подзадач эпика
        updateEpic(epic); // обновление эпика
    }


    //E. Обновление задачи
    public void updateTask(Task task) {
        listOfTasks.put(task.getId(), task);
    }

    // Обновление эпика
    public void updateEpic(Epic epic) {
        ArrayList<SubTask> subTaskList = getListOfSubtaskOfEpic(epic);// Список подзадач эпика

        if (subTaskList.isEmpty()) { // Если список пуст, значит статус эпика NEW
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
    public void updateSubTask(SubTask subTask) {
        int subTaskId = subTask.getId();
        listOfSubTasks.put(subTaskId, subTask);
        updateEpic(subTask.getEpic()); // Обновляем эпик
    }


    //F. Удаление по идентификатору
    public void deleteTaskById(int taskId) {
        listOfTasks.remove(taskId);
    }

    // Удаление эпик по Id
    public void deleteEpicById(int epicId) {
        Epic epic = listOfEpics.get(epicId);
        for (Integer subId : epic.getSubTaskIdList()) { // Удаляем подзадачи эпика из списка всех подзадач
            listOfSubTasks.remove(subId);
        }

        listOfEpics.remove(epicId);
    }

    // Удаление подзадачи по Id
    public void deleteSubTaskById(Integer subTaskId) {
        SubTask subTask = listOfSubTasks.get(subTaskId);
        Epic epic = subTask.getEpic(); // Получение ID эпика текущей подзадачи
        epic.getSubTaskIdList().remove(subTaskId); // Удаление ID подзадачи из списка ID подзадач текущего эпика
        updateEpic(epic); // Проверка и изменение статуса эпика

        listOfSubTasks.remove(subTaskId); // Удаление из списка всех подзадач
    }


    //получение списка задач определенного эпика
    public ArrayList<SubTask> getListOfSubtaskOfEpic(Epic epic) {
        ArrayList<SubTask> listOfSunTasksOfEpic = new ArrayList<>();

        for (Integer subTasId : epic.getSubTaskIdList()) {
            listOfSunTasksOfEpic.add(listOfSubTasks.get(subTasId));
        }

        return listOfSunTasksOfEpic;
    }


}
