package com.example.abrrapp.models;

import java.util.List;

public class LikeRestaurantModel {
    String message;
    boolean success;
    List<LikeRestaurant> data;

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

    public List<LikeRestaurant> getData() {
        return data;
    }

    public void setData(List<LikeRestaurant> data) {
        this.data = data;
    }
}
