package com.example.cianm.bookstore.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cianm.bookstore.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by cianm on 06/04/2018.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    LayoutInflater inflater;
    Context context;
    ArrayList<String> titles;
    ArrayList<String> quantities;
    ArrayList<String> totals;
    ArrayList<String> images;

    public CartAdapter(Context context, ArrayList<String> titles, ArrayList<String> quantities, ArrayList<String> totals, ArrayList<String> images) {
        this.context = context;
        this.titles = titles;
        this.quantities = quantities;
        this.totals = totals;
        this.images = images;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView cartImage;
        TextView mTitle, mQuantity, mTotal;

        public CartViewHolder(View itemView){
            super(itemView);
            cartImage = (ImageView) itemView.findViewById(R.id.bookImageCart);
            mTitle = (TextView) itemView.findViewById(R.id.titleCartItem);
            mQuantity = (TextView) itemView.findViewById(R.id.quantityCartItem);
            mTotal = (TextView) itemView.findViewById(R.id.totalCartItem);
        }
    }

    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_list_item, parent, false);
        return new CartAdapter.CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        holder.mTitle.setText(titles.get(position));
        holder.mQuantity.setText("Quantity: " + quantities.get(position));
        holder.mTotal.setText( "Total: " + "€ " + totals.get(position));

        Picasso.with(context).load(images.get(position)).fit().placeholder(R.mipmap.ic_launcher_round).into(holder.cartImage);
    }


    @Override
    public int getItemCount() {
        return titles.size();
    }

}
