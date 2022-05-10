package com.example.taskmaster.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class task {
    @PrimaryKey(autoGenerate = true)
     private int id;
    private String title;
    private String body;
    public taskState state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public task(String title, String body) {
        this.title = title;
        this.body = body;
        this.state=taskState.NEW;
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
       if(state==taskState.NEW){
           return "NEW";
       }
       else if(state==taskState.ASSIGNED){
           return "ASSIGNED";
       }
       else if(state==taskState.IN_PROGRESS){
           return "IN_PROGRESS";
       }
       else
           return "COMPLETE";
       }


    public void setState(taskState state) {

        this.state = state;
    }
}
