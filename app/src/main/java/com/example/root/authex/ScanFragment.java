package com.example.root.authex;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanFragment newInstance(String param1, String param2) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView cameraView;
    private TextView barcodeValue;

    private boolean lock = true;

    private String type,WebSocketIP,Certificate,useraddr,parameters,Data;
    private int WebSocketPORT;
    private int socketindex;
    private String method,OrgInfo;
    private TextView title;
    private String GetOrgInfo = "GetOrgInfo";
    private String GetOrgPermissions = "GetOrgInfo2";
    private Bundle bundle;
    private String info [];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scan, container, false);


        bundle = AddnScanCards.i.getExtras();




        barcodeValue = (TextView) view.findViewById(R.id.barcode_value);
        cameraView = (SurfaceView) view.findViewById(R.id.surface_view);

        barcodeDetector = new BarcodeDetector.Builder(getContext())
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(getContext(), barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    //noinspection MissingPermission
                    if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
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




        return view;
    }


    class Decoder extends AsyncTask<String, Void, Boolean> {
        String hex;
        String response = "";

        @Override
        protected Boolean doInBackground(String... strings) {
            //admin_panel/authprotocol/decoder.php
            URL url = null;
            try {
                url = new URL("http://18.205.129.69:3300/decrypt/"+hex);
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
            info = new String[6];
            StringTokenizer st = new StringTokenizer(response,"|");
            int count = 0;
            while (st.hasMoreTokens()) {
                info[count] = st.nextToken();
                count += 1;
            }







            System.out.println("type"+info[0]+"socketindex: "+info[1]+" WebSocketIP: "+info[2]+" WebSocketPORT: "+info[3]+" orgaddress: "+ info[4]+" Price: "+info[5]);
            type = info[0];
            socketindex = Integer.parseInt(info[1]);
            WebSocketIP = info[2];
            WebSocketPORT = Integer.parseInt(info[3]);
            Certificate = info[4];
            Data = info[5];

            if(type.equals("payment")){


                Intent i = new Intent(getContext(),AddnScanCards.class);

                Bundle bundle1 = new Bundle();
                bundle1.putString("title","Choose Your Card");
                bundle1.putString("transactiontype","payment");
                bundle1.putString("price",Data);
                bundle1.putString("socketindex", String.valueOf(socketindex));
                i.putExtras(bundle1);

                startActivityForResult(i,1);


            }else if(type.equals("subscription")){

                Intent i = new Intent(getContext(),AddnScanCards.class);

                Bundle bundle1 = new Bundle();
                bundle1.putString("title","Choose Your Card");
                bundle1.putString("transactiontype","subscription");
                bundle1.putString("subscription",Data);
                bundle1.putString("socketindex", String.valueOf(socketindex));
                i.putExtras(bundle1);

                startActivityForResult(i,1);
            }




        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
