package com.example.cianm.bookstore.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.cianm.bookstore.R;
import com.example.cianm.bookstore.adapters.CartAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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

        mCartRV.setHasFixedSize(true);
        mCartRV.setLayoutManager(new LinearLayoutManager(this));
        mCartRV.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mProceedToPayment.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewCart.this, PaymentDetails.class));
            }
        });

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
}
