package com.example.cianm.bookstore.entity;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by cianm on 11/04/2018.
 */

@IgnoreExtraProperties
public class Order {

    String title, image;
    int quantity;

    public Order(){}

    public Order(String title, String image, int quantity) {
        this.title = title;
        this.image = image;
        this.quantity = quantity;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
