package com.example.abrrapp.models;

import java.util.List;

public class OrderCart {
    Order order;
    List<OrderCartItem> orderDetail;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderCartItem> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderCartItem> orderDetail) {
        this.orderDetail = orderDetail;
    }
}
