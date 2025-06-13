package model;

public class SubTask extends Task{

    private Epic epic;

    public SubTask(int id, String name, String description, Status status, Epic epic) {
        super(id, name, description, status);
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", subTaskStatus=" + status + '\'' +
                ", epicID=" + epic.getId() +
                '}';
    }
}
