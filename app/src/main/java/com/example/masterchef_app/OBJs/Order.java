package com.example.masterchef_app.OBJs;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    User customer ;
    String customerPhone ;
    String customerEmail ;
    String customerName ;
String totalPrice ;
    List<Card> orderCardList;
    String orderKey ;
    public Order() {
    }
    public Order(User customer, List<Card> orderCardList, String orderKey, String customerPhone, String customerEmail, String customerName, String totalPrice) {
        this.customer = customer;
        this.orderCardList = orderCardList;
        this.orderKey = orderKey;
        this.customerPhone = customerPhone;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
        this.totalPrice = totalPrice;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public List<Card> getOrderCardList() {
        return orderCardList;
    }

    public void setOrderCardList(List<Card> orderCardList) {
        this.orderCardList = orderCardList;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;

    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
