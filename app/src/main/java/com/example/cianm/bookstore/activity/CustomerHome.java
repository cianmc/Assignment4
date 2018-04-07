package com.example.cianm.bookstore.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cianm.bookstore.R;
import com.google.firebase.auth.FirebaseAuth;

public class CustomerHome extends AppCompatActivity {

    Button mSignOut, mSearchButton, mViewCart;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_home);
        setTitle("Customer Home");

        mSignOut = (Button) findViewById(R.id.signOut);
        mSearchButton = (Button) findViewById(R.id.searchBtn);
        mViewCart = (Button) findViewById(R.id.viewCartBtn);

        mAuth = FirebaseAuth.getInstance();

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerHome.this, SearchBook.class));
            }
        });

        mViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CustomerHome.this, ViewCart.class));
            }
        });

        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "Signing out...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent (CustomerHome.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }
}
