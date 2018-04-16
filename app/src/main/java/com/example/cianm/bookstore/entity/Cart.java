package com.example.cianm.bookstore.entity;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by cianm on 06/04/2018.
 */

@IgnoreExtraProperties
public class Cart {

    private String userID, userName, bookID, title, author, category, cartID, image;
    private int quantity, stock;
    private double price, total;

    public Cart() {
    }

    private Cart(final CartBuilder builder){
        userID = builder.userID;
        userName = builder.userName;
        bookID = builder.bookID;
        title = builder.title;
        author = builder.author;
        category = builder.category;
        cartID = builder.cartID;
        image = builder.image;
        quantity = builder.quantity;
        stock = builder.stock;
        price = builder.price;
        total = builder.total;
    }


    public String getUserID() {
        return userID;
    }


    public String getUserName() {
        return userName;
    }


    public String getBookID() {
        return bookID;
    }


    public String getTitle() {
        return title;
    }


    public String getAuthor() {
        return author;
    }


    public String getCategory() {
        return category;
    }


    public String getCartID() {
        return cartID;
    }


    public String getImage() {
        return image;
    }


    public int getQuantity() {
        return quantity;
    }


    public double getPrice() {
        return price;
    }


    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getStock() {
        return stock;
    }


    public static class CartBuilder {
        private String userID, userName, bookID, title, author, category, cartID, image;
        private int quantity, stock;
        private double price, total;

        public CartBuilder (String title, int quantity, String userName, String image){
            this.title = title;
            this.quantity = quantity;
            this.userName = userName;
            this.image = image;
        }

        public CartBuilder setTitle(String title){
            this.title = title;
            return this;
        }

        public CartBuilder setQuantity(int quantity){
            this.quantity = quantity;
            return this;
        }

        public CartBuilder setUsername(String userName){
            this.userName = userName;
            return this;
        }

        public CartBuilder setImage(String image){
            this.image = image;
            return this;
        }

        public CartBuilder setUserID(String userID){
            this.userID = userID;
            return this;
        }

        public CartBuilder setBookID(String bookID){
            this.bookID = bookID;
            return this;
        }

        public CartBuilder setAuthor(String author){
            this.author = author;
            return this;
        }

        public CartBuilder setCategory(String category){
            this.category = category;
            return this;
        }

        public CartBuilder setCartID(String cartID){
            this.cartID = cartID;
            return this;
        }

        public CartBuilder setStock(int stock){
            this.stock = stock;
            return this;
        }

        public CartBuilder setPrice(Double price){
            this.price = price;
            return this;
        }

        public CartBuilder setTotal(Double total){
            this.total = total;
            return this;
        }

        public Cart buildCart(){
            return new Cart(this);
        }

    }
}