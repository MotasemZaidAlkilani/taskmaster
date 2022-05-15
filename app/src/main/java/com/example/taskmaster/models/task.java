package com.example.taskmaster.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class task {
    @PrimaryKey(autoGenerate = true)
     private int id;
    private String title;
    private String body;
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public task(String title, String body) {
        this.title = title;
        this.body = body;
        this.state="NEW";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getState() {
        return state;
    }


    public void setState(String state) {

        this.state = state;
    }
}
