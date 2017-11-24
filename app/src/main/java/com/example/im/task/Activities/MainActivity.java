package com.example.im.task.Activities;

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

import com.example.im.task.Model.Contact;
import com.example.im.task.Database.Databasehelper;
import com.example.im.task.R;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    //Decalring variables.
    Databasehelper dbhelp = new Databasehelper(this);    //creating Databasehelper obeject
    Contact c = new Contact();
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private Button gmailSignInButton;
    String userName, email, userPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gmailSignInButton = (Button) findViewById(R.id.gmail_signinbutton);
        gmailSignInButton.setOnClickListener(this);
        isNetworkAvailable();

//Configures sign-in to request the user's ID, email address, and basic profile.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
// Builds a GoogleSignInClient with the options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this).addOnConnectionFailedListener(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
//checks if user is already logged in and if is already logged in shows the  HomeActivity.
        c = dbhelp.checkLogin();
        if (c.getApp().equals("Google")) {
            dbhelp.delete();
            signIn();
        }
    }

    //oncClick method to perform action according to thebutton being clicked.
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.gmail_signinbutton: {
                signIn();
                break;
            }
        }
    }

    //google sign in function
    private void signIn() {
        boolean connected = isNetworkAvailable();
        //If network is Available then perform Signin.
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
        if (!result.isSuccess()) {

        } else {
            // Signed in successfully, show authenticated UI.
            mProgressDialog = ProgressDialog.show(this, "", "Please Wait...", true);
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.e(TAG, "display name: " + acct.getDisplayName());
            userName = acct.getDisplayName();
            email = acct.getEmail();
            if (acct.getPhotoUrl() != null) {
                userPic = acct.getPhotoUrl().toString();
            } else {
                userPic = "Nopic";
            }
            //Inserting Values in the Database
            c.setName(userName);
            c.setEmail(email);
            c.setPic(userPic);
            c.setApp("Google");
            c.setDate(DateFormat.getDateTimeInstance().format(new Date()));
            dbhelp.insert(c);
            //Creating intent to HomeActivity.
            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            //Using Bundle to pass data to HomeActivity.
            Bundle bundle = new Bundle();
            bundle.putString("name", userName);
            bundle.putString("email", email);
            bundle.putString("userPic", userPic);
            intent.putExtras(bundle);
            //Starting HomeActivity.
            startActivity(intent);
            finish();
        }

    }

    //In case Connnection is Failed.
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //Receives result from the function being called in MainActivity.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    //Funtion to check Network Availability.
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
