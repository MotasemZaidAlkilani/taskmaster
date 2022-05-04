package com.example.taskmaster.models;

public class task {

    private String title;
    private String body;
    taskState state;

    public task(String title, String body) {
        this.title = title;
        this.body = body;
        this.state=getState();
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

    public taskState getState() {
        return state;
    }

    public void setState(taskState state) {
        this.state = state;
    }
}
