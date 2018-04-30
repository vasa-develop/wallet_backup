package com.example.root.authex;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

public class CardDetails extends AppCompatActivity {

    private Button submit;
    private EditText email,mno,address,adhaarnumber;
    private String TAG = "Firebase";
    private FirebaseAuth mAuth;
    private String mVerificationId,semail,smno,saddress;
    private TextView verify_email,verify_number;
    private boolean mVerificationInProgress = false;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private AlertDialog.Builder alertDialog;
    private FirebaseUser firebaseUser;
    private static int PER_LOGIN = 1000;
    private boolean verificationEmailSent = false;
    private int card_index;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_details);


        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        email = (EditText) findViewById(R.id.email);
        mno = (EditText) findViewById(R.id.mno);
        address = (EditText) findViewById(R.id.address);
        verify_email = (TextView) findViewById(R.id.verify_email);
        verify_number = (TextView) findViewById(R.id.verify_number);
        adhaarnumber = (EditText) findViewById(R.id.adhaarnumber);

        Intent i = getIntent();
        final Bundle bundle = i.getExtras();
        if(bundle != null){
            card_index = bundle.getInt("card_name");
            if(card_index==0){
                adhaarnumber.setVisibility(View.INVISIBLE);
            }
            System.out.println(bundle.getInt("card_name"));
        }


        verify_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("code sending started!");
                verify_number.setText("verifying number...");
                try {
                    startPhoneNumberVerification(mno.getText().toString());
                }catch (java.lang.IllegalArgumentException e){
                    Toast.makeText(getApplicationContext(),"Please fill the number field.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        verify_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verify_email.setText("starting verification...");
                //Toast.makeText(getApplicationContext(),"Email verification status: "+String.valueOf(firebaseUser.isEmailVerified()),Toast.LENGTH_SHORT).show();

                try {
                    /*ActionCodeSettings actionCodeSettings =
                            ActionCodeSettings.newBuilder()
                                    // URL you want to redirect back to. The domain (www.example.com) for this
                                    // URL must be whitelisted in the Firebase Console.
                                    // This must be true
                                    .setUrl("http://towardsblockchain.com")
                                    .setHandleCodeInApp(true)
                                    .setAndroidPackageName(
                                            "com.example.root.authex",
                                            true, *//* installIfNotAvailable *//*
                                            "12"    *//* minimumVersion *//*)
                                    .build();*/

                    if(!verificationEmailSent) {
                        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                                .setAllowNewEmailAccounts(true).build(), PER_LOGIN);
                    }
                    else {

                        mAuth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(firebaseUser.isEmailVerified()){
                                    verify_email.setText("email verified!");
                                    verify_email.setTextColor(Color.parseColor("#00ff00"));
                                    verify_email.setEnabled(false);
                                }
                                else {
                                    verify_email.setText("email not verified! Check after few seconds");
                                }

                            }
                        });


                    }

                }
                catch (java.lang.IllegalArgumentException e){
                    Toast.makeText(getApplicationContext(),"Please fill the email field.",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    System.out.println("Email sending error: "+e);
                }
            }
        });




        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                semail = email.getText().toString();
                smno = mno.getText().toString();
                saddress = address.getText().toString();


                if(card_index==0){
                    SendPostRequest sendPostRequest = new SendPostRequest();
                    sendPostRequest.method = "AddCard";
                    sendPostRequest.cardtype = "simple";
                    sendPostRequest.execute();
                }
                else if(card_index==1){
                    SendPostRequest sendPostRequest = new SendPostRequest();
                    sendPostRequest.method = "AddCard";
                    sendPostRequest.cardtype = "id";
                    sendPostRequest.execute();
                }




                Intent i = new Intent(CardDetails.this,AddnScanCards.class);

                Bundle bundle1 = new Bundle();
                bundle1.putInt("card_name",bundle.getInt("card_name"));
                bundle1.putString("email",email.getText().toString());
                bundle1.putString("mno",mno.getText().toString());
                bundle1.putString("address",address.getText().toString());

                i.putExtras(bundle1);

                startActivityForResult(i,1);
            }
        });




        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(getApplicationContext(),"Invalid number",Toast.LENGTH_SHORT).show();
                    verify_number.setText("verify number");
                    // Invalid request
                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(getApplicationContext(),"SMS quota exceeded!",Toast.LENGTH_SHORT).show();
                    // The SMS quota for the project has been exceeded
                    // ...
                }

                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                showAlertBox("Mobile verification","enter OTP");

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;


            }

            private void showAlertBox(String title,String message) {
                System.out.println("ALERT BOX!!");
                alertDialog = new AlertDialog.Builder(CardDetails.this);
                alertDialog.setTitle("PASSWORD");
                alertDialog.setMessage("Enter Password");

                final EditText input = new EditText(CardDetails.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);
                alertDialog.setIcon(R.drawable.wallet);

                alertDialog.setPositiveButton("verify",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, input.getText().toString());
                                signInWithPhoneAuthCredential(credential);
                            }
                        });

                alertDialog.setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                verify_number.setText("verify number");
                            }
                        });

                alertDialog.show();
            }
        };



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PER_LOGIN){
            handleSignInResponse(resultCode,data);
        }
    }

    private void handleSignInResponse(int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            Toast.makeText(getApplicationContext(),"OK",Toast.LENGTH_SHORT).show();
            //System.out.println("User Email: "+firebaseUser.getEmail());
            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        verificationEmailSent = true;
                        verify_email.setText("mail sent to "+firebaseUser.getEmail()+". Click to check verification status.");
                    }
                    else {
                        verify_email.setText("Error while sending verification mail!");
                    }
                }
            });
        }
    }

    class ContractApi extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
    }







    public class SendPostRequest extends AsyncTask<String, Void, String> {

        public String method,cardtype;


        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try {
                //address userAddress, string cardType, string name, string email, string addr, string mno, string adhaarnumber
                URL url = new URL("http://"+new Constants().ServerIP+":3800/"+method); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("userAddress",new Constants().Address);
                postDataParams.put("cardType", cardtype);
                postDataParams.put("name","");
                postDataParams.put("email",email.getText().toString());
                postDataParams.put("addr",address.getText().toString());
                postDataParams.put("mno",mno.getText().toString());
                if(cardtype.equals("simple")){
                    postDataParams.put("adhaarnumber","");
                }
                else if(cardtype.equals("id")){
                    postDataParams.put("adhaarnumber",adhaarnumber.getText().toString());
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



    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]

        System.out.println("code sent!");

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            try {
                                alertDialog.setView(null);
                                verify_number.setText("number verified");
                                verify_number.setTextColor(Color.parseColor("#00ff00"));
                                verify_number.setEnabled(false);
                            }
                            catch (NullPointerException e){
                                verify_number.setText("number verified");
                                verify_number.setTextColor(Color.parseColor("#00ff00"));
                                verify_number.setEnabled(false);
                            }
                            FirebaseUser user = task.getResult().getUser();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


}
