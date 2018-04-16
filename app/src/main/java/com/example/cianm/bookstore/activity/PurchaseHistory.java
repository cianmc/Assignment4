package com.example.cianm.bookstore.activity;

import android.content.Intent;
import android.opengl.Visibility;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.cianm.bookstore.R;
import com.example.cianm.bookstore.entity.GlobalVariables;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PurchaseHistory extends AppCompatActivity {

    DatabaseReference mUser;
    ListView mCustomersList;
    ArrayList<String> names, ids;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_history);
        setTitle("View Purchase History");

        mCustomersList = (ListView) findViewById(R.id.customerListView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar2);

        names = new ArrayList<>();
        ids = new ArrayList<>();

        mUser = FirebaseDatabase.getInstance().getReference("User");
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String name = ds.child("name").getValue(String.class);
                    String id = ds.child("id").getValue(String.class);

                    names.add(name);
                    ids.add(id);
                }
                ArrayAdapter arrayAdapter = new ArrayAdapter(PurchaseHistory.this, android.R.layout.simple_list_item_1, names);
                mCustomersList.setAdapter(arrayAdapter);
                mCustomersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        mCustomersList.setVisibility(View.INVISIBLE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        ((GlobalVariables) PurchaseHistory.this.getApplication()).setCurrentCustomer(ids.get(i).toString());
                        startActivity(new Intent(PurchaseHistory.this, ViewPurchaseHistory.class));
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onResume(){
        super.onResume();
        mCustomersList.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
