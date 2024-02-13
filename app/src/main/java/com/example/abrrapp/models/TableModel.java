package com.example.abrrapp.models;

import java.util.List;

public class TableModel {
    boolean success;
    String message;
    List<Table> data;

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

    public List<Table> getData() {
        return data;
    }

    public void setData(List<Table> data) {
        this.data = data;
    }
}
