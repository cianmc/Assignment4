package com.example.cianm.bookstore.entity;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by cianm on 28/03/2018.
 */

@IgnoreExtraProperties
public class User {

    private String id, name, email, password, shippingAddress, cardType, cardNumber;
    private int noOfPurchases;

    public User(){}

    private User(final UserBuilder builder) {
        name = builder.name;
        email = builder.email;
        password = builder.password;
        noOfPurchases = builder.noOfPurchases;
        id = builder.id;
        shippingAddress = builder.shippingAddress;
        cardNumber = builder.cardNumber;
        cardType = builder.cardType;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public String getCardType() {
        return cardType;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getNoOfPurchases() {
        return noOfPurchases;
    }

    public void setNoOfPurchases(int noOfPurchases) {
        this.noOfPurchases = noOfPurchases;
    }

    public static class UserBuilder {
        private String name;
        private String email;
        private String password;
        private int noOfPurchases;
        private String id;
        private String shippingAddress;
        private String cardNumber;
        private String cardType;

        public UserBuilder(String name, String password, String email){
            this.name = name;
            this.password = password;
            this.email = email;
        }

        public UserBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setNoOfPurchases(int noOfPurchases) {
            this.noOfPurchases = noOfPurchases;
            return this;
        }

        public UserBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public UserBuilder setShippingAddress(String shippingAddress) {
            this.shippingAddress = shippingAddress;
            return this;
        }

        public UserBuilder setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
            return this;
        }

        public UserBuilder setCardType(String cardType) {
            this.cardType = cardType;
            return this;
        }

        public User buildUser() {
            return new User(this);
        }
    }
}
