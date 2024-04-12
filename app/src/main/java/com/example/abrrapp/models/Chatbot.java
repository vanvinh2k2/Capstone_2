package com.example.abrrapp.models;

import java.util.Date;

public class Chatbot {
    private String body_bot;
    private String body_user;
    private String date;

    public Chatbot(String body_bot, String body_user) {
        this.body_bot = body_bot;
        this.body_user = body_user;
    }

    public String getBody_bot() {
        return body_bot;
    }

    public void setBody_bot(String body_bot) {
        this.body_bot = body_bot;
    }

    public String getBody_user() {
        return body_user;
    }

    public void setBody_user(String body_user) {
        this.body_user = body_user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
