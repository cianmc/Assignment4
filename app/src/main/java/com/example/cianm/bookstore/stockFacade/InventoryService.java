package com.example.cianm.bookstore.stockFacade;

import com.example.cianm.bookstore.entity.Book;

/**
 * Created by cianm on 12/04/2018.
 */

public class InventoryService {

    public static boolean inStock(Book book){
        boolean inStock;
        int stock = book.getStock();
        if (stock == 0){
            inStock = false;
        } else {
            inStock = true;
        }
        return inStock;
    }
}
