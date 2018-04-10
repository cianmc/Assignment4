package com.example.cianm.bookstore.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.cianm.bookstore.R;
import com.example.cianm.bookstore.activity.RecyclerViewClickListener;
import com.example.cianm.bookstore.entity.Book;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by cianm on 10/04/2018.
 */

public class AllBooksAdapter extends ArrayAdapter<Book> {

    Context context;
    ArrayList<Book> books;
    int pos;

    private static class ViewHolder {
        TextView titleTV;
        TextView authorTV;
        TextView categoryTV;
        TextView priceTV;
        ImageView imageView;
    }

    public AllBooksAdapter(ArrayList<Book> books, Context context) {
        super(context, R.layout.search_list_items, books);
        this.books = books;
        this.context = context;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        //View view = super.getView(position, convertView, parent);
        ViewHolder viewHolder;

        viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.search_list_items, parent, false);
        viewHolder.titleTV = (TextView) convertView.findViewById(R.id.titleSearchItem);
        viewHolder.authorTV = (TextView) convertView.findViewById(R.id.authorSearchItem);
        viewHolder.categoryTV = (TextView) convertView.findViewById(R.id.categorySearchItem);
        viewHolder.priceTV = (TextView) convertView.findViewById(R.id.priceSearchItem);
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.bookImage);

        viewHolder.titleTV.setText(books.get(position).getTitle());
        viewHolder.authorTV.setText(books.get(position).getAuthor());
        viewHolder.categoryTV.setText(books.get(position).getCategory());
        viewHolder.priceTV.setText("€ " + books.get(position).getPrice().toString());

        Picasso.with(context).load(books.get(position).getImage()).fit().placeholder(R.mipmap.ic_launcher_round).into(viewHolder.imageView);

        return convertView;
    }
}
