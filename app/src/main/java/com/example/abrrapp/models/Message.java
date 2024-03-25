package com.example.abrrapp.models;

import java.util.Date;

public class Message {
    private String body, msg_user, msg_restaurant, sender;
    private Date date;

    public Message(String body, String msg_user, String msg_restaurant, String sender, Date date) {
        this.body = body;
        this.msg_user = msg_user;
        this.msg_restaurant = msg_restaurant;
        this.sender = sender;
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getMsg_user() {
        return msg_user;
    }

    public void setMsg_user(String msg_user) {
        this.msg_user = msg_user;
    }

    public String getMsg_restaurant() {
        return msg_restaurant;
    }

    public void setMsg_restaurant(String msg_restaurant) {
        this.msg_restaurant = msg_restaurant;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
