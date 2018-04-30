package com.example.root.authex;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShowCardDetails extends AppCompatActivity {

    private TextView email,mno,address,adharnumber,adhaar_title;
    private  String cardname,details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_card_details);

        email = (TextView) findViewById(R.id.email);
        mno = (TextView) findViewById(R.id.mno);
        address = (TextView) findViewById(R.id.address);
        adharnumber = (TextView) findViewById(R.id.adhaarnumber);
        adhaar_title = (TextView) findViewById(R.id.adhaar_title);

        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if(bundle!=null){
            cardname = bundle.getString("cardname");
            if(cardname.equals("ID Card")){
                GetCards getCards1 = new GetCards();
                getCards1.name = "id";
                getCards1.execute();
            }else if(cardname.equals("Simple Card")){
                adharnumber.setVisibility(View.INVISIBLE);
                adhaar_title.setVisibility(View.INVISIBLE);
                GetCards getCards2 = new GetCards();
                getCards2.name = "simple";
                getCards2.execute();
            }
        }


    }

    class GetCards extends AsyncTask<String, Void, String> {

        String name;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            ContractApi contractApi = new ContractApi("cards","getCard","0x30f28686aef33adbfbc13797b1d9f5a2f2759f56/"+name);
            details = contractApi.doInBackground();
            System.out.println("DETAILSXXX: "+details);

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(details);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                if(jsonArray.getString(0)!=null){
                    if(name.equals("id")){
                        email.setText(jsonArray.getString(1));
                        mno.setText(jsonArray.getString(3));
                        adharnumber.setText(jsonArray.getString(4));
                        address.setText(jsonArray.getString(2));
                    }
                    else if(name.equals("simple")){
                        email.setText(jsonArray.getString(1));
                        mno.setText(jsonArray.getString(3));
                        address.setText(jsonArray.getString(2));

                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
