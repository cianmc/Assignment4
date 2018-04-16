package com.example.cianm.bookstore.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cianm.bookstore.R;
import com.example.cianm.bookstore.adapters.CartAdapter;
import com.example.cianm.bookstore.adapters.PurchaseHistoryAdapter;
import com.example.cianm.bookstore.adapters.SearchAdapter;
import com.example.cianm.bookstore.entity.GlobalVariables;
import com.example.cianm.bookstore.entity.Order;
import com.example.cianm.bookstore.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewPurchaseHistory extends AppCompatActivity {

    DatabaseReference mUserRef, mHistoryRef;
    FirebaseAuth mAuth;
    FirebaseUser fbUser;
    ArrayList<Order> orders;
    ListView mHistoryLV;
    Button mHomeBtn;
    TextView mName, mEmail, mShippingAddress, noHistory;


    String customerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_purchase_history);
        setTitle("Purchase history");

        customerID = ((GlobalVariables) ViewPurchaseHistory.this.getApplication()).getCurrentCustomer();
        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();

        orders = new ArrayList<>();

        mHomeBtn = (Button) findViewById(R.id.homeBtn);
        mHistoryLV = (ListView) findViewById(R.id.purchaseHistoryLV);
        mName = (TextView) findViewById(R.id.nameTV);
        mEmail = (TextView) findViewById(R.id.emailTV);
        mShippingAddress = (TextView) findViewById(R.id.shippingAddressTV);
        noHistory = (TextView) findViewById(R.id.noHistory);

        mUserRef = FirebaseDatabase.getInstance().getReference("User").child(customerID);
        mUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mName.setText(user.getName());
                mEmail.setText(user.getEmail());
                mShippingAddress.setText(user.getShippingAddress());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mHistoryRef = FirebaseDatabase.getInstance().getReference("PurchaseHistory");
        mHistoryRef.child(fbUser.getUid()).child(customerID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    noHistory.setVisibility(View.VISIBLE);
                    mHistoryLV.setVisibility(View.INVISIBLE);
                } else {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String title = ds.child("title").getValue(String.class);
                        int quantity = ds.child("quantity").getValue(Integer.class);
                        String image = ds.child("image").getValue(String.class);

                        Order order = new Order(title, image, quantity);

                        orders.add(order);
                    }
                    PurchaseHistoryAdapter adapter = new PurchaseHistoryAdapter(orders, ViewPurchaseHistory.this);
                    mHistoryLV.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (ViewPurchaseHistory.this, AdminHome.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
