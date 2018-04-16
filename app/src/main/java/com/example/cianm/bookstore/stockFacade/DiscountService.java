package com.example.cianm.bookstore.stockFacade;

import com.example.cianm.bookstore.entity.Cart;

/**
 * Created by cianm on 15/04/2018.
 */

public class DiscountService {
    public static boolean getDiscount(Cart cart) {
        boolean getsDiscount;
        double subtotal = cart.getTotal();
        if (subtotal < 50.00) {
            getsDiscount = false;
        } else {
            getsDiscount = true;
        }
        return getsDiscount;
    }
}
