package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private int epicId;

    public SubTask(int id, Type type, String name, String description, Status status, LocalDateTime startTime, Duration duration, int epicId) {
        super(id, type, name, description, status, startTime, duration);
        this.epicId = epicId;
        getEndTime();
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", subTaskStatus=" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                ", duration='" + duration + '\'' +
                ", epicID=" + epicId +
                '}';
    }
}
