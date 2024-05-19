package com.example.abrrapp.models;

import java.util.List;

public class RatingModel {
    private boolean success;
    private String message;
    private List<Rating> data;

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

    public List<Rating> getData() {
        return data;
    }

    public void setData(List<Rating> data) {
        this.data = data;
    }
}
