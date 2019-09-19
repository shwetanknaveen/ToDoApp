package com.example.todo3.pojo;


import androidx.annotation.Nullable;

public class ToDoTag {


    private int id;         //will be primary key and will be done with hashCode() of object

    private String tagName;
    private String color;  //to use this to set color we will use (viewObject).setTextColor(Color.parseColor())

    public ToDoTag(String tagName, String color) {
        this.tagName = tagName;
        this.color = color;
    }

    public ToDoTag(){

    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (this.getClass() != obj.getClass()) return false;
        ToDoTag that = (ToDoTag) obj;
        if (this.id != that.id) return false;
        if (!this.tagName.equals(that.tagName)) return false;
        if (!this.color.equals(that.color)) return false;
        return true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Override
    public String toString() {
        return "ToDoTag -> " +
                "id = " + id +
                ",  tagName = '" + tagName + '\'' +
                ",  color = '" + color + '\'' ;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
