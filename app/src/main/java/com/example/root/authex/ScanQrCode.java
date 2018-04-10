package com.example.root.authex;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.neovisionaries.ws.client.HostnameUnverifiedException;
import com.neovisionaries.ws.client.OpeningHandshakeException;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.tx.Contract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class ScanQrCode extends AppCompatActivity {

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private TextView barcodeValue;

    private boolean lock = true;

    private String WebSocketIP,Certificate,useraddr,parameters,Price;
    private int WebSocketPORT;
    private int socketindex;
    private String method,OrgInfo;
    private TextView title;
    private String GetOrgInfo = "GetOrgInfo";
    private String GetOrgPermissions = "GetOrgInfo2";
    private Bundle bundle;
    private String info [];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr_code);

        Intent i = getIntent();
        bundle = i.getExtras();




        barcodeValue = (TextView) findViewById(R.id.barcode_value);
        cameraView = (SurfaceView) findViewById(R.id.surface_view);

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    //noinspection MissingPermission
                    if (ActivityCompat.checkSelfPermission(ScanQrCode.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cameraSource.start(cameraView.getHolder());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    barcodeValue.post(new Runnable() {
                        @Override
                        public void run() {
                            //Update barcode value to TextView
                            barcodeValue.setText(barcodes.valueAt(0).displayValue);
                            if(lock){

                                String data = barcodes.valueAt(0).displayValue;
                                System.out.println();
                                Decoder decoder = new Decoder();
                                decoder.hex = data;
                                decoder.execute();

                                lock = false;
                            }


                        }
                    });
                }
            }
        });
    }

    class Decoder extends AsyncTask<String, Void, Boolean> {
        String hex;
        String response = "";

        @Override
        protected Boolean doInBackground(String... strings) {
            //admin_panel/authprotocol/decoder.php
            URL url = null;
            try {
                url = new URL("http://54.91.189.21:3300/decrypt/"+hex);
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
                    response = sb.toString();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                br.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }


            System.out.println(response);
            return true;

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            info = new String[5];
            StringTokenizer st = new StringTokenizer(response,"|");
            int count = 0;
            while (st.hasMoreTokens()) {
                info[count] = st.nextToken();
                count += 1;
            }

            //CHECK IF THE PUBLIC KEY GIVEN BY THE SERVER IS VALID AND IS REGISTERED AS AN ORGANISATION.


            ContractApi contractApi = new ContractApi("kyc",GetOrgInfo,info[3]);
            contractApi.execute();



            System.out.println("socketindex: "+info[0]+" WebSocketIP: "+info[1]+" WebSocketPORT: "+info[2]+" orgaddress: "+ info[3]+" Price: "+info[4]);
            socketindex = Integer.parseInt(info[0]);
            WebSocketIP = info[1];
            WebSocketPORT = Integer.parseInt(info[2]);
            Certificate = info[3];
            Price = info[4];


            Intent i = new Intent(ScanQrCode.this,AddnScanCard.class);

            Bundle bundle1 = new Bundle();
            bundle1.putString("title","Choose Your Card");
            bundle1.putString("card_name",bundle.getString("card_name"));
            bundle1.putString("email",bundle.getString("email"));
            bundle1.putString("mno",bundle.getString("mno"));
            bundle1.putString("address",bundle.getString("address"));
            bundle1.putString("price",info[4]);
            bundle1.putString("socketindex", String.valueOf(socketindex));
            i.putExtras(bundle1);

            startActivityForResult(i,1);

            //new CreateWebsocket().execute();

        }
    }






}
