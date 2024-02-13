package com.example.abrrapp.models;

import java.io.Serializable;

public class Table implements Serializable {
    String tid, title;
    int number_seat;

    public Table(String tid, String title) {
        this.tid = tid;
        this.title = title;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNumber_seat() {
        return number_seat;
    }

    public void setNumber_seat(int number_seat) {
        this.number_seat = number_seat;
    }
}
