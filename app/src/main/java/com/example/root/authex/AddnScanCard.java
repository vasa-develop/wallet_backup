package com.example.root.authex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jnr.ffi.annotations.In;

public class AddnScanCard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CardsAdapter cardsAdapter;
    private List<Cards> cardsList;

    private Button scan;

    private TextView t3,t1;
    private LinearLayout l2;
    private String Cardname,email,mno,address,price,socketindex;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addn_scan_card);

        t3 = (TextView) findViewById(R.id.t3);
        t1 = (TextView) findViewById(R.id.t1);

        l2 = (LinearLayout) findViewById(R.id.l2);

        scan = (Button) findViewById(R.id.scan);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        cardsList = new ArrayList<>();


        l2.setVisibility(View.INVISIBLE);


        Intent i = getIntent();
        bundle = i.getExtras();
        if(bundle != null){
            getCard(bundle.getInt("card_name"));
            email = bundle.getString("email");
            mno = bundle.getString("mno");
            address = bundle.getString("address");
            price = bundle.getString("price");
            socketindex = bundle.getString("socketindex");
            System.out.println(bundle.getInt("card_name"));
            System.out.println(email);
            System.out.println(mno);
            System.out.println(address);
            if(bundle.getString("title") != null){
                t1.setText(bundle.getString("title"));
            }
            l2.setVisibility(View.VISIBLE);

        }



       /* cardsAdapter = new CardsAdapter(this, cardsList,email, mno, address, price, socketindex);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(cardsAdapter);*/




        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddnScanCard.this, ScanQrCode.class);

                Bundle bundle = new Bundle();
                bundle.putString("card_name",Cardname);
                bundle.putString("email",email);
                bundle.putString("mno",mno);
                bundle.putString("address",address);
                i.putExtras(bundle);

                startActivityForResult(i,1);
            }
        });


        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddnScanCard.this, ChooseCard.class);
                startActivity(i);
            }
        });

        prepareCards();
    }


    public  void getCard(int id){
        switch (id){
            case 0:Cardname = "Simple Card";break;
            case 1:Cardname = "Adhaar Card"; break;
            case 2:Cardname = "Authox Payment";break;
        }
    }

    public void prepareCards(){
        int[] card_res = {R.drawable.card};

        if(bundle != null) {
            System.out.println("yprice: "+price);
            /*Cards cards1 = new Cards(card_res[0], price, socketindex,"identity");
            cardsList.add(cards1);*/

        }
        cardsAdapter.notifyDataSetChanged();
    }
}
