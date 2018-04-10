package com.example.cianm.bookstore.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cianm.bookstore.R;
import com.example.cianm.bookstore.adapters.AllBooksAdapter;
import com.example.cianm.bookstore.adapters.SearchAdapter;
import com.example.cianm.bookstore.entity.Book;
import com.example.cianm.bookstore.entity.GlobalVariables;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class AdminHome extends AppCompatActivity {

    Button mSignOut, mAddBook, mSearchBook, mViewCart, mPurchaseHistory, mTitleAscending, mTitleDescending, mAuthorAscending, mAuthorDescending, mPriceAscending, mPriceDescending;
    TextView mNoBooks;
    ListView mAllBooks;
    FirebaseAuth mAuth;
    DatabaseReference mBookRef;
    ArrayList<Book> allBooks;
    AllBooksAdapter booksAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        setTitle("Admin Home");

        mAuth = FirebaseAuth.getInstance();

        allBooks = new ArrayList<>();

        // Navigation Buttons & TextViews & ListViews
        mSignOut = (Button) findViewById(R.id.signOut);
        mAddBook = (Button) findViewById(R.id.addBook);
        mSearchBook = (Button) findViewById(R.id.searchBtn);
        mViewCart = (Button) findViewById(R.id.viewCartBtn);
        mPurchaseHistory = (Button) findViewById(R.id.customerPurchaseHistoryBtn);
        mNoBooks = (TextView) findViewById(R.id.noBooks);
        mAllBooks = (ListView) findViewById(R.id.allBooksListView);

        // Ascending & Descending Buttons
        mTitleAscending = (Button) findViewById(R.id.titleAscendingBtn);
        mTitleDescending = (Button) findViewById(R.id.titleDescendingBtn);
        mAuthorAscending = (Button) findViewById(R.id.authorAscedingBtn);
        mAuthorDescending = (Button) findViewById(R.id.authorDescedingBtn);
        mPriceAscending = (Button) findViewById(R.id.priceAscendingBtn);
        mPriceDescending = (Button) findViewById(R.id.priceDescendingBtn);

        getAllBooks();
        sortBooks();

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

    // Populates the list with all the books in the Database
    public void getAllBooks(){
        mBookRef = FirebaseDatabase.getInstance().getReference("Book");
        mBookRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(!dataSnapshot.exists()){
                        mNoBooks.setVisibility(View.VISIBLE);
                        mAllBooks.setVisibility(View.INVISIBLE);
                    } else {
                        String id = ds.child("id").getValue(String.class);
                        String title = ds.child("title").getValue(String.class);
                        String author = ds.child("author").getValue(String.class);
                        String category = ds.child("category").getValue(String.class);
                        String image = ds.child("image").getValue(String.class);
                        Double price = ds.child("price").getValue(Double.class);
                        Integer stock = ds.child("stock").getValue(Integer.class);
                        Integer noReviews = ds.child("noOfReviews").getValue(Integer.class);
                        Double rating = ds.child("rating").getValue(Double.class);

                        Book book = new Book(id, title, author, category, image, noReviews, stock, price, rating);
                        allBooks.add(book);
                    }
                }
                booksAdapter = new AllBooksAdapter(allBooks, AdminHome.this);
                mAllBooks.setAdapter(booksAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void sortBooks(){
        mTitleAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitleAscending.setVisibility(View.INVISIBLE);
                mTitleDescending.setVisibility(View.VISIBLE);
                Collections.sort(allBooks, new Comparator<Book>() {
                    @Override
                    public int compare(Book book, Book t1) {
                        return book.getTitle().compareTo(t1.getTitle());
                    }
                });
                booksAdapter.notifyDataSetChanged();
            }
        });

        mTitleDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTitleDescending.setVisibility(View.INVISIBLE);
                mTitleAscending.setVisibility(View.VISIBLE);
                Collections.sort(allBooks, new Comparator<Book>() {
                    @Override
                    public int compare(Book book, Book t1) {
                        return t1.getTitle().compareTo(book.getTitle());
                    }
                });
                booksAdapter.notifyDataSetChanged();
            }
        });

        mAuthorAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuthorDescending.setVisibility(View.VISIBLE);
                mAuthorAscending.setVisibility(View.INVISIBLE);
                Collections.sort(allBooks, new Comparator<Book>() {
                    @Override
                    public int compare(Book book, Book t1) {
                        return book.getAuthor().compareTo(t1.getAuthor());
                    }
                });
                booksAdapter.notifyDataSetChanged();
            }
        });

        mAuthorDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuthorDescending.setVisibility(View.INVISIBLE);
                mAuthorAscending.setVisibility(View.VISIBLE);
                Collections.sort(allBooks, new Comparator<Book>() {
                    @Override
                    public int compare(Book book, Book t1) {
                        return t1.getAuthor().compareTo(book.getAuthor());
                    }
                });
                booksAdapter.notifyDataSetChanged();
            }
        });

        mPriceAscending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPriceAscending.setVisibility(View.INVISIBLE);
                mPriceDescending.setVisibility(View.VISIBLE);
                Collections.sort(allBooks, new Comparator<Book>() {
                    @Override
                    public int compare(Book book, Book t1) {
                        return t1.getPrice().compareTo(book.getPrice());
                    }
                });
                booksAdapter.notifyDataSetChanged();
            }
        });

        mPriceDescending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPriceAscending.setVisibility(View.VISIBLE);
                mPriceDescending.setVisibility(View.INVISIBLE);
                Collections.sort(allBooks, new Comparator<Book>() {
                    @Override
                    public int compare(Book book, Book t1) {
                        return book.getPrice().compareTo(t1.getPrice());
                    }
                });
                booksAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onResume(){
        super.onResume();
        mAllBooks.setVisibility(View.VISIBLE);
        mNoBooks.setVisibility(View.INVISIBLE);
    }
}
