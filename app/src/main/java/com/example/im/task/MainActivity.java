package com.example.im.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.text.DateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {
    Databasehelper dbhelp = new Databasehelper(this);
    Contact c=new Contact();
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private Button gmailSignInButton;
    String userName,email,userPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gmailSignInButton = (Button) findViewById(R.id.gmail_signinbutton);
        gmailSignInButton.setOnClickListener(this);
        isNetworkAvailable();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this).addOnConnectionFailedListener(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        c = dbhelp.checkLogin();        //checks if user is already logged in and if is already logged in shows the logout page
        if (c.getApp().equals("Google")) {
            dbhelp.delete();
            signIn();
        }


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) { case R.id.gmail_signinbutton:
        {
            signIn();
            break;
        }
        }
    }




    //google sign in function
    private void signIn() {
        boolean connected = isNetworkAvailable();
        if (connected == true) {
            c.setApp("Google");
            dbhelp.insert(c);
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
        } else {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }
    //sign out  function for google
    void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
//                        updateUI(false);
                    }
                });

    }

    //function to fetch the google log in data(Name , Email and profile) for current profile logged in
    public void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (!result.isSuccess())
        {

        }
        else {
            // Signed in successfully, show authenticated UI.
            mProgressDialog = ProgressDialog.show(this, "","Please Wait...", true);
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "display name: " + acct.getDisplayName());
            userName = acct.getDisplayName();
            email = acct.getEmail();
            if(acct.getPhotoUrl()!=null)
            {
                userPic = acct.getPhotoUrl().toString();
            }
            else
            {
                userPic="Nopic";
            }
            c.setName(userName);
            c.setEmail(email);
            c.setPic(userPic);
            c.setApp("Google");
            c.setDate(DateFormat.getDateTimeInstance().format(new Date()));
            dbhelp.insert(c);
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            Bundle bundle =new Bundle();
            bundle.putString("name",userName);
            bundle.putString("email",email);
            bundle.putString("userPic",userPic);
            intent.putExtras(bundle);
            startActivity(intent);
//            mProgressDialog.dismiss();
            finish();
        }

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    //receives result from the function being called in mainactivity
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }
    boolean connected = false;
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            connected = false;
        } else {
            connected = true;
        }
        return connected;
    }

}
