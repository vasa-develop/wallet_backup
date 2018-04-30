package com.example.root.authex;

import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SubscriptionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SubscriptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SubscriptionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public SubscriptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubscriptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubscriptionFragment newInstance(String param1, String param2) {
        SubscriptionFragment fragment = new SubscriptionFragment();
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
    private SubscriptionAdapter subscriptionAdapter;
    private List<Subscription> subscriptionList;
    private String details;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_subscription, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        subscriptionList = new ArrayList<>();

        subscriptionAdapter = new SubscriptionAdapter(getContext(), subscriptionList,"subscription");
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(subscriptionAdapter);


        GetInfo getInfo = new GetInfo();
        getInfo.method = "getSubscriptionCard";
        getInfo.parameters = new Constants().Address;
        getInfo.execute();

        return view;
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
                if(!jArray.getString(5).equals("")){
                    Subscription subscription = new Subscription(R.drawable.subscription,"","","");
                    subscriptionList.add(subscription);
                    subscriptionAdapter.notifyDataSetChanged();
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
