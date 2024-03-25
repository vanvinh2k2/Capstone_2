package com.example.abrrapp.models;

public class ItemOrder {
    private String did, image;
    private int quantity;
    private float price, total;

    public ItemOrder(String did, String image, int quantity, float price, float total) {
        this.did = did;
        this.image = image;
        this.quantity = quantity;
        this.price = price;
        this.total = total;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
