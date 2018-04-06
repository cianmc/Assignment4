package com.example.cianm.bookstore.activity;

import android.app.Activity;
import android.content.Context;
import android.media.Rating;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.cianm.bookstore.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

import java.util.Map;

/**
 * Created by cianm on 05/04/2018.
 */

public class CommentAdapter extends SimpleAdapter {

    LayoutInflater inflater;
    Context context;
    ArrayList<Map<String, String>> data2;

    public CommentAdapter(Context context, ArrayList<Map<String, String>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.context = context;
        this.data2 = data;
        inflater.from(context);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        View view = super.getView(position, convertView, parent);

        TextView userTV = (TextView) view.findViewById(R.id.usernameItem);
        TextView ratingTV = (TextView) view.findViewById(R.id.avgRatingBarComment);
        TextView commentTV = (TextView) view.findViewById(R.id.commentItem);

        userTV.setText(data2.get(position).get("username"));
        commentTV.setText(data2.get(position).get("comment"));
        ratingTV.setText(data2.get(position).get("rating") + " Stars");

        return view;
    }
}
