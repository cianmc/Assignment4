package com.example.cianm.bookstore.stockFacade;

import com.example.cianm.bookstore.entity.User;

/**
 * Created by cianm on 15/04/2018.
 */

public class LoyaltyService {
    public static boolean getLoyalyBonus(User user){
        boolean getsLoyaltyBonus;
        int noOfPurchases = user.getNoOfPurchases();
        if(noOfPurchases >=5){
            getsLoyaltyBonus = true;
        } else {
            getsLoyaltyBonus = false;
        }
        return  getsLoyaltyBonus;
    }
}
