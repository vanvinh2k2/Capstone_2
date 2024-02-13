package com.example.abrrapp.models;

import java.util.List;

public class DishFeatureModel {
    String message;
    boolean success;
    List<Dish> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Dish> getData() {
        return data;
    }

    public void setData(List<Dish> data) {
        this.data = data;
    }
}
