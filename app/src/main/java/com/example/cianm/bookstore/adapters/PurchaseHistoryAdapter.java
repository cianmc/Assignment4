package com.example.cianm.bookstore.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cianm.bookstore.R;
import com.example.cianm.bookstore.activity.AdminHome;
import com.example.cianm.bookstore.activity.PurchaseHistory;
import com.example.cianm.bookstore.activity.ViewBook;
import com.example.cianm.bookstore.entity.GlobalVariables;
import com.example.cianm.bookstore.entity.Order;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by cianm on 11/04/2018.
 */

public class PurchaseHistoryAdapter extends ArrayAdapter<Order>{

    Context context;
    ArrayList<Order> orders;

    private static class ViewHolder{
        TextView titleView;
        TextView quantityView;
        ImageView imageView;
    }

    public PurchaseHistoryAdapter(ArrayList<Order> orders, Context context){
        super(context, R.layout.purchase_history_item, orders);
        this.orders = orders;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){

        ViewHolder viewHolder;
        viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.purchase_history_item, parent, false);
        viewHolder.titleView = (TextView) convertView.findViewById(R.id.titlePH);
        viewHolder.quantityView = (TextView) convertView.findViewById(R.id.quantityPH);
        viewHolder.imageView = (ImageView) convertView.findViewById(R.id.bookImagePH);

        viewHolder.titleView.setText(orders.get(position).getTitle());
        viewHolder.quantityView.setText("Quantity: " + orders.get(position).getQuantity().toString());
        Picasso.with(context).load(orders.get(position).getImage()).fit().placeholder(R.mipmap.ic_launcher_round).into(viewHolder.imageView);

        return convertView;
    }

}
