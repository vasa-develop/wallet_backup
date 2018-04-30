package com.example.root.authex;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jnr.ffi.annotations.In;

public class YourInformation extends AppCompatActivity {

    private EditText mno,email,address,adhaarnumber;
    private String OutPut,price,socketindex,transactiontype,cardname,subscription,subscription_email;
    private Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_information);

        mno = (EditText) findViewById(R.id.mno);
        email = (EditText) findViewById(R.id.email);
        address = (EditText) findViewById(R.id.address);
        adhaarnumber = (EditText) findViewById(R.id.adhaarnumber);
        next = (Button) findViewById(R.id.next);

        Intent i = getIntent();
        final Bundle bundle = i.getExtras();
        if(bundle != null){
            transactiontype = bundle.getString("transactiontype");
            cardname = bundle.getString("cardname");
            if(transactiontype.equals("payment")){

                price = bundle.getString("price");
                System.out.println("PRICE: "+price);
                if(cardname.equals("ID Card")){
                    GetInfo getInfo = new GetInfo();
                    getInfo.method = "getCard";
                    getInfo.parameters = "0x30f28686aef33adbfbc13797b1d9f5a2f2759f56/id";
                    getInfo.execute();
                }
                else if(cardname.equals("Simple Card")){
                    adhaarnumber.setVisibility(View.INVISIBLE);
                    GetInfo getInfo = new GetInfo();
                    getInfo.method = "getCard";
                    getInfo.parameters = "0x30f28686aef33adbfbc13797b1d9f5a2f2759f56/simple";
                    getInfo.execute();
                }
            }else if(transactiontype.equals("subscription")){

                subscription = bundle.getString("subscription");
                System.out.println("SUBSCRIPTION: "+subscription);

                if(cardname.equals("ID Card")){
                    GetInfo getInfo = new GetInfo();
                    getInfo.method = "getCard";
                    getInfo.parameters = "0x30f28686aef33adbfbc13797b1d9f5a2f2759f56/id";
                    getInfo.execute();
                }
                else if(cardname.equals("Simple Card")){
                    adhaarnumber.setVisibility(View.INVISIBLE);
                    GetInfo getInfo = new GetInfo();
                    getInfo.method = "getCard";
                    getInfo.parameters = "0x30f28686aef33adbfbc13797b1d9f5a2f2759f56/simple";
                    getInfo.execute();
                }

                mno.setVisibility(View.INVISIBLE);
                adhaarnumber.setVisibility(View.INVISIBLE);
                address.setVisibility(View.INVISIBLE);
            }

            socketindex = bundle.getString("socketindex");

        }



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(socketindex!=null) {
                    Intent i = new Intent(YourInformation.this, SelectPaymentCard.class);

                    Bundle bundle1 = new Bundle();

                    bundle1.putString("transactiontype", transactiontype);
                    if(transactiontype.equals("payment")){
                        bundle1.putString("price", price);
                    }else if(transactiontype.equals("subscription")){
                        bundle1.putString("subscription", subscription);
                        bundle1.putString("email",subscription_email);
                    }
                    bundle1.putString("socketindex", socketindex);
                    i.putExtras(bundle1);

                    startActivityForResult(i, 1);
                }else {
                    Toast.makeText(getApplicationContext(),"Scan the QR code first.",Toast.LENGTH_SHORT).show();
                }
            }
        });





    }

    class GetInfo extends AsyncTask<String, Void, String>{

        String method,parameters;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {

            ContractApi contractApi = new ContractApi("cards",method,parameters);
            OutPut = contractApi.doInBackground();

            return OutPut;
        }

        @Override
        protected void onPostExecute(String s) {

            JSONObject jsonObject = null;
            JSONArray jArray;
            try {
                jsonObject = new JSONObject(OutPut);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

                jArray = jsonObject.getJSONArray("result");


                mno.setText("Mno: "+jArray.getString(3));
                address.setText("Address: "+jArray.getString(2));
                subscription_email = jArray.getString(1);
                email.setText("Email: "+jArray.getString(1));
                adhaarnumber.setText("Adhaar: "+jArray.getString(4));
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

}
