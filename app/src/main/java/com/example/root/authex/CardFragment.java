package com.example.root.authex;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CardFragment newInstance(String param1, String param2) {
        CardFragment fragment = new CardFragment();
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

    private RecyclerView recyclerView;
    private CardsAdapter cardsAdapter;
    private List<Cards> cardsList;


    private TextView t3,t1;
    private LinearLayout l2;
    private String action,Cardname,email,mno,address,price,socketindex,transactiontype,subscription,details;

    private Bundle bundle;
    private String account_address = new Constants().Address;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_card, container, false);

        t3 = (TextView) view.findViewById(R.id.t3);
        t1 = (TextView) view.findViewById(R.id.t1);

        l2 = (LinearLayout) view.findViewById(R.id.l2);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        cardsList = new ArrayList<>();


        l2.setVisibility(View.INVISIBLE);

        t3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),ChooseCard.class);
                startActivity(i);
            }
        });

        GetCards getCards1 = new GetCards();
        getCards1.cardType = "id";
        getCards1.execute();

        GetCards getCards2 = new GetCards();
        getCards2.cardType = "simple";
        getCards2.execute();


        bundle = AddnScanCards.i.getExtras();
        if(bundle != null){
            try {
                action = "transaction";
                transactiontype = bundle.getString("transactiontype");
                socketindex = bundle.getString("socketindex");

                if(transactiontype.equals("payment")){
                    price = bundle.getString("price");
                }
                else if(transactiontype.equals("subscription")){
                    subscription = bundle.getString("subscription");
                }

                if(bundle.getString("title") != null){
                    t1.setText(bundle.getString("title"));
                }
                l2.setVisibility(View.VISIBLE);
            }catch (Exception e){
                action = "view";
                transactiontype = "";
            }


        }else {
            action = "view";
        }

        cardsAdapter = new CardsAdapter(getContext(), cardsList,subscription,transactiontype, price,action, socketindex);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(cardsAdapter);


        return view;
    }





    class GetCards extends AsyncTask<String, Void, String> {

        String cardType;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... strings) {
            ContractApi contractApi = new ContractApi("cards","getCard","0x30f28686aef33adbfbc13797b1d9f5a2f2759f56/"+cardType);
            details = contractApi.doInBackground();


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {

                //Check for the internet connection

                JSONObject jsonObject = null;
                try{
                    jsonObject = new JSONObject(details);
                }catch (NullPointerException e){
                    Toast.makeText(getContext(),"Please turn on your Internet connection",Toast.LENGTH_LONG).show();
                    return;
                }

                JSONArray jsonArray = jsonObject.getJSONArray("result");
                if(jsonArray.getString(0)!=null){
                    if(cardType.equals("id")){
                        Cards cards1 = new Cards(R.drawable.card,"","","ID Card","identity");
                        cardsList.add(cards1);
                        cardsAdapter.notifyDataSetChanged();
                    }
                    else if(cardType.equals("simple")){
                        Cards cards1 = new Cards(R.drawable.card,"","","Simple Card","identity");
                        cardsList.add(cards1);
                        cardsAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
