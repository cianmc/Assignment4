package com.example.cianm.bookstore.stockFacade;

import com.example.cianm.bookstore.entity.Book;

/**
 * Created by cianm on 12/04/2018.
 */

public interface OrderServiceFacade {
    boolean checkStock(Book book);
    boolean checkDiscount(double subTotal);
    boolean checkLoyalty(int noOfPurchases);
}
