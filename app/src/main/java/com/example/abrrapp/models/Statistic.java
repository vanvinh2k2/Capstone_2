package com.example.abrrapp.models;

import java.util.List;

public class Statistic {
    private List<Float> num_order;
    private float total;

    public List<Float> getNum_order() {
        return num_order;
    }

    public void setNum_order(List<Float> num_order) {
        this.num_order = num_order;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
