package com.example.abrrapp.models;

import java.util.List;

public class SearchAI {
    String result;
    List<Dish> restaurant;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public List<Dish> getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(List<Dish> restaurant) {
        this.restaurant = restaurant;
    }
}
