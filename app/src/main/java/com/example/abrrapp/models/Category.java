package com.example.abrrapp.models;

import java.io.Serializable;

public class Category implements Serializable {
    String cid, title, image;

    public Category(String cid, String title) {
        this.cid = cid;
        this.title = title;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
