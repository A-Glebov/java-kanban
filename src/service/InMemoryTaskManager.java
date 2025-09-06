package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private int taskId = 0;

    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, SubTask> subtasks = new HashMap<>();

    private final TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator
            .comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
            .thenComparing(Task::getId));

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    private int getTaskId() {
        ++taskId;
        return taskId;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    // Получение списка задач
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

    // Удаление всех задач
    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            prioritizedTasks.remove(getTask(id));
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
        deleteAllSubTask();
        epics.clear(); // очищаем список всех эпиков
    }

    // Удаление всех подзадач
    @Override
    public void deleteAllSubTask() {
        for (Integer id : subtasks.keySet()) {
            prioritizedTasks.remove(getSubTask(id));
            historyManager.remove(id);
        }
        subtasks.clear(); // очищаем список всех подзадач

        for (Integer id : epics.keySet()) {
            Epic epic = epics.get(id);
            updateEpicStatus(epic); //Обновляем статусы всех эпиков
        }

    }

    // Получение задачи по идентификатору
    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));  // Добавляем в историю
        return tasks.get(id);
    }

    // Получение эпика по идентификатору
    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    // Получение подзадачи по идентификатору
    @Override
    public SubTask getSubTask(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    // Создание задачи
    @Override
    public void createTask(Task task) {
        if (!validateTaskByDateTime(task)) {
            final int id = getTaskId();
            task.setId(id);
            tasks.put(id, task);
            prioritizedTasks.add(task);
        }
    }

    // Создание эпика
    @Override
    public void createEpic(Epic epic) {
        final int id = getTaskId();
        epic.setId(id);
        epics.put(id, epic);
    }

    // Создание суб задачи
    @Override
    public void createSubTask(SubTask subTask) {
        if (!validateTaskByDateTime(subTask)) {
            final int id = getTaskId();
            subTask.setId(id);
            subtasks.put(id, subTask); // Сохранение задачи в список всех подзадач
            prioritizedTasks.add(subTask);
            Epic epic = epics.get(subTask.getEpicId());// Получения эпика текущей подзадачи
            epic.getSubTaskIdList().add(subTask.getId()); // Добавление ID подзадачи в список ID подзадач эпика
            updateEpicStatus(epic);// обновление эпика
            setEpicDataTime(epic);
        }
    }

    // Обновление задачи
    @Override
    public void updateTask(Task task) {
        int taskId = task.getId();
        Task safedTask = tasks.get(taskId);
        if (safedTask == null) {
            return;
        }
        if (!validateTaskByDateTime(task)) {
            tasks.remove(task.getId());
            tasks.put(task.getId(), task);
            prioritizedTasks.add(task);
        }
    }

    // Обновление эпика
    @Override
    public void updateEpic(Epic epic) {
        int epicId = epic.getId();
        Epic safedEpic = epics.get(epicId);
        if (safedEpic == null) {
            return;
        }
        epics.put(epic.getId(), epic);
    }

    // Обновление подзадачи
    @Override
    public void updateSubTask(SubTask subTask) {
        int subTaskId = subTask.getId();
        SubTask safedSubTask = subtasks.get(subTaskId);
        if (safedSubTask == null) {
            return;
        }
        if (!validateTaskByDateTime(subTask)) {
            subtasks.remove(subTaskId);
            subtasks.put(subTaskId, subTask);
            prioritizedTasks.add(subTask);
            Epic epic = epics.get(subTask.getEpicId());
            updateEpicStatus(epic);
            setEpicDataTime(epic);
        }
    }

    // Удаление по идентификатору
    @Override
    public void deleteTaskById(int taskId) {
        if (tasks.containsKey(taskId)) {
            historyManager.remove(taskId);
            prioritizedTasks.remove(tasks.get(taskId));
            tasks.remove(taskId);
        }
    }

    // Удаление эпик по Id
    @Override
    public void deleteEpicById(int epicId) {
        if (epics.containsKey(epicId)) {
            Epic epic = epics.get(epicId);

            for (Integer subId : epic.getSubTaskIdList()) {
                historyManager.remove(subId);
                subtasks.remove(subId);
            }

            historyManager.remove(epicId);
            epics.remove(epicId);
        } else {
            System.out.println("Эпик не существует");
        }

    }

    // Удаление подзадачи по Id
    @Override
    public void deleteSubTaskById(int subTaskId) {
        if (subtasks.containsKey(subTaskId)) {
            SubTask subTask = subtasks.get(subTaskId);
            Epic epic = epics.get(subTask.getEpicId());
            epic.getSubTaskIdList().remove((Integer) subTaskId);
            updateEpicStatus(epic);
            setEpicDataTime(epic);

            historyManager.remove(subTaskId);
            prioritizedTasks.remove(subtasks.get(subTaskId));
            subtasks.remove(subTaskId); // Удаление из списка подзадач
        } else {
            System.out.println("Подзадача не существует");
        }

    }

    //получение списка подзадач определенного эпика
    @Override
    public List<SubTask> getListOfSubtaskOfEpic(Epic epic) {
        return epic.getSubTaskIdList().stream().map(subtasks::get).toList();
    }

    // Получение истории просмотров
    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void setEpicDataTime(Epic epic) {
        LocalDateTime startTime;
        LocalDateTime endTime;
        Duration duration;

        if (epic.getSubTaskIdList().isEmpty()) {
            return;
        }

        List<SubTask> subTasks = getListOfSubtaskOfEpic(epic);
        startTime = subTasks.getFirst().getStartTime();
        endTime = subTasks.getFirst().getEndTime();
        duration = Duration.ofMinutes(0);

        for (SubTask subTask : subTasks) {
            if (subTask.getStartTime().isBefore(startTime)) {
                startTime = subTask.getStartTime();
            }

            if (subTask.getEndTime().isBefore(endTime)) {
                endTime = subTask.getEndTime();
            }

            duration = duration.plus(subTask.getDuration());
        }

        epic.setStartTime(startTime);
        epic.setEndTime(endTime);
        epic.setDuration(duration);

    }

    private boolean checkTaskForIntersectionInTime(Task task, Task existTask) {
        return (task.getStartTime().isAfter(existTask.getStartTime()) && task.getStartTime().isBefore(existTask.getEndTime()))
                || (task.getEndTime().isAfter(existTask.getStartTime()) && task.getEndTime().isBefore(existTask.getEndTime()))
                || task.getStartTime().equals(existTask.getStartTime())
                || task.getEndTime().equals(existTask.getEndTime());
    }

    private boolean validateTaskByDateTime(Task task) {
        return prioritizedTasks.stream()
                .anyMatch(existTask -> checkTaskForIntersectionInTime(task, existTask));
    }

    // Обновление статуса эпика
    private void updateEpicStatus(Epic epic) {
        List<SubTask> subTaskList = getListOfSubtaskOfEpic(epic);// Список подзадач эпика

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

}
