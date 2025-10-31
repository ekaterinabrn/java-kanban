package service;

import model.*;
import service.exception.ManagerSaveException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

//  файловое сохранение
public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
        try {
            if (!file.exists()) {
                Files.createFile(file.toPath());
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при создании файла", e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        int maxId = 0;
        //открываем файл для построчного чтения
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            //если первая строка пуста вернем пустой менеджер
            if (line == null) {
                return manager;
            }
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                String[] fields = line.split(",");
                if (fields.length < 5) continue;
//разбираем поля в переменные
                int id = Integer.parseInt(fields[0]);
                TaskType type = TaskType.valueOf(fields[1]);
                String name = fields[2];
                Status status = Status.valueOf(fields[3]);
                String description = fields[4];
//обновляем айди чтоб правильно поставить следующий
                if (id > maxId) maxId = id;

                switch (type) {
                    case TASK: {
                        Task t = new Task(status, name, description);
                        t.setId(id);
                        manager.tasks.put(id, t);
                        break;
                    }
                    case EPIC: {
                        Epic e = new Epic(name, description);
                        e.setId(id);
                        e.setStatus(status);
                        manager.epics.put(id, e);
                        break;
                    }
                    case SUBTASK: {
                        int epicId = fields.length > 5 ? Integer.parseInt(fields[5]) : 0;
                        Subtask s = new Subtask(status, name, description, epicId);
                        s.setId(id);
                        manager.subtask.put(id, s);
                        Epic epic = manager.epics.get(epicId);
                        if (epic != null) epic.addSubtaskId(id);
                        break;
                    }
                }
            }

            // пересчитываем статусы эпиков без сохранения
            for (Epic epic : manager.epics.values()) {
                List<Integer> ids = epic.getSubtaskIds();
                if (ids.isEmpty()) {
                    epic.setStatus(Status.NEW);
                } else {
                    boolean allDone = true;
                    for (Integer sid : ids) {
                        Subtask s = manager.subtask.get(sid);
                        if (s == null || s.getStatus() != Status.DONE) {
                            allDone = false;
                            break;
                        }
                    }
                    epic.setStatus(allDone ? Status.DONE : Status.IN_PROGRESS);
                }
            }

            manager.nextId = maxId + 1;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка при загрузке файла", e);
        }
        return manager;
    }

    // сохранение состояния в файл
    private void save() {
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,epic\n");
            for (Task task : tasks.values()) {
                writer.write(csvLine(task));
                writer.write("\n");
            }
            for (Epic epic : epics.values()) {
                writer.write(csvLine(epic));
                writer.write("\n");
            }
            for (Subtask s : subtask.values()) {
                writer.write(csvLine(s));
                writer.write("\n");
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла", e);
        }
    }

    // преобразование задачи в строку csv
    private static String csvLine(Task task) {
        StringBuilder sb = new StringBuilder();
        sb.append(task.getId()).append(",");
        sb.append(task.getType()).append(",");
        sb.append(task.getName()).append(",");
        sb.append(task.getStatus()).append(",");
        sb.append(task.getDescription());
        if (task.getType() == TaskType.SUBTASK) {
            sb.append(",").append(((Subtask) task).getEpicId());
        }
        return sb.toString();
    }

    // переопределение методов с сохранением
    @Override
    public Task createTask(Task newTask) {
        Task result = super.createTask(newTask);
        save();
        return result;
    }

    @Override
    public void updateTask(Task updateTask) {
        super.updateTask(updateTask);
        save();
    }

    @Override
    public void deleteAllTask() {
        super.deleteAllTask();
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public Epic createEpic(Epic newEpic) {
        Epic result = super.createEpic(newEpic);
        save();
        return result;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteEpicById(int id) {
        super.deleteEpicById(id);
        save();
    }

    @Override
    public Subtask createSubtask(Subtask subtasks) {
        Subtask result = super.createSubtask(subtasks);
        save();
        return result;
    }

    @Override
    public void updateSubtask(Subtask subtasks) {
        super.updateSubtask(subtasks);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteAllSubtask() {
        super.deleteAllSubtask();
        save();
    }
}

