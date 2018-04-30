package com.example.root.authex;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

public class AddnScanCards extends AppCompatActivity implements CardFragment.OnFragmentInteractionListener,
        ScanFragment.OnFragmentInteractionListener,SubscriptionFragment.OnFragmentInteractionListener{

    private FrameLayout frameLayout;
    private CardFragment cardFragment;
    private ScanFragment scanFragment;
    private SubscriptionFragment subscriptionFragment;
    public static Intent i ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addn_scan_cards);

        i = getIntent();

        frameLayout = (FrameLayout) findViewById(R.id.framelayout);
        cardFragment = new CardFragment();
        scanFragment = new ScanFragment();
        subscriptionFragment = new SubscriptionFragment();

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,cardFragment);
        fragmentTransaction.commit();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        setFragment(cardFragment);
                        return true;
                    case R.id.navigation_dashboard:
                        setFragment(scanFragment);
                        return true;
                    case R.id.navigation_notifications:
                        setFragment(subscriptionFragment);
                        return true;
                }
                return false;
            }

            public void setFragment(Fragment fragment) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.framelayout,fragment);
                fragmentTransaction.commit();

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {


    }
}
