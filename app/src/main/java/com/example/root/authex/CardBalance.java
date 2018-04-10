package com.example.root.authex;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class CardBalance extends AppCompatActivity {

    private String balance;
    private TextView balanceView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_balance);

        balanceView = (TextView) findViewById(R.id.balance);


        new UpdateBalance().execute();
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
                balanceView.setText(balance+" AuthCoins");
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
}
