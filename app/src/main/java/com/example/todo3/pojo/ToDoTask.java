package com.example.todo3.pojo;
import java.io.Serializable;


public class ToDoTask implements Serializable {


    private int id;     //will be primary key and will be pushed as hashcode of object itself
    private String title;
    private String description;
    private ToDoTag tag;
    private int priority;
    private boolean status;
    private String dateAndTime;      //format will be "dd/mm/yyyy   HH:MM"

    public ToDoTask(){
    }

    public ToDoTask(String title, String description, ToDoTag tag, int priority, boolean status,String date) {
        this.title = title;
        this.description = description;
        this.tag = tag;
        this.priority = priority;
        this.status = status;
        this.dateAndTime = date;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTag(ToDoTag tag) {
        this.tag = tag;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getDescription() {
        return description;
    }

    public ToDoTag getTag() {
        return tag;
    }

    public int getPriority() {
        return priority;
    }

    public boolean getStatus() {
        return status;
    }
    public String getDateAndTime()
    {
        return dateAndTime;
    }

    @Override
    public String toString() {
        return "ToDoTask{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", tagid=" + tag.getId() + ",tagName="+tag.getTagName()+",tagColor="+tag.getColor()+
                ", priority=" + priority +
                ", status=" + status +
                ", dateAndTime='" + dateAndTime + '\'' +
                '}';
    }
}
