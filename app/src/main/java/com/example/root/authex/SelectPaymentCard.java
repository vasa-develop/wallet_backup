package com.example.root.authex;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jnr.ffi.annotations.In;

public class SelectPaymentCard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardsAdapter cardsAdapter;
    private List<Cards> cardsList;
    private String price,socketindex,balance;
    private TextView leftBalance,bill;
    private Button pay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment_card);

        price = getIntent().getStringExtra("price");
        socketindex = getIntent().getStringExtra("socketindex");
        leftBalance = (TextView) findViewById(R.id.balance);
        bill = (TextView) findViewById(R.id.bill);

        new UpdateBalance().execute();


        pay = (Button) findViewById(R.id.pay);
        bill.setText("Amount to Pay: "+price);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContractApi contractApi = new ContractApi("coin","transfer","0xF9Bf0A5130c48769B3BBcEf507497156B145821E/"+price);
                contractApi.execute();

                Toast.makeText(getApplicationContext(),"Transaction in process",Toast.LENGTH_SHORT).show();

                Intent i =new Intent(SelectPaymentCard.this,ThankYou.class);

                i.putExtra("socketindex",socketindex);

                startActivity(i);

            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        cardsList = new ArrayList<>();
        cardsAdapter = new CardsAdapter(this, cardsList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(cardsAdapter);

        prepareCards();

    }

    public void prepareCards(){
        int[] card_res = {R.drawable.wallet};

        Cards cards1 = new Cards(card_res[0],"Authox Card","","","","","","payment");
        cardsList.add(cards1);




        cardsAdapter.notifyDataSetChanged();
    }

    class UpdateBalance extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            ContractApi contractApi = new ContractApi("coin","balanceOf","0x30F28686AEF33aDbFbC13797b1d9F5a2F2759F56");
            balance = contractApi.doInBackground();
            return balance;
        }

        @Override
        protected void onPostExecute(String s) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(balance);
                balance = jsonObject.getString("result");
                leftBalance.setText("Balance: "+balance+" coins");
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
}
