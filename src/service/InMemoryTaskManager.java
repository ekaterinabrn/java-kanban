package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {
    private int nextId = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtask = new HashMap<>();
    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
    }

    public int generateNextId() {
        return nextId++;
    }

    //  проверка пересечения задач по времени
    @Override
    public Task createTask(Task newTask) {
        int id = generateNextId();
        newTask.setId(id);
        newTask.setStatus(Status.NEW);
        // проверка на пересечение с другими задачами
        if (isTaskOverlapping(newTask, id)) {
            return null; // задача не создается, если пересекается с существующей
        }
        Task savedTask = new Task(newTask);
        tasks.put(savedTask.getId(), savedTask);
        return new Task(savedTask);
    }


    @Override
    public void updateTask(Task updateTask) {
        if (tasks.containsKey(updateTask.getId())) {
            //проверка на пересечение с другими задачами
            if (isTaskOverlapping(updateTask, updateTask.getId())) {
                return; // обновление не выполняется, если есть пересечение
            }
            // конструктор копирования, который копирует все поля включая duration и startTime
            Task savedTask = new Task(updateTask);
            savedTask.setId(updateTask.getId());
            tasks.put(updateTask.getId(), savedTask);
        }
    }

    @Override
    public List<Task> getAllTask() {
        return tasks.values().stream()
                .map(Task::new)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteAllTask() {
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId); // удаление из истории
        }
        tasks.clear();
    }


    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id); //  удаление из истории
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        historyManager.add(task);
        return new Task(task);
    }

    @Override
    public Epic createEpic(Epic newEpic) {
        newEpic.setId(generateNextId());
        newEpic.setStatus(Status.NEW);
        Epic savedEpic = new Epic(newEpic);
        epics.put(savedEpic.getId(), savedEpic);
        //пересчет полей времени эпика на основе подзадач
        updateEpicTimeFields(savedEpic);
        return new Epic(savedEpic);
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
            return new Epic(epic);
        }
        return null;
    }

    @Override
    public List<Epic> getAllEpics() {
        return epics.values().stream()
                .map(Epic::new)
                .collect(Collectors.toList());
    }


    @Override
    public void deleteAllEpics() {
        for (Epic e : epics.values()) {
            if (e != null) {
                List<Integer> subtaskIds = e.getSubtaskIds();
                for (Integer id : subtaskIds) {
                    subtask.remove(id);
                    historyManager.remove(id); //  удаление подзадач из истории
                }
                e.deleteAllSubtasks();
            }
        }
        for (Integer epicId : epics.keySet()) {
            historyManager.remove(epicId); // удаление эпиков из истории
        }
        subtask.clear();
        epics.clear();
    }


    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            List<Integer> subtaskId = epic.getSubtaskIds();
            for (Integer SubId : subtaskId) {
                subtask.remove(SubId);
                historyManager.remove(SubId);
            }
            historyManager.remove(id);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic savedEpic = new Epic(epic);
            epics.put(epic.getId(), savedEpic);
            // пересчет полей времени эпика
            updateEpicTimeFields(savedEpic);
        }
    }

    private Status calculateEpicStatus(Epic epic) {
        List<Integer> ids = epic.getSubtaskIds();
        if (ids.isEmpty()) return Status.NEW;
        for (Integer id : ids) {
            Subtask s = subtask.get(id);
            if (s != null && s.getStatus() != Status.DONE) {
                return Status.IN_PROGRESS;
            }
        }
        return Status.DONE;
    }

    @Override
    public Subtask createSubtask(Subtask subtasks) {
        int id = generateNextId();
        subtasks.setId(id);
        //проверка на пересечение с другими задачами
        if (isTaskOverlapping(subtasks, id)) {
            return null; // подзадача не создается, если пересекается с существующей
        }
        Subtask savedSubtask = new Subtask(subtasks);
        subtask.put(id, savedSubtask);
        if (epics.containsKey(subtasks.getEpicId())) {
            Epic e = epics.get(subtasks.getEpicId());
            e.addSubtaskId(id);
            e.setStatus(calculateEpicStatus(e));
            // пересчет полей времени эпика при добавлении подзадачи
            updateEpicTimeFields(e);
        }
        return new Subtask(savedSubtask);
    }

    @Override
    public void updateSubtask(Subtask subtasks) {
        if (subtask.containsKey(subtasks.getId())) {
            // проверка на пересечение с другими задачами
            if (isTaskOverlapping(subtasks, subtasks.getId())) {
                return; // обновление не выполняется, если есть пересечение
            }
            Subtask savedSubtask = new Subtask(subtasks);
            subtask.put(subtasks.getId(), savedSubtask);
            Epic e = epics.get(subtasks.getEpicId());
            if (e != null) {
                e.setStatus(calculateEpicStatus(e));
                // пересчет полей времени эпика при обновлении подзадачи
                updateEpicTimeFields(e);
            }
        }
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtaskItem = subtask.get(id);
        if (subtaskItem != null) {
            historyManager.add(subtaskItem);
            return new Subtask(subtaskItem);
        }
        return null;
    }

    @Override
    public void deleteSubtaskById(int id) {
        Subtask sub = subtask.get(id);
        if (sub != null) {
            int epicId = sub.getEpicId();
            subtask.remove(id);
            historyManager.remove(id);
            Epic e = epics.get(epicId);
            if (e != null) {
                e.removeSubtaskId(id);
                e.setStatus(calculateEpicStatus(e));
                updateEpicTimeFields(e);
            }
        }
    }

    // удаление подзадач из истории
    @Override
    public void deleteAllSubtask() {
        for (Integer subtaskId : subtask.keySet()) {
            historyManager.remove(subtaskId); // удаление из истории
        }
        for (Epic e : epics.values()) {
            if (e != null) {
                e.deleteAllSubtasks();
                e.setStatus(calculateEpicStatus(e));
            }
        }
        subtask.clear();
    }

    @Override
    public List<Subtask> getAllSubtask() {
        return subtask.values().stream()
                .map(Subtask::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<Subtask> getEpicSubtask(int epicId) {
        Epic e = epics.get(epicId);
        if (e != null) {
            return e.getSubtaskIds().stream()
                    .map(subtask::get)
                    .filter(sub -> sub != null)
                    .map(Subtask::new)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicTimeFields(Epic epic) {
        List<Integer> subtaskIds = epic.getSubtaskIds();
        if (subtaskIds.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
            return;
        }

        // Используем Stream API для фильтрации подзадач с заданным временем
        List<Subtask> subtasks = subtaskIds.stream()
                .map(subtask::get)
                .filter(sub -> sub != null && sub.getStartTime() != null && sub.getDuration() != null)
                .collect(Collectors.toList());

        if (subtasks.isEmpty()) {
            epic.setStartTime(null);
            epic.setDuration(null);
            epic.setEndTime(null);
            return;
        }

        // Находим минимальное время начала
        LocalDateTime minStartTime = subtasks.stream()
                .map(Subtask::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(null);

        // Находим максимальное время завершения
        LocalDateTime maxEndTime = subtasks.stream()
                .map(Subtask::getEndTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);

        // Суммируем продолжительности всех подзадач
        Duration totalDuration = subtasks.stream()
                .map(Subtask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);

        epic.setStartTime(minStartTime);
        epic.setDuration(totalDuration);
        epic.setEndTime(maxEndTime);
    }


    //  математический метод наложения отрезков
    private boolean isTasksOverlapping(Task task1, Task task2) {
        if (task1.getStartTime() == null || task1.getDuration() == null ||
            task2.getStartTime() == null || task2.getDuration() == null) {
            return false;
        }

        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        // Задачи пересекаются, если start1 < end2 И start2 < end1
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    private boolean isTaskOverlapping(Task newTask, int excludeId) {
        if (newTask.getStartTime() == null || newTask.getDuration() == null) {
            return false;
        }

        // Собираем все задачи и подзадачи (кроме проверяемой) с заданным временем
        List<Task> allTasks = Stream.concat(
                tasks.values().stream(),
                subtask.values().stream()
        ).filter(task -> task.getId() != excludeId)
         .filter(task -> task.getStartTime() != null && task.getDuration() != null)
         .collect(Collectors.toList());

        // Проверяем пересечение с каждой задачей
        return allTasks.stream().anyMatch(task -> isTasksOverlapping(newTask, task));
    }

    // Метод для получения задач в порядке приоритета (спринт 8)
    // Задачи без startTime не включаются в список
    @Override
    public List<Task> getPrioritizedTasks() {
        Set<Task> prioritizedSet = new TreeSet<>(Comparator
                .comparing(Task::getStartTime, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Task::getId));

        // Добавляем задачи с заданным временем начала
        prioritizedSet.addAll(tasks.values().stream()
                .filter(task -> task.getStartTime() != null)
                .collect(Collectors.toList()));

        prioritizedSet.addAll(subtask.values().stream()
                .filter(task -> task.getStartTime() != null)
                .collect(Collectors.toList()));

        return new ArrayList<>(prioritizedSet);
    }
}
