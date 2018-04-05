package com.example.cianm.bookstore.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cianm.bookstore.R;
import com.example.cianm.bookstore.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mFirebaseDatabase;

    private EditText mEmail, mPassword, mName;
    private Button mRegister;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mName = (EditText) findViewById(R.id.name);
        mRegister = (Button) findViewById(R.id.register);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fbUser = firebaseAuth.getCurrentUser();
                if (fbUser != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in" + fbUser.getUid());
                    mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("User");
                    mFirebaseDatabase.child(fbUser.getUid()).setValue(user);
                    Toast.makeText(getApplicationContext(), "Successfully signed in with: " + fbUser.getEmail(), Toast.LENGTH_SHORT).show();
                    if (user.getEmail().equalsIgnoreCase("admin@gmail.com")) {
                        startActivity(new Intent(MainActivity.this, AdminHome.class));
                    } else {
                        startActivity(new Intent(MainActivity.this, CustomerHome.class));
                    }
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();
                String name = mName.getText().toString();
                String noOfPurchases = "0";
                if (email.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter in a email address", Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter in a password address", Toast.LENGTH_SHORT).show();
                }else if (name.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter in your name address", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(MainActivity.this, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password);
                    user = new User(name, email, password, noOfPurchases);
                    mRegister.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
