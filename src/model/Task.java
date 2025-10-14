package model;

import java.util.Objects;

public class Task {
    private int id;
    private Status status;
    private String name;
    private String description;

    public Task(Status status, String name, String description) {
        this.status = status;
        this.name = name;
        this.description = description;
    }

    public Task(Task newTask) {
        this.id = newTask.id;
        this.status = newTask.status;
        this.name = newTask.name;
        this.description = newTask.description;
        // конструктор для копии
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "model.Task{" +
                "id=" + id +
                ", status=" + status +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}

