package com.example.root.authex;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SubscriptionDetails extends AppCompatActivity {

    private TextView subscription,email;
    private String details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_details);

        subscription = (TextView) findViewById(R.id.subscription);
        email = (TextView) findViewById(R.id.email);

        GetInfo getInfo = new GetInfo();
        getInfo.method = "getSubscriptionCard";
        getInfo.parameters = new Constants().Address;
        getInfo.execute();

    }

    class GetInfo extends AsyncTask<String, Void, String> {

        String method,parameters;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {

            ContractApi contractApi = new ContractApi("cards",method,parameters);
            details = contractApi.doInBackground();
            System.out.println(details);
            return details;
        }

        @Override
        protected void onPostExecute(String s) {

            JSONObject jsonObject = null;
            JSONArray jArray;
            try {
                jsonObject = new JSONObject(details);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {

                jArray = jsonObject.getJSONArray("result");

                email.setText(jArray.getString(3));
                subscription.setText(jArray.getString(5));
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
}
