package com.example.cianm.bookstore.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cianm.bookstore.R;
import com.example.cianm.bookstore.adapters.SearchAdapter;
import com.example.cianm.bookstore.entity.GlobalVariables;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchBook extends AppCompatActivity{

    EditText mSearchEditText;
    TextView mNoBooks;
    RecyclerView recyclerView;
    DatabaseReference mDatabase;
    FirebaseUser firebaseUser;
    ArrayList<String> idList;
    ArrayList<String> titleList;
    ArrayList<String> authorList;
    ArrayList<String> categoryList;
    ArrayList<Double> priceList;
    ArrayList<Integer> stockList;
    ArrayList <String> bookImageList;

    SearchAdapter searchAdapter;
    private static RecyclerViewClickListener itemListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_book);

        mSearchEditText = (EditText) findViewById(R.id.search_edit_text);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mNoBooks = (TextView) findViewById(R.id.checkBooks);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        idList = new ArrayList<>();
        titleList = new ArrayList<>();
        authorList = new ArrayList<>();
        categoryList = new ArrayList<>();
        priceList = new ArrayList<>();
        stockList = new ArrayList<>();
        bookImageList = new ArrayList<>();

        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty()){
                    setAdapter(s.toString());
                } else {
                    idList.clear();
                    titleList.clear();
                    authorList.clear();
                    categoryList.clear();
                    priceList.clear();
                    stockList.clear();
                    bookImageList.clear();
                    recyclerView.removeAllViews();
                }
            }
        });

    }

    private void setAdapter(final String searchedString) {
        mDatabase.child("Book").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the list for each new search
                idList.clear();
                titleList.clear();
                authorList.clear();
                categoryList.clear();
                priceList.clear();
                stockList.clear();
                bookImageList.clear();
                recyclerView.removeAllViews();

                int counter = 0;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (!dataSnapshot.exists()) {
                        recyclerView.setVisibility(View.INVISIBLE);
                        mNoBooks.setVisibility(View.VISIBLE);
                    } else {

                        String uid = ds.getKey();
                        String id = ds.child("id").getValue(String.class);
                        String title = ds.child("title").getValue(String.class);
                        String author = ds.child("author").getValue(String.class);
                        String category = ds.child("category").getValue(String.class);
                        Double price = ds.child("price").getValue(Double.class);
                        Integer stock = ds.child("stock").getValue(Integer.class);
                        String image = ds.child("image").getValue(String.class);

                        if (title.toLowerCase().contains(searchedString.toLowerCase())) {
                            idList.add(id);
                            titleList.add(title);
                            authorList.add(author);
                            categoryList.add(category);
                            priceList.add(price);
                            stockList.add(stock);
                            bookImageList.add(image);
                            counter++;
                        } else if (author.toLowerCase().contains(searchedString.toLowerCase())) {
                            idList.add(id);
                            titleList.add(title);
                            authorList.add(author);
                            categoryList.add(category);
                            priceList.add(price);
                            stockList.add(stock);
                            bookImageList.add(image);
                            counter++;
                        } else if (category.toLowerCase().contains(searchedString.toLowerCase())) {
                            idList.add(id);
                            titleList.add(title);
                            authorList.add(author);
                            categoryList.add(category);
                            priceList.add(price);
                            stockList.add(stock);
                            bookImageList.add(image);
                            counter++;
                        }
                        if (counter == 15) {
                            break;
                        }

                        searchAdapter = new SearchAdapter(SearchBook.this, idList, titleList, authorList, categoryList, priceList, stockList, bookImageList, new RecyclerViewClickListener() {
                            @Override
                            public void recyclerViewLisClicked(View v, int position) {
                                ((GlobalVariables) SearchBook.this.getApplication()).setCurrentBook(idList.get(position).toString());
                                startActivity(new Intent(SearchBook.this, ViewBook.class));
                            }
                        });
                        recyclerView.setAdapter(searchAdapter);


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setVisibility(View.VISIBLE);
        mNoBooks.setVisibility(View.INVISIBLE);
    }
}
