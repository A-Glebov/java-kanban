package model;

import java.util.ArrayList;

public class Epic extends Task {

    private ArrayList<Integer> subTaskIdList;

    public Epic(int id, String name, String description, Status status, ArrayList<Integer> subTaskIdList) {
        super(id, name, description, status);
        this.subTaskIdList = subTaskIdList;
    }

    public ArrayList<Integer> getSubTaskIdList() {
        return subTaskIdList;
    }

    public void setSubTaskIdList(ArrayList<Integer> subTaskIdList) {
        this.subTaskIdList = subTaskIdList;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", epicStatus=" + status + '\'' +
                "subTaskIdList=" + subTaskIdList +
                '}';
    }

}
