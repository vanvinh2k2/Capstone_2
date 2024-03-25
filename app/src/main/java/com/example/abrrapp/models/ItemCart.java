package com.example.abrrapp.models;

public class ItemCart {
    String did;
    int quantity;

    public ItemCart(String did, int quantity) {
        this.did = did;
        this.quantity = quantity;
    }



    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
