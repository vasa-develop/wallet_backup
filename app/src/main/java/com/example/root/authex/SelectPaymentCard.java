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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import jnr.ffi.annotations.In;

public class SelectPaymentCard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SubscriptionAdapter subscriptionAdapter;
    private List<Subscription> subscriptionList;
    private String price,socketindex,balance,subscription,transactiontype,email;
    private TextView leftBalance,bill;
    private Button pay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment_card);

        leftBalance = (TextView) findViewById(R.id.balance);
        bill = (TextView) findViewById(R.id.bill);
        pay = (Button) findViewById(R.id.pay);

        Intent i = getIntent();
        final Bundle bundle = i.getExtras();

        if(bundle!=null){
            transactiontype = bundle.getString("transactiontype");
            socketindex = bundle.getString("socketindex");
            if(transactiontype.equals("payment")){
                price = bundle.getString("price");
                bill.setText("Amount to Pay: "+price);
                pay.setText("Pay");
            }else if(transactiontype.equals("subscription")){
                subscription = bundle.getString("subscription");
                email = bundle.getString("email");
                if(subscription.equals("1")){
                    bill.setText("$8/month for 6 months");
                }else if(subscription.equals("2")){
                    bill.setText("$10/month for 3 months");
                }

                pay.setText("Subscribe");
            }

            System.out.println("socketindex: "+socketindex);
            System.out.println("transactiontype: "+transactiontype);
        }




        new UpdateBalance().execute();




        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(transactiontype.equals("payment")){
                    ContractApi contractApi = new ContractApi("coin","transfer","0x30f28686aef33adbfbc13797b1d9f5a2f2759f56/"+price);
                    contractApi.execute();

                    Toast.makeText(getApplicationContext(),"Transaction in process",Toast.LENGTH_SHORT).show();

                    Intent i =new Intent(SelectPaymentCard.this,ThankYou.class);

                    i.putExtra("socketindex",socketindex);

                    startActivity(i);
                }else if(transactiontype.equals("subscription")){
                    SendPostRequest sendPostRequest = new SendPostRequest();
                    sendPostRequest.execute();
                }


            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        subscriptionList = new ArrayList<>();

        subscriptionAdapter = new SubscriptionAdapter(SelectPaymentCard.this, subscriptionList,"pay");
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectPaymentCard.this));

        recyclerView.setAdapter(subscriptionAdapter);


        Subscription subscription = new Subscription(R.drawable.subscription,"","","");
        subscriptionList.add(subscription);
        subscriptionAdapter.notifyDataSetChanged();



    }



    public class SendPostRequest extends AsyncTask<String, Void, String> {

        public String method,cardtype;


        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {
                //"http://"+new Constants().ServerIP+":"+3800+"/AddSubscriptionCard"
                //address userAddress, string cardType, string name, string email, string addr, string mno, string adhaarnumber
                URL url = new URL("http://"+new Constants().ServerIP+":3800/AddSubscriptionCard"); // here is your URL path


                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userAddress", new Constants().Address);
                postDataParams.put("username", "");
                postDataParams.put("firstname", "");
                postDataParams.put("lastname", "");
                postDataParams.put("email", email);
                postDataParams.put("password", "");
                if(subscription.equals("1")){
                    postDataParams.put("subscription", "$8/month for 6 months");
                }else if(subscription.equals("2")){
                    postDataParams.put("subscription", "$10/month for 3 months");
                }
                


                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(
                            new InputStreamReader(
                                    conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("POST request: "+result);
            Toast.makeText(getApplicationContext(),"Subscription in process",Toast.LENGTH_SHORT).show();

            Intent i =new Intent(SelectPaymentCard.this,ThankYou.class);

            i.putExtra("socketindex",socketindex);

            startActivity(i);
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
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
