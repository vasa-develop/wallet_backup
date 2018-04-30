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

import java.util.List;

////public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.MyViewHolder> {
public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.MyViewHolder>{

    private Context mContext;
    private List<Payment> paymentList;

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

    public PaymentAdapter(Context mContext, List<Payment> paymentList) {
        this.mContext = mContext;
        this.paymentList = paymentList;
    }

    @Override
    public PaymentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        return new PaymentAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PaymentAdapter.MyViewHolder holder, int position) {
        final Payment payment = paymentList.get(position);
        holder.image.setImageResource(R.drawable.card);
        holder.name.setText("Authox Paymant Card");

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, CardBalance.class);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
