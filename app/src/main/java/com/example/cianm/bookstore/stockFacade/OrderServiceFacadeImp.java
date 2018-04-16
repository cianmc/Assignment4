package com.example.cianm.bookstore.stockFacade;

import com.example.cianm.bookstore.entity.Book;
import com.example.cianm.bookstore.entity.Cart;
import com.example.cianm.bookstore.entity.User;

/**
 * Created by cianm on 12/04/2018.
 */

public class OrderServiceFacadeImp implements OrderServiceFacade {

    @Override
    public boolean checkStock(Book book) {
        boolean orderFulfilled=false;
        if(InventoryService.inStock(book)){
            orderFulfilled = true;
        } else {
            orderFulfilled = false;
        }
        return orderFulfilled;
    }

    @Override
    public boolean checkDiscount(double subTotal) {
        boolean discountFulfilled = false;
        Cart cart = new Cart();
        cart.setTotal(subTotal);
        if(DiscountService.getDiscount(cart)){
            discountFulfilled = true;
        } else {
            discountFulfilled = false;
        }
        return discountFulfilled;
    }

    @Override
    public boolean checkLoyalty(int noOfPurchases) {
        boolean loyaltyFulfilled = false;
        User user = new User();
        user.setNoOfPurchases(noOfPurchases);
        if(LoyaltyService.getLoyalyBonus(user)){
            loyaltyFulfilled = true;
        } else {
            loyaltyFulfilled = false;
        }
        return loyaltyFulfilled;
    }
}
