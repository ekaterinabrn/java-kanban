package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {
    private int nextId = 1;
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    HashMap<Integer, Subtask> subtask = new HashMap<>();

    public int generateNextId() {
        return nextId++;
    }


    public Task createTask(Task task) {
        task.setId(generateNextId());
        tasks.put(task.getId(), task);
        return task;
    }

    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public List<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }

    void deleteAllTask() {
        tasks.clear();
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }

    Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateNextId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    Epic getEpicById(int id) {
        return epics.get(id);
    }


    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public void deleteAllEpics() {
        for (Epic e : epics.values()) {
            if (e != null) {
                List<Integer> subtaskIds = e.getSubtaskIds();
                for (Integer id : subtaskIds) {
                    subtask.remove(id);
                }
                e.deleteAllSubtasks();
            }

        }
        subtask.clear();
        epics.clear();
    }

    public void deleteEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            List<Integer> subtaskId = epic.getSubtaskIds();
            for (Integer SId : subtaskId) {
                subtask.remove(SId);
            }
        }

    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            epics.put(epic.getId(), epic);

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

    public Subtask createSubtask(Subtask subtasks) {
        subtasks.setId(generateNextId());
        subtask.put(subtasks.getId(), subtasks);
        if (epics.containsKey(subtasks.getEpicId())) {
            Epic e = epics.get(subtasks.getEpicId());
            e.addSubtaskId(subtasks.getId());
            e.setStatus(calculateEpicStatus(e));
        }
        return subtasks;
    }

    public void updateSubtask(Subtask subtasks) {
        if (subtask.containsKey(subtasks.getId())) {
            subtask.put(subtasks.getId(), subtasks);
            Epic e = epics.get(subtasks.getEpicId());
            if (e != null) {
                e.setStatus(calculateEpicStatus(e));
            }
        }
    }

    Subtask getSubtaskById(int id) {
        return subtask.get(id);
    }

    public void deleteSubtaskById(int id) {
        Subtask sub = subtask.get(id);
        if (sub != null) {
            subtask.remove(id);
            Epic e = epics.get(sub.getEpicId());
            if (e != null) {
                e.removeSubtaskId(id);
                e.setStatus(calculateEpicStatus(e));
            }
        }
    }

    public void deleteAllSubtask() {
        for (Epic e : epics.values()) {
            if (e != null) {
                e.deleteAllSubtasks();
                e.setStatus(calculateEpicStatus(e));
            }
        }
        subtask.clear();
    }

    List<Subtask> getAllSubtask() {
        return new ArrayList<>(subtask.values());
    }   //комент с просьбой помочь в том же методе эпика

    List<Subtask> getEpicSubtask(int epicId) {
        Epic e = epics.get(epicId);
        if (e != null) {
            List<Subtask> containsInEpic = new ArrayList<>();
            for (Integer id : e.getSubtaskIds()) {
                Subtask sub = subtask.get(id);
                if (sub != null) {
                    containsInEpic.add(sub);
                }
            }
            return containsInEpic;
        } else {
            return new ArrayList<>();
        }

    }


}


