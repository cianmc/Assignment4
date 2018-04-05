package com.example.cianm.bookstore.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cianm.bookstore.R;
import com.example.cianm.bookstore.entity.Book;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddBook extends AppCompatActivity {

    DatabaseReference mBookRef;

    EditText mTitle, mAuthor, mPrice, mQuantity;
    Spinner mCategory;
    Button mAddBook;

    Book book;
    String bookID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        setTitle("Add Book");

        mTitle = (EditText) findViewById(R.id.titleEditText);
        mAuthor = (EditText) findViewById(R.id.authorEditText);
        mPrice = (EditText) findViewById(R.id.priceEditText);
        mQuantity = (EditText) findViewById(R.id.quantityEditText);
        mCategory = (Spinner) findViewById(R.id.categorySpinner);
        mAddBook = (Button) findViewById(R.id.addBookBtn);

        final String [] category = getResources().getStringArray(R.array.bookCategory);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddBook.this, android.R.layout.simple_spinner_dropdown_item, category);
        mCategory.setAdapter(arrayAdapter);

        mBookRef = FirebaseDatabase.getInstance().getReference("Book");
        bookID = mBookRef.push().getKey();

        mAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mTitle.getText().toString();
                String author = mAuthor.getText().toString();
                String price = mPrice.getText().toString();
                String category = mCategory.getSelectedItem().toString();
                int quantity = Integer.parseInt(mQuantity.getText().toString());
                if (title.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter in a title", Toast.LENGTH_SHORT).show();
                    return;
                } else if (author.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter in a author", Toast.LENGTH_SHORT).show();
                    return;
                } else if (price.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter in a price", Toast.LENGTH_SHORT).show();
                    return;
                }  else if (quantity <= 0) {
                    Toast.makeText(getApplicationContext(), "Quantity cannot be 0 or a negative number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    String quantityS = Integer.toString(quantity);
                    String avgRating = "0";
                    String noOfRatings = "0";
                    book = new Book(bookID, title, author, price, category, quantityS, avgRating, noOfRatings);
                    mBookRef.child(bookID).setValue(book);
                    mAddBook.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Book added: " + book.getTitle(), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(AddBook.this, AdminHome.class));
                }
            }
        });

    }
}
