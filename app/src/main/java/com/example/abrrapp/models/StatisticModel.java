package com.example.abrrapp.models;

import java.util.List;

public class StatisticModel {
    private boolean success;
    private String message;
    private Statistic data;

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

    public Statistic getData() {
        return data;
    }

    public void setData(Statistic data) {
        this.data = data;
    }
}
