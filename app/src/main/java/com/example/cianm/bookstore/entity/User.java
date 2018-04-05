package com.example.cianm.bookstore.entity;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by cianm on 28/03/2018.
 */

@IgnoreExtraProperties
public class User {

    String name, email, password, shippingAddress, paymentDetails, noOfPurchases;

    public User(){}

    public User(String name, String email, String password, String noOfPurchases) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.noOfPurchases = noOfPurchases;
    }

    public User(String name, String email, String password, String shippingAddress, String paymentDetails) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.shippingAddress = shippingAddress;
        this.paymentDetails = paymentDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getNoOfPurchases() {
        return noOfPurchases;
    }

    public void setNoOfPurchases(String noOfPurchases) {
        this.noOfPurchases = noOfPurchases;
    }
}
