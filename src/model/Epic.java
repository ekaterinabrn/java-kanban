package model;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    // ДОБАВЛЕНО: Поле endTime для хранения времени завершения эпика (спринт 8)
    // duration и startTime наследуются от Task, но являются расчетными
    private LocalDateTime endTime;
    private final List<Integer> subtaskIds = new ArrayList<>();

    public List<Integer> getSubtaskIds() {
        return new ArrayList<>(subtaskIds);
    }


    public Epic(String name, String description) {
        super(Status.NEW, name, description); //статус всегда нью
    }

    // ИЗМЕНЕНО: Обновлен конструктор копирования для поддержки новых полей (спринт 8)
    public Epic(Epic epic) {
        super(epic); // копирует все поля включая duration и startTime
        this.endTime = epic.endTime; // копируем endTime
        for (Integer subtaskId : epic.getSubtaskIds()) {
            this.subtaskIds.add(subtaskId);
        }
    }


    public void addSubtaskId(int subtaskId) {
        //  эпик не добавляет сам себя в подзадачи а то тест падал
        if (subtaskId != getId()) {
            subtaskIds.add(subtaskId);
        }
    }

    public void removeSubtaskId(int subtaskId) {
        subtaskIds.remove(Integer.valueOf(subtaskId));
    }

    public void deleteAllSubtasks() {
        subtaskIds.clear();
    }

    // сеттер для endTime
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    // ДОБАВЛЕНО: Переопределение getEndTime для Epic (спринт 8)
    // В Epic endTime хранится отдельно, так как рассчитывается на основе подзадач
    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "id=" + getId() +
                "subtaskIds=" + subtaskIds +
                ", status=" + getStatus() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                '}';
    }
}
