package com.example.root.authex;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.neovisionaries.ws.client.HostnameUnverifiedException;
import com.neovisionaries.ws.client.OpeningHandshakeException;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ThankYou extends AppCompatActivity {

    private String socketindex,useraddr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you);

        socketindex = getIntent().getStringExtra("socketindex");
        System.out.println("Socketindex: "+socketindex);



        new CreateWebsocket().execute();

    }

    @Override
    protected void onStart() {
        super.onStart();

        new CreateWebsocket().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new CreateWebsocket().execute();
    }

    @SuppressLint("StaticFieldLeak")
    class CreateWebsocket extends AsyncTask<String, Void, Boolean> {


        @Override
        protected Boolean doInBackground(String... strings) {
            WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);

            WebSocket ws = null;
            try {
                ws = factory.createSocket("ws://"+new Constants().ServerIP+":4000");
            } catch (IOException e) {
                e.printStackTrace();
            }


            try
            {
                // Connect to the server and perform an opening handshake.
                // This method blocks until the opening handshake is finished.
                ws.connect();
            }
            catch (OpeningHandshakeException e)
            {
                System.out.println("ERROR: "+e);
                // A violation against the WebSocket protocol was detected
                // during the opening handshake.
            }
            catch (HostnameUnverifiedException e)
            {
                System.out.println("ERROR: "+e);
                // The certificate of the peer does not match the expected hostname.
            }
            catch (WebSocketException e)
            {
                System.out.println("ERROR: "+e);
                // Failed to establish a WebSocket connection.
            }


            final WebSocket finalWs = ws;
            ws.addListener(new WebSocketAdapter(){
                @Override
                public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
                    super.onConnectError(websocket, exception);
                }

                @Override
                public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
                    super.onError(websocket, cause);
                }

                @Override
                public void onMessageError(WebSocket websocket, WebSocketException cause, List<WebSocketFrame> frames) throws Exception {
                    super.onMessageError(websocket, cause, frames);
                }

                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    super.onConnected(websocket, headers);
                    System.out.println("Connected!!");
                }

                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    System.out.println("server_msg: "+text);

                    //Sending some message back to the server.

                    String message;
                    JSONObject json = new JSONObject();

                    try {
                        useraddr = new Constants().Address;
                        System.out.println("index: "+socketindex+" useraddr: "+useraddr);
                        json.put("type","info");
                        json.put("index", socketindex);
                        json.put("useraddr", useraddr);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    message = json.toString();
                    System.out.println(message);
                    finalWs.sendText(message);
                }
            });

            return true;
        }


    }

    @Override
    public void onBackPressed() {


    }
}
