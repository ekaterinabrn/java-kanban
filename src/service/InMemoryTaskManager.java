package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public Task createTask(Task newTask) {
        newTask.setId(generateNextId());
        newTask.setStatus(Status.NEW);
        // делаем копию задачи
        Task savedTask = new Task(newTask);
        tasks.put(savedTask.getId(), savedTask);
        return new Task(savedTask);
    }

    @Override
    public void updateTask(Task updateTask) {
        if (tasks.containsKey(updateTask.getId())) {
            Task savedTask = new Task(updateTask.getStatus(), updateTask.getName(), updateTask.getDescription());
            savedTask.setId(updateTask.getId());
            tasks.put(updateTask.getId(), savedTask);
        }
    }

    @Override
    public List<Task> getAllTask() {
        List<Task> taskList = new ArrayList<>();
        for (Task task : tasks.values()) {
            taskList.add(new Task(task));
        }
        return taskList;
    }

    @Override
    public void deleteAllTask() {
        // удаляем все задачи из истории просмотров перед очисткой
        for (Integer taskId : tasks.keySet()) {
            historyManager.remove(taskId);
        }
        tasks.clear();
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        // удаляем задачу из истории просмотров
        historyManager.remove(id);
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
        List<Epic> epicList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicList.add(new Epic(epic));
        }
        return epicList;
    }

    @Override
    public void deleteAllEpics() {
        for (Epic e : epics.values()) {
            if (e != null) {
                List<Integer> subtaskIds = e.getSubtaskIds();
                for (Integer id : subtaskIds) {
                    subtask.remove(id);
                    // удаляем подзадачу из истории просмотров
                    historyManager.remove(id);
                }
                e.deleteAllSubtasks();
            }
        }
        // удаляем  эпики из истории просмотров
        for (Integer epicId : epics.keySet()) {
            historyManager.remove(epicId);
        }
        subtask.clear();
        epics.clear();
    }

    @Override
    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            List<Integer> subtaskId = epic.getSubtaskIds();
            for (Integer SId : subtaskId) {
                subtask.remove(SId);
                // удаляем подзадачу из истории просмотров
                historyManager.remove(SId);
            }
            // удаляем эпик из истории просмотров
            historyManager.remove(id);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic savedEpic = new Epic(epic.getName(), epic.getDescription());
            savedEpic.setId(epic.getId());
            savedEpic.setStatus(epic.getStatus());
            // Копируем список подзадач
            for (Integer subtaskId : epic.getSubtaskIds()) {
                savedEpic.addSubtaskId(subtaskId);
            }
            epics.put(epic.getId(), savedEpic);
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
        Subtask savedSubtask = new Subtask(subtasks.getStatus(), subtasks.getName(), subtasks.getDescription(), subtasks.getEpicId());
        savedSubtask.setId(id);
        subtask.put(id, savedSubtask);
        if (epics.containsKey(subtasks.getEpicId())) {
            Epic e = epics.get(subtasks.getEpicId());
            e.addSubtaskId(id);
            e.setStatus(calculateEpicStatus(e));
        }
        return new Subtask(savedSubtask);
    }

    @Override
    public void updateSubtask(Subtask subtasks) {
        if (subtask.containsKey(subtasks.getId())) {
            Subtask savedSubtask = new Subtask(subtasks.getStatus(), subtasks.getName(), subtasks.getDescription(), subtasks.getEpicId());
            savedSubtask.setId(subtasks.getId());
            subtask.put(subtasks.getId(), savedSubtask);
            Epic e = epics.get(subtasks.getEpicId());
            if (e != null) {
                e.setStatus(calculateEpicStatus(e));
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
            subtask.remove(id);
            // удаляем подзадачу из истории просмотров
            historyManager.remove(id);
            Epic e = epics.get(sub.getEpicId());
            if (e != null) {
                e.removeSubtaskId(id);
                e.setStatus(calculateEpicStatus(e));
            }
        }
    }

    @Override
    public void deleteAllSubtask() {
        // удаляем  подзадачи из истории просмотров перед очисткой
        for (Integer subtaskId : subtask.keySet()) {
            historyManager.remove(subtaskId);
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
        List<Subtask> subtaskList = new ArrayList<>();
        for (Subtask subtaskItem : subtask.values()) {
            subtaskList.add(new Subtask(subtaskItem));
        }
        return subtaskList;
    }

    @Override
    public List<Subtask> getEpicSubtask(int epicId) {
        Epic e = epics.get(epicId);
        if (e != null) {
            List<Subtask> containsInEpic = new ArrayList<>();
            for (Integer id : e.getSubtaskIds()) {
                Subtask sub = subtask.get(id);
                if (sub != null) {
                    containsInEpic.add(new Subtask(sub));
                }
            }
            return containsInEpic;
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}