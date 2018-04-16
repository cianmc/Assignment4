package com.example.cianm.bookstore.stockFacade;

import com.example.cianm.bookstore.entity.Book;

/**
 * Created by cianm on 13/04/2018.
 */

public class OrderFulfillmentController {
    public OrderServiceFacade facade;
    public boolean orderFulfilled=false;
    public boolean discountFulfilled=false;
    public boolean loyaltyFulfilled=false;
    public void orderProduct(Book book){
        orderFulfilled = facade.checkStock(book);
    }
    public void applyDiscount(double subTotal){
        discountFulfilled = facade.checkDiscount(subTotal);
    }
    public void applyLoyalty(int noOfPurchases){
        loyaltyFulfilled = facade.checkLoyalty(noOfPurchases);
    }
}

