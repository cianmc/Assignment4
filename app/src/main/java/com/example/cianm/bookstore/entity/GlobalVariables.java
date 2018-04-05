package com.example.cianm.bookstore.entity;

import android.app.Application;

/**
 * Created by cianm on 04/04/2018.
 */

public class GlobalVariables extends Application{

    String currentBook;

    public String getCurrentBook() {
        return currentBook;
    }

    public void setCurrentBook(String currentBook) {
        this.currentBook = currentBook;
    }
}
