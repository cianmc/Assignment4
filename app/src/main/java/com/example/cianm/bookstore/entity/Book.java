package com.example.cianm.bookstore.entity;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by cianm on 28/03/2018.
 */

@IgnoreExtraProperties
public class Book {

    String id, title, author, price, category, image, quantity, noOfReviews, rating;

    public Book(){}

    public Book(String id, String title, String author, String price, String category, String quantity, String Image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.image = image;
    }

    public Book(String id, String title, String author, String price, String category, String quantity, String noOfReviews, String rating, String image) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
        this.noOfReviews = noOfReviews;
        this.rating = rating;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getNoOfReviews() {
        return noOfReviews;
    }

    public void setNoOfReviews(String noOfReviews) {
        this.noOfReviews = noOfReviews;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
