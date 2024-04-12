package com.example.abrrapp.models;

import java.util.List;

public class DishSuggestModel {
    private boolean success;
    private String message;
    private List<DishSuggest> data;

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

    public List<DishSuggest> getData() {
        return data;
    }

    public void setData(List<DishSuggest> data) {
        this.data = data;
    }
}
