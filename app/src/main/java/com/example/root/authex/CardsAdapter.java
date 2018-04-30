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
    private String subscription;
    public String price,socketindex,action,transactiontype;

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

    public CardsAdapter(Context mContext, List<Cards> cardList,String subscription,String transactiontype, String price,String action, String socketindex) {
        this.mContext = mContext;
        this.cardsList = cardList;
        this.subscription = subscription;
        this.price = price;
        this.action = action;
        this.transactiontype = transactiontype;
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


        if(action.equals("view")){
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(mContext, ShowCardDetails.class);
                    i.putExtra("cardname", cards.getcard_name());
                    mContext.startActivity(i);
                }
            });

        }
        else {
            if (Objects.equals(transactiontype, "payment")) {
                holder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(mContext, YourInformation.class);

                        i.putExtra("price", price);
                        i.putExtra("transactiontype", "payment");
                        i.putExtra("cardname", cards.getcard_name());
                        i.putExtra("socketindex", socketindex);


                        mContext.startActivity(i);
                    }
                });
            }

            else if (Objects.equals(transactiontype, "subscription")) {
                holder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(mContext, YourInformation.class);

                        i.putExtra("subscription", subscription);
                        i.putExtra("cardname", cards.getcard_name());
                        i.putExtra("transactiontype", "subscription");
                        i.putExtra("socketindex", socketindex);

                        mContext.startActivity(i);
                    }
                });
            }
        }






        Glide.with(mContext).load(cards.getImage_id()).into(holder.image);



    }




    @Override
    public int getItemCount() {
        return cardsList.size();
    }


}