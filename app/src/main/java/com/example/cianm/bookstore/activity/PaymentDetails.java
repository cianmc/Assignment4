package com.example.cianm.bookstore.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cianm.bookstore.R;
import com.example.cianm.bookstore.entity.Cart;
import com.example.cianm.bookstore.entity.User;
import com.example.cianm.bookstore.stockFacade.OrderFulfillmentController;
import com.example.cianm.bookstore.stockFacade.OrderServiceFacadeImp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaymentDetails extends AppCompatActivity {

    DatabaseReference mUserRef, mCartRef, mPurchaseRef, mBookRef;
    FirebaseAuth mAuth;
    FirebaseUser fbUser;
    RadioGroup mCardTypeGroup;
    RadioButton mCreditCard, mDebitCard;
    EditText mShippingAddress, mCardNumber;
    TextView mSubTotal, mDiscount, mDiscountValue, mLoyalty, mLoyaltyValue, mTotal;
    Button mConfirmOrder;

    User user;
    Cart cart;

    String userName, title, email, password, adminUid, bookID, image, cardNumber, address, cardType;
    int noOfOrders, quantity, stock, newNoOrders;
    Double total, subTotal, discount, loyalty;
    boolean detailsSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        setTitle("Payment Details");

        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();
        loyalty = 0.00;
        discount = 0.00;
        detailsSaved = false;

        // RadioButtons & Buttons
        mCardTypeGroup = (RadioGroup) findViewById(R.id.cardRadioGroup);
        mCreditCard = (RadioButton) findViewById(R.id.creditRadioBtn);
        mDebitCard = (RadioButton) findViewById(R.id.debitRadioBtn);
        mConfirmOrder = (Button) findViewById(R.id.confirmOrderBtn);

        // EditTexts
        mShippingAddress = (EditText) findViewById(R.id.shippingAddress);
        mCardNumber = (EditText) findViewById(R.id.cardNumber);

        // TextViews
        mSubTotal = (TextView) findViewById(R.id.subTotal);
        mDiscount = (TextView) findViewById(R.id.discountTV);
        mDiscountValue = (TextView) findViewById(R.id.orderDiscount);
        mLoyalty = (TextView) findViewById(R.id.loyaltyBonusTV);
        mLoyaltyValue = (TextView) findViewById(R.id.loyaltyBonus);
        mTotal = (TextView) findViewById(R.id.total);

        mDiscount.setVisibility(View.INVISIBLE);
        mDiscountValue.setVisibility(View.INVISIBLE);
        mLoyalty.setVisibility(View.INVISIBLE);
        mLoyaltyValue.setVisibility(View.INVISIBLE);

        getUserDetails();
        getUserPaymentDetails();
        calTotal();
        getAdminUid();

        mConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserDetails();
            }
        });

    }

    public void calTotal() {
        subTotal = 0.00;
        final OrderFulfillmentController controller = new OrderFulfillmentController();
        controller.facade=new OrderServiceFacadeImp();
        mCartRef = FirebaseDatabase.getInstance().getReference("Cart").child(fbUser.getUid());
        mCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Double total = ds.child("total").getValue(Double.class);
                    subTotal = subTotal + total;
                }
                mSubTotal.setText("€" + subTotal.toString());
                controller.applyDiscount(subTotal);
                boolean getsDiscount = controller.discountFulfilled;
                if (!getsDiscount) {
                    Toast.makeText(getApplicationContext(), "Spend €50 to get 10% off" , Toast.LENGTH_LONG).show();
                } else {
                    mDiscount.setVisibility(View.VISIBLE);
                    mDiscountValue.setVisibility(View.VISIBLE);
                    discount = (subTotal * (10.00f / 100.00f));
                    Double newDiscount = Math.round(discount * 100.0) / 100.0;
                    mDiscountValue.setText("€" + newDiscount.toString());
                }
                controller.applyLoyalty(noOfOrders);
                boolean getsLoyalty = controller.loyaltyFulfilled;
                if (!getsLoyalty) {
                    Toast.makeText(getApplicationContext(), "Get 10% off your fifth purchase" , Toast.LENGTH_LONG).show();
                } else {
                    mLoyalty.setVisibility(View.VISIBLE);
                    mLoyaltyValue.setVisibility(View.VISIBLE);
                    loyalty = (subTotal * (10.00f / 100.00f));
                    Double newloyalty = Math.round(loyalty * 100.0) / 100.0;
                    mLoyaltyValue.setText("€" + newloyalty.toString());
                }
                total = subTotal - (discount + loyalty);
                Double newTotal = Math.round(total * 100.0) / 100.0;
                mTotal.setText("€" + newTotal.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getUserDetails() {
        mUserRef = FirebaseDatabase.getInstance().getReference("User").child(fbUser.getUid());
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName = user.getName();
                email = user.getEmail();
                password = user.getPassword();
                noOfOrders = user.getNoOfPurchases();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getAdminUid() {
        mUserRef = FirebaseDatabase.getInstance().getReference("User");
        mUserRef.orderByChild("name").equalTo("Admin").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    adminUid = ds.child("id").getValue(String.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateUserDetails() {
        cardNumber = mCardNumber.getText().toString();
        address = mShippingAddress.getText().toString();
        cardType = "";
        newNoOrders = noOfOrders + 1;
        if (mDebitCard.isChecked()) {
            cardType = "Debit";
        } else if (mCreditCard.isChecked()) {
            cardType = "Credit";
        }

        if (TextUtils.isEmpty(address)) {
            mShippingAddress.setError("Please enter in an address");
            return;
        } else if (TextUtils.isEmpty(cardNumber)) {
            mCardNumber.setError("Please enter in a card number");
            return;
        } else if (mCardNumber.getText().length() < 16 || mCardNumber.getText().length() > 16) {
            mCardNumber.setError("Card number must be 16 digits long");
            return;
        } else if (!mCreditCard.isChecked() && !mDebitCard.isChecked()) {
            Toast.makeText(getApplicationContext(), "Please select a card type", Toast.LENGTH_LONG).show();
        } else {
            placeOrder();
        }
    }

    public void placeOrder() {
        mBookRef = FirebaseDatabase.getInstance().getReference("Book");
        mPurchaseRef = FirebaseDatabase.getInstance().getReference("PurchaseHistory").child(adminUid);
        mCartRef = FirebaseDatabase.getInstance().getReference("Cart").child(fbUser.getUid());
        mCartRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String cartKey = ds.getKey();
                    bookID = ds.child("bookID").getValue(String.class);
                    title = ds.child("title").getValue(String.class);
                    stock = ds.child("stock").getValue(Integer.class);
                    quantity = ds.child("quantity").getValue(Integer.class);
                    image = ds.child("image").getValue(String.class);
                    final String purchaseID = mPurchaseRef.push().getKey();
                    cart = new Cart.CartBuilder(title, quantity, userName, image).buildCart();
                    mPurchaseRef.child(fbUser.getUid()).child(purchaseID).setValue(cart);
                    int newStock = stock - quantity;
                    mBookRef.child(bookID).child("stock").setValue(newStock);
                    mCartRef.child(cartKey).removeValue();
                }
                if (!detailsSaved){
                    checkUserType();
                } else {
                    saveDetails();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void checkUserType() {
        Toast.makeText(getApplicationContext(), "Order successfully placed", Toast.LENGTH_LONG).show();
        if (userName.equalsIgnoreCase("Admin")) {
            startActivity(new Intent(PaymentDetails.this, AdminHome.class));
        } else {
            startActivity(new Intent(PaymentDetails.this, CustomerHome.class));
        }
    }

    public void saveDetails() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i) {
                    case DialogInterface.BUTTON_POSITIVE:
                        user = new User.UserBuilder(userName, email, password)
                                .setId(fbUser.getUid())
                                .setShippingAddress(address)
                                .setCardNumber(cardNumber)
                                .setNoOfPurchases(newNoOrders)
                                .setCardType(cardType)
                                .buildUser();
                        mUserRef.child(fbUser.getUid()).setValue(user);
                        checkUserType();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        checkUserType();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentDetails.this);
        builder.setMessage("Do you want to save your payment details?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void getUserPaymentDetails(){
        mUserRef = FirebaseDatabase.getInstance().getReference("User").child(fbUser.getUid());
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.getShippingAddress() != null || user.getCardType() != null || user.getCardNumber() != null) {
                    String cardType = user.getCardType();
                    mShippingAddress.setText(user.getShippingAddress());
                    mCardNumber.setText(user.getCardNumber());
                    if (cardType.equalsIgnoreCase("Credit")) {
                        mCardTypeGroup.check(R.id.creditRadioBtn);
                    } else {
                        mCardTypeGroup.check(R.id.debitRadioBtn);
                    }
                    detailsSaved=true;
                } else {
                    detailsSaved=false;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

