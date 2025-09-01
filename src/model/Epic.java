package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTaskIdList;
    private LocalDateTime endTime;

    public Epic(int id, Type type, String name, String description, Status status,
                LocalDateTime startTime, Duration duration, ArrayList<Integer> subTaskIdList) {
        super(id, type, name, description, status, startTime, duration);
        this.subTaskIdList = subTaskIdList;
    }

    public ArrayList<Integer> getSubTaskIdList() {
        return subTaskIdList;
    }

    public void setSubTaskIdList(ArrayList<Integer> subTaskIdList) {
        this.subTaskIdList = subTaskIdList;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", epicStatus='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + getEndTime() + '\'' +
                ", duration='" + duration + '\'' +
                ", subTaskIdList=" + subTaskIdList +
                '}';
    }

}
