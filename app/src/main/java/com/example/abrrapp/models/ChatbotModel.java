package com.example.abrrapp.models;

import java.util.List;

public class ChatbotModel {
    private boolean success;
    private String result;
    private List<Chatbot> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Chatbot> getData() {
        return data;
    }

    public void setData(List<Chatbot> data) {
        this.data = data;
    }
}
