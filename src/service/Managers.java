package service;

import java.io.File;

public class Managers {
//перенесен  getDefault
    public static TaskManager getDefault() {
        File file = new File("kanban.csv");
        return new FileBackedTaskManager(file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}