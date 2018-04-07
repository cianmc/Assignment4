package com.example.cianm.bookstore.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cianm.bookstore.R;
import com.example.cianm.bookstore.entity.Book;
import com.example.cianm.bookstore.entity.Cart;
import com.example.cianm.bookstore.entity.Comment;
import com.example.cianm.bookstore.entity.GlobalVariables;
import com.example.cianm.bookstore.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewBook extends AppCompatActivity {

    DatabaseReference mBookRef, mCartRef, mUserRef, mCommentRef;
    FirebaseAuth mAuth;
    FirebaseUser fbUser;
    Context context;

    TextView mTitle, mAuthor, mCategory, mPrice, mStock, stockTV, noComments, mQuantity;
    EditText eTitle, eAuthor, ePrice, eStock, mComment;
    Button bEdit, bSave, bAddToCart, bPostReview, bMinusQuantity, bPlusQuantity;
    RatingBar rating, displayRating;
    Spinner spinner;
    ListView mCommentLV;
    ImageView mImageView;
    String bookID, userName, title, image;
    Integer quan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);
        setTitle("View Book");
        bookID  = ((GlobalVariables) ViewBook.this.getApplication()).getCurrentBook();
        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();

        //TextView
        mTitle = (TextView) findViewById(R.id.viewTitle);
        mAuthor = (TextView) findViewById(R.id.viewAuthor);
        mCategory = (TextView) findViewById(R.id.viewCategory);
        mPrice = (TextView) findViewById(R.id.viewPrice);
        mStock = (TextView) findViewById(R.id.viewStock);
        stockTV = (TextView) findViewById(R.id.stockTV);
        noComments = (TextView) findViewById(R.id.noComments);
        mQuantity = (TextView) findViewById(R.id.quantityValue);

        //EditText
        eTitle = (EditText) findViewById(R.id.editTitle);
        eAuthor = (EditText) findViewById(R.id.editAuthor);
        ePrice = (EditText) findViewById(R.id.editPrice);
        eStock = (EditText) findViewById(R.id.editStock);
        mComment = (EditText) findViewById(R.id.editComment);

        //Buttons
        bEdit = (Button) findViewById(R.id.editBtn);
        bSave = (Button) findViewById(R.id.saveBtn);
        bAddToCart = (Button) findViewById(R.id.addToCartBtn);
        bPostReview = (Button) findViewById(R.id.postReviewBtn);
        bMinusQuantity = (Button) findViewById(R.id.quantityMinusBtn);
        bPlusQuantity = (Button) findViewById(R.id.quantityPlusBtn);

        //ProgressBar & Spinner & ListView & ImageView
        mImageView = (ImageView) findViewById(R.id.imageViewBook);
        rating = (RatingBar) findViewById(R.id.ratingBar);
        spinner = (Spinner) findViewById(R.id.spinner);
        mCommentLV = (ListView) findViewById(R.id.commentsListView);
        displayRating = (RatingBar) findViewById(R.id.avgRatingBar);
        displayRating.setNumStars(5);
        displayRating.setStepSize(0.5f);

        noComments.setVisibility(View.INVISIBLE);
        bMinusQuantity.setVisibility(View.INVISIBLE);

        mUserRef = FirebaseDatabase.getInstance().getReference("User");
        mUserRef.child(fbUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userName  = user.getName();
                if(!userName.equalsIgnoreCase("Admin")){
                    bEdit.setVisibility(View.INVISIBLE);
                    bSave.setVisibility(View.INVISIBLE);
                    mStock.setVisibility(View.INVISIBLE);
                    eStock.setVisibility(View.INVISIBLE);
                    stockTV.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getBookDetails();

        bPlusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quan = Integer.parseInt(mQuantity.getText().toString());
                bMinusQuantity.setVisibility(View.VISIBLE);
                quan++;
                mQuantity.setText(Integer.toString(quan));
            }
        });

        bMinusQuantity.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                quan = Integer.parseInt(mQuantity.getText().toString());
                quan--;
                if (quan == 1){
                    bMinusQuantity.setVisibility(View.INVISIBLE);
                }
                mQuantity.setText(Integer.toString(quan));
            }
        });

        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getBookDetails();
                editBook();
            }
        });

        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveBookDetails();
            }
        });

        bAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToCart();
            }
        });

        bPostReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                leaveCommentRating();
            }
        });

        mCommentRef = FirebaseDatabase.getInstance().getReference("Comment");
        mCommentRef.child(bookID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()){
                    noComments.setVisibility(View.VISIBLE);
                    mCommentLV.setVisibility(View.INVISIBLE);
                }else{
                    displayComments((Map<String, Object>) dataSnapshot.getValue());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void leaveCommentRating(){

        final int rate = rating.getProgress();

        if(rate == 0){
            Toast.makeText(getApplicationContext(), "You must give the book a rating", Toast.LENGTH_LONG).show();
        } else {

            mBookRef = FirebaseDatabase.getInstance().getReference("Book");
            mBookRef.orderByChild("id").equalTo(bookID).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Book book = ds.getValue(Book.class);
                        int noOfRatings = book.getNoOfReviews();
                        Double rating = book.getRating();

                        noOfRatings++;

                        Double newRating = (rate + rating) / noOfRatings;

                        mCommentRef = FirebaseDatabase.getInstance().getReference("Comment");

                        String commentID = mCommentRef.push().getKey();
                        String sRate = String.valueOf(rate);
                        String comment = mComment.getText().toString();

                        Comment com = new Comment(userName, title, sRate, comment, bookID);
                        mCommentRef = FirebaseDatabase.getInstance().getReference("Comment");
                        mCommentRef.child(bookID).child(commentID).setValue(com);
                        mBookRef.child(bookID).child("noOfReviews").setValue(noOfRatings);
                        mBookRef.child(bookID).child("rating").setValue(newRating);
                        Toast.makeText(getApplicationContext(), "You gave a rating of " + sRate, Toast.LENGTH_LONG).show();
                        if (userName.equalsIgnoreCase("Admin")) {
                            startActivity(new Intent(ViewBook.this, AdminHome.class));
                        } else {
                            startActivity(new Intent(ViewBook.this, CustomerHome.class));
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void addToCart(){
        mCartRef = FirebaseDatabase.getInstance().getReference("Cart");

        String cartID = mCartRef.push().getKey();
        String title = mTitle.getText().toString();
        String author = mAuthor.getText().toString();
        String category = mCategory.getText().toString();
        Double price = Double.parseDouble(mPrice.getText().toString());
        int quantity = Integer.parseInt(mQuantity.getText().toString());
        Double total = price*quantity;

        Cart cart = new Cart(userName, bookID, title, author, category, cartID, quantity, price, total, image);
        mCartRef.child(fbUser.getUid()).child(cartID).setValue(cart);
        Toast.makeText(getApplicationContext(), "Added " + cart.getTitle() + " to cart", Toast.LENGTH_LONG).show();
        if (userName.equalsIgnoreCase("Admin")) {
            startActivity(new Intent(ViewBook.this, AdminHome.class));
        } else {
            startActivity(new Intent(ViewBook.this, CustomerHome.class));
        }
    }

    public void saveBookDetails(){

        String newTitle = eTitle.getText().toString();
        String newAuthor = eAuthor.getText().toString();
        String newCategory = spinner.getSelectedItem().toString();
        Double newPrice = Double.parseDouble(ePrice.getText().toString());
        int newStock = Integer.parseInt(eStock.getText().toString());

        String newStockS = Integer.toString(newStock);
        String newPriceS = Double.toString(newPrice);
        mBookRef.child(bookID).child("title").setValue(newTitle);
        mBookRef.child(bookID).child("author").setValue(newAuthor);
        mBookRef.child(bookID).child("category").setValue(newCategory);
        mBookRef.child(bookID).child("price").setValue(newPrice);
        mBookRef.child(bookID).child("stock").setValue(newStock);

        saveBook();

        mTitle.setText(newTitle);
        mAuthor.setText(newAuthor);
        mCategory.setText(newCategory);
        mPrice.setText(newPriceS);
        mStock.setText(newStockS);

    }

    public void getBookDetails(){
        mBookRef = FirebaseDatabase.getInstance().getReference("Book");
        mBookRef.orderByChild("id").equalTo(bookID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    Book book = ds.getValue(Book.class);

                    mTitle.setText(book.getTitle());
                    mAuthor.setText(book.getAuthor());
                    mCategory.setText(book.getCategory());
                    mPrice.setText(String.valueOf(book.getPrice()));
                    mStock.setText(String.valueOf(book.getStock()));
                    mQuantity.setText("1");
                    displayRating.setRating(Float.parseFloat(book.getRating().toString()));

                    title = book.getTitle();
                    image = book.getImage();

                    eTitle.setText(book.getTitle(), TextView.BufferType.EDITABLE);
                    eAuthor.setText(book.getAuthor(), TextView.BufferType.EDITABLE);
                    ePrice.setText(String.valueOf(book.getPrice()), TextView.BufferType.EDITABLE);
                    eStock.setText(String.valueOf(book.getStock()), TextView.BufferType.EDITABLE);
                    Picasso.with(context).load(image).fit().placeholder(R.mipmap.ic_launcher_round).into(mImageView);

                    int pos = 0;
                    final String categoryOptions[] = new String[]{book.getCategory(), "Horror", "Fiction", "Non-fiction", "History", "Auto-Biography", "Biography"};
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewBook.this, android.R.layout.simple_spinner_dropdown_item, categoryOptions);
                    spinner.setAdapter(arrayAdapter);
                    spinner.setSelection(pos);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void editBook(){
        bAddToCart.setVisibility(View.INVISIBLE);
        mComment.setVisibility(View.INVISIBLE);
        bEdit.setVisibility(View.INVISIBLE);
        bSave.setVisibility(View.VISIBLE);
        bMinusQuantity.setVisibility(View.INVISIBLE);
        bPlusQuantity.setVisibility(View.INVISIBLE);

        mTitle.setVisibility(View.INVISIBLE);
        mAuthor.setVisibility(View.INVISIBLE);
        mCategory.setVisibility(View.INVISIBLE);
        mPrice.setVisibility(View.INVISIBLE);
        mStock.setVisibility(View.INVISIBLE);

        eTitle.setVisibility(View.VISIBLE);
        eAuthor.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        ePrice.setVisibility(View.VISIBLE);
        eStock.setVisibility(View.VISIBLE);
    }

    public void saveBook(){

        quan = Integer.parseInt(mQuantity.getText().toString());
        if (quan == 1){
            bMinusQuantity.setVisibility(View.INVISIBLE);
            bPlusQuantity.setVisibility(View.VISIBLE);
        } else {
            bMinusQuantity.setVisibility(View.VISIBLE);
            bPlusQuantity.setVisibility(View.VISIBLE);
        }

        eTitle.setVisibility(View.INVISIBLE);
        eAuthor.setVisibility(View.INVISIBLE);
        spinner.setVisibility(View.INVISIBLE);
        ePrice.setVisibility(View.INVISIBLE);
        eStock.setVisibility(View.INVISIBLE);

        mTitle.setVisibility(View.VISIBLE);
        mAuthor.setVisibility(View.VISIBLE);
        mCategory.setVisibility(View.VISIBLE);
        mPrice.setVisibility(View.VISIBLE);
        mStock.setVisibility(View.VISIBLE);

        bAddToCart.setVisibility(View.VISIBLE);
        mComment.setVisibility(View.VISIBLE);
        bSave.setVisibility(View.INVISIBLE);
        bEdit.setVisibility(View.VISIBLE);
    }

    public void displayComments(Map<String, Object> bookComments){

        final ArrayList<String> usernames = new ArrayList<>();
        final ArrayList<String> comments = new ArrayList<>();
        final ArrayList<String> ratings = new ArrayList<>();
        int currentItem = 0;
        final ArrayList<Map<String, String>> data2 = new ArrayList<Map<String, String>>();

            for(Map.Entry<String, Object> entry : bookComments.entrySet()){

                Map singleUser = (Map) entry.getValue();
                usernames.add((String) singleUser.get("userName"));

                Map singleComment = (Map) entry.getValue();
                comments.add((String) singleComment.get("comment"));

                Map singleRating = (Map) entry.getValue();
                ratings.add((String) singleRating.get("rating"));

                Map<String, String> data = new HashMap<String, String>(3);
                data.put("username", usernames.get(currentItem));
                data.put("comment", comments.get(currentItem));
                data.put("rating", ratings.get(currentItem));
                currentItem++;
                data2.add(data);
            }
        String[] from = {"username", "comment", "rating"};
        int[] to = {R.id.usernameItem, R.id.commentItem, R.id.avgRatingBarComment};
        CommentAdapter commentAdapter = new CommentAdapter(ViewBook.this, data2, R.layout.comment_list_view, from, to);
        mCommentLV.setAdapter(commentAdapter);
    }
}
