package com.example.abrrapp.models;

public class SearchAIModel {
    private boolean success;
    private String message;
    private SearchAI data;

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

    public SearchAI getData() {
        return data;
    }

    public void setData(SearchAI data) {
        this.data = data;
    }
}
