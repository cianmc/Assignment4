package com.example.cianm.bookstore.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cianm.bookstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewCart extends AppCompatActivity {

    DatabaseReference mCartRef;
    FirebaseAuth mAuth;
    FirebaseUser fbUser;

    Button mProceedToPayment;
    RecyclerView mCartRV;
    TextView noCartItems;
    int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);
        setTitle("View Cart");
        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();

        mProceedToPayment = (Button) findViewById(R.id.proceedToPaymentBtn);
        mCartRV = (RecyclerView) findViewById(R.id.cartRecyclerView);
        noCartItems = (TextView) findViewById(R.id.noItems);
        noCartItems.setVisibility(View.INVISIBLE);

        mCartRV.setHasFixedSize(true);
        mCartRV.setLayoutManager(new LinearLayoutManager(this));
        mCartRV.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        addCartItems();
    }

    public void addCartItems(){

        final ArrayList<String> images = new ArrayList<>();
        final ArrayList<String> titles = new ArrayList<>();
        final ArrayList<String> quantities = new ArrayList<>();
        final ArrayList<String> totals = new ArrayList<>();
        counter = 0;

        mCartRef = FirebaseDatabase.getInstance().getReference("Cart");
        mCartRef.child(fbUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (!dataSnapshot.exists()) {
                        noCartItems.setVisibility(View.VISIBLE);
                        mCartRV.setVisibility(View.INVISIBLE);
                    } else {
                        String title = ds.child("title").getValue(String.class);
                        String quantity = ds.child("quantity").getValue(Integer.class).toString();
                        String total = ds.child("total").getValue(Double.class).toString();
                        String image = ds.child("image").getValue(String.class);

                        images.add(image);
                        titles.add(title);
                        quantities.add(quantity);
                        totals.add(total);
                        counter++;

                        CartAdapter cartAdapter = new CartAdapter(ViewCart.this, titles, quantities, totals, images);
                        mCartRV.setAdapter(cartAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    public void diplayCartItems(Map<String, Object> cartItems){
//
//        final ArrayList<String> images = new ArrayList<>();
//        final ArrayList<String> titles = new ArrayList<>();
//        final ArrayList<String> quantitys = new ArrayList<>();
//        final ArrayList<String> totals = new ArrayList<>();
//        int currentItem = 0;
//        final ArrayList<Map<String, String>> data2 = new ArrayList<Map<String, String>>();
//
//        for(Map.Entry<String, Object> entry : cartItems.entrySet()){
//
//            Map singleImage = (Map) entry.getValue();
//            images.add((String) singleImage.get("image"));
//
//            Map singleTitle = (Map) entry.getValue();
//            titles.add((String) singleTitle.get("title"));
//
//            Map singleQuantity = (Map) entry.getValue();
//            quantitys.add((String) singleQuantity.get("quantity"));
//
//            Map singleTotal = (Map) entry.getValue();
//            totals.add((String) singleTotal.get("total"));
//
//            Map<String, String> data = new HashMap<String, String>(4);
//            data.put("image", images.get(currentItem));
//            data.put("title", titles.get(currentItem));
//            data.put("quantity", quantitys.get(currentItem).toString());
//            data.put("total", totals.get(currentItem).toString());
//            currentItem++;
//            data2.add(data);
//        }
//        String[] from = {"image", "title", "quantity", "total"};
//        int[] to = {R.id.bookImageCart, R.id.titleCartItem, R.id.quantityCartItem, R.id.totalCartItem};
//        CartAdapter cartAdapter = new CartAdapter(ViewCart.this, data2, R.layout.cart_list_item, from, to);
//        mCartLV.setAdapter(cartAdapter);
//    }
}
