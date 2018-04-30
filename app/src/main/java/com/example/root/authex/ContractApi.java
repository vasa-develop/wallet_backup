package com.example.root.authex;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Objects;

/**
 * Created by root on 3/31/18.
 */

class ContractApi extends AsyncTask<String, Void, String> {

    public String method,parameters,OutPut,type,port;

    public ContractApi(String type,String method, String parameters){
        this.method = method;
        this.parameters = parameters;
        this.type = type;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... strings) {

        URL url = null;


        try {
            if(Objects.equals(type, "cards")){
                port = "3800";
            }
            else if(Objects.equals(type, "coin")){
                port = "3600";
            }

            url = new URL("http://"+new Constants().ServerIP+":"+port+"/"+method+"/"+parameters);
            //url = new URL("http://162.144.124.122/~lokasotech/stage_v2/app_panel/admin/admin_panel/authprotocol/decoder.php?crypted="+crypted+"&pubkey="+pubkey);
            System.out.println(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        }

        String line;
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            while ((line = br.readLine()) != null) {
                sb.append(line);
                OutPut = sb.toString();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            br.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        System.out.println("OrgInfo: "+OutPut);



        return OutPut;
    }




}
