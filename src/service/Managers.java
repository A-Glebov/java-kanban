package service;

import java.io.File;
import java.io.IOException;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTaskManager getFileBacked() throws IOException {
        File file = File.createTempFile("file-backed-task-manager", ".csv");
        return new FileBackedTaskManager(file);
    }
}
