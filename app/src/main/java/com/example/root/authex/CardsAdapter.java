package com.example.root.authex;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Objects;

/**
 * Created by root on 3/19/18.
 */

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Cards> cardsList;
    private String email,mno,address;
    public String price,socketindex;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name,cardtype;
        public LinearLayout card;



        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            name = (TextView) view.findViewById(R.id.name);
            cardtype = (TextView) view.findViewById(R.id.type);
            card = (LinearLayout) view.findViewById(R.id.card);
        }
    }


    public CardsAdapter(Context mContext, List<Cards> cardList) {
        this.mContext = mContext;
        this.cardsList = cardList;
    }

    public CardsAdapter(Context mContext, List<Cards> cardList,String email,String mno,String address, String price, String socketindex) {
        this.mContext = mContext;
        this.cardsList = cardList;
        this.email = email;
        this.mno = mno;
        this.address = address;
        this.price = price;
        this.socketindex = socketindex;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Cards cards = cardsList.get(position);
        holder.image.setImageResource(R.drawable.male);
        holder.name.setText(cards.getcard_name());



        if(Objects.equals(cards.getCardtype(), "identity")){
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext,YourInformation.class);

                    i.putExtra("price",price);
                    i.putExtra("socketindex",socketindex);
                    System.out.println("socket: "+socketindex);

                    mContext.startActivity(i);
                }
            });
        }
        if(Objects.equals(cards.getCardtype(), "payment")){
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext,CardBalance.class);

                    mContext.startActivity(i);
                }
            });
        }




        Glide.with(mContext).load(cards.getImage_id()).into(holder.image);



    }




    @Override
    public int getItemCount() {
        return cardsList.size();
    }


}