package com.fepa.meupet.model.environment.notification;

import java.io.Serializable;

public class Notification implements Serializable {

    private String name;
    private String date;
    private String time;
    private String content;

    public Notification(String name, String date, String time, String content) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
