package com.example.cianm.bookstore.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cianm.bookstore.R;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHome extends AppCompatActivity {

    Button mSignOut, mAddBook, mSearchBook, mViewCart;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        setTitle("Admin Home");

        mAuth = FirebaseAuth.getInstance();

        mSignOut = (Button) findViewById(R.id.signOut);
        mAddBook = (Button) findViewById(R.id.addBook);
        mSearchBook = (Button) findViewById(R.id.searchBtn);
        mViewCart = (Button) findViewById(R.id.viewCartBtn);

        mAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome.this, AddBook.class));
            }
        });

        mSearchBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome.this, SearchBook.class));
            }
        });

        mViewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminHome.this, ViewCart.class));
            }
        });

        mSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Toast.makeText(getApplicationContext(), "Signing out...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent (AdminHome.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
