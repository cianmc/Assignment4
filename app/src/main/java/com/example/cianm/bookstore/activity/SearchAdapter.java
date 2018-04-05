package com.example.cianm.bookstore.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.cianm.bookstore.R;

import java.util.ArrayList;

/**
 * Created by cianm on 28/03/2018.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    Context context;
    private static RecyclerViewClickListener itemListener;
    ArrayList<String> idList;
    ArrayList<String> titleList;
    ArrayList<String> authorList;
    ArrayList<String> categoryList;
    ArrayList<String> priceList;
    ArrayList<String> quantityList;
    ArrayList<String> bookImageList;

    public SearchAdapter(Context context, ArrayList<String> idList, ArrayList<String> titleList, ArrayList<String> authorList, ArrayList<String> categoryList, ArrayList<String> priceList, ArrayList<String> quantityList, RecyclerViewClickListener itemListener) {
        this.context = context;
        this.itemListener = itemListener;
        this.idList = idList;
        this.titleList = titleList;
        this.authorList = authorList;
        this.categoryList = categoryList;
        this.priceList = priceList;
        this.quantityList = quantityList;
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //ImageView bookImage;
        TextView mTitle, mAuthor, mCategory, mPrice;


        public SearchViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            //bookImage = (ImageView) itemView.findViewById(R.id.bookImage);
            mTitle = (TextView) itemView.findViewById(R.id.titleSearchItem);
            mAuthor = (TextView) itemView.findViewById(R.id.authorSearchItem);
            mPrice = (TextView) itemView.findViewById(R.id.priceSearchItem);
            mCategory = (TextView) itemView.findViewById(R.id.categorySearchItem);

        }

        @Override
        public void onClick(View v){
            itemListener.recyclerViewLisClicked(v, this.getLayoutPosition());
        }
    }

    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_list_items, parent, false);
        return new SearchAdapter.SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        holder.mTitle.setText(titleList.get(position));
        holder.mAuthor.setText(authorList.get(position));
        holder.mPrice.setText("€" + priceList.get(position));
        holder.mCategory.setText(categoryList.get(position));

        //Glide.with(context).load(bookImageList.get(position)).asBitmap().placeholder(R.mipmap.ic_launcher_round).into(holder.bookImage);
    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

}
