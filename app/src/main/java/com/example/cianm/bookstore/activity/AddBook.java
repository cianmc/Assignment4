package com.example.cianm.bookstore.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cianm.bookstore.R;
import com.example.cianm.bookstore.entity.Book;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.UUID;

public class AddBook extends AppCompatActivity {

    DatabaseReference mBookRef;

    EditText mTitle, mAuthor, mPrice, mStock;
    Spinner mCategory;
    Button mAddBook, mChooseImage;
    ImageView mImageView;
    ProgressBar mProgressBar;
    Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;
    FirebaseStorage mStorage;
    StorageReference mStorageReference;


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
        mStock = (EditText) findViewById(R.id.stockEditText);
        mCategory = (Spinner) findViewById(R.id.categorySpinner);
        mAddBook = (Button) findViewById(R.id.addBookBtn);
        mChooseImage = (Button) findViewById(R.id.chooseImage);
        mImageView = (ImageView) findViewById(R.id.imageView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        final String[] category = getResources().getStringArray(R.array.bookCategory);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(AddBook.this, android.R.layout.simple_spinner_dropdown_item, category);
        mCategory.setAdapter(arrayAdapter);

        mStorage = FirebaseStorage.getInstance();
        mStorageReference = mStorage.getReference();

        mBookRef = FirebaseDatabase.getInstance().getReference("Book");
        bookID = mBookRef.push().getKey();

        mAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAddBook.setVisibility(View.INVISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
                final String title = mTitle.getText().toString();
                final String author = mAuthor.getText().toString();
                final Double price = Double.parseDouble(mPrice.getText().toString());
                final String category = mCategory.getSelectedItem().toString();
                final int stock = Integer.parseInt(mStock.getText().toString());
                if (title.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter in a title", Toast.LENGTH_SHORT).show();
                    return;
                } else if (author.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter in a author", Toast.LENGTH_SHORT).show();
                    return;
                } else if (price.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter in a price", Toast.LENGTH_SHORT).show();
                    return;
                } else if (stock <= 0) {
                    Toast.makeText(getApplicationContext(), "Stock cannot be 0 or a negative number", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    final Double avgRating = 0.00;
                    final int noOfRatings = 0;
                    StorageReference ref = mStorageReference.child("images/" + UUID.randomUUID().toString());
                    ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests")
                            Uri uri = taskSnapshot.getDownloadUrl();
                            String imagePath = uri.toString();
                            Toast.makeText(AddBook.this, "Uploaded Image", Toast.LENGTH_SHORT).show();
                            book = new Book(bookID, title, author,category, imagePath, noOfRatings, stock, price, avgRating);
                            mBookRef.child(bookID).setValue(book);
                            mAddBook.setVisibility(View.INVISIBLE);
                            Toast.makeText(getApplicationContext(), "Book added: " + book.getTitle(), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(AddBook.this, AdminHome.class));
                        }
                    });
                }
            }
        });

        mChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                mImageView.setImageBitmap(bitmap);
                StorageReference reference = mStorageReference.child("images").child(filePath.getLastPathSegment());
                reference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Toast.makeText(AddBook.this, "Uploaded", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAddBook.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
