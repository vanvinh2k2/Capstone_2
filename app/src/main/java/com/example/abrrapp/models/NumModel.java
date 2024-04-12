package com.example.abrrapp.models;

public class NumModel {
    private boolean success;
    private String message;
    private Num data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Num getData() {
        return data;
    }

    public void setData(Num data) {
        this.data = data;
    }
}
