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

    private EditText mno,email,address;
    private String OutPut,price,socketindex;
    private Button next;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_information);

        Intent i = getIntent();
        final Bundle bundle = i.getExtras();
        if(bundle != null){
            price = bundle.getString("price");
            socketindex = bundle.getString("socketindex");
            System.out.println("PRICE: "+price);
        }

        mno = (EditText) findViewById(R.id.mno);
        email = (EditText) findViewById(R.id.email);
        address = (EditText) findViewById(R.id.address);

        next = (Button) findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(price!=null) {
                    Intent i = new Intent(YourInformation.this, SelectPaymentCard.class);

                    Bundle bundle1 = new Bundle();
                    bundle1.putString("price", price);
                    bundle1.putString("socketindex", socketindex);
                    i.putExtras(bundle1);

                    startActivityForResult(i, 1);
                }else {
                    Toast.makeText(getApplicationContext(),"Scan the QR code first.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        new GetInfo().execute();



    }

    class GetInfo extends AsyncTask<String, Void, String>{
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {

            ContractApi contractApi = new ContractApi("kyc","GetOrgInfo","0x26aA62a120Bc9183290639A0980Ce88c51f0Ba2a");
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
                email.setText("Email: "+jArray.getString(0));
                mno.setText("Mno: "+jArray.getString(1));
                address.setText("Address: "+jArray.getString(2));
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

}
