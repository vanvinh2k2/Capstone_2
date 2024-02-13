package com.example.abrrapp.models;

public class OrderCartModel {
    String message;
    boolean success;
    OrderCart data;

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

    public OrderCart getData() {
        return data;
    }

    public void setData(OrderCart data) {
        this.data = data;
    }
}
