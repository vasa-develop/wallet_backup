package com.example.root.authex;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.web3j.abi.datatypes.Int;

import java.util.List;

//public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {
public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.MyViewHolder> {


    private Context mContext;
    private List<Subscription> subscriptionList;
    private String type;
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

    public SubscriptionAdapter(Context mContext, List<Subscription> subscriptionList,String type) {
        this.mContext = mContext;
        this.subscriptionList = subscriptionList;
        this.type = type;
    }

    @Override
    public SubscriptionAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        return new SubscriptionAdapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(SubscriptionAdapter.MyViewHolder holder, int position) {
        final Subscription subscription = subscriptionList.get(position);
        holder.image.setImageResource(R.drawable.subscription);
        if(type.equals("subscription")){
            holder.name.setText("Adult site");
        }else if(type.equals("pay")){
            holder.name.setText("Payment Card");
        }


        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = null;
                if(type.equals("subscription")){
                    i = new Intent(mContext, SubscriptionDetails.class);
                }else if(type.equals("pay")){
                    i = new Intent(mContext, CardBalance.class);
                }

                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subscriptionList.size();
    }
}
