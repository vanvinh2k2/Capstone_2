package com.example.abrrapp.models;

public class Rating {
    private int avg_rating;
    private String rid;
    private int num_review;

    public int getAvg_rating() {
        return avg_rating;
    }

    public void setAvg_rating(int avg_rating) {
        this.avg_rating = avg_rating;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public int getNum_review() {
        return num_review;
    }

    public void setNum_review(int num_review) {
        this.num_review = num_review;
    }
}
