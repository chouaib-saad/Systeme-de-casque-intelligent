package com.example.casquenet;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;

public class SigninSignup extends AppCompatActivity {


    LottieAnimationView animationView;

    private FirebaseAuth mAuth;


    private Button signupBtn, loginBtn;

    ProgressDialog progressDialog;

    @Override
    public void onBackPressed() {
        showExitDialog();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_signup);

        fadeInLogo();

        signupBtn = findViewById(R.id.log_in);
        loginBtn = findViewById(R.id.signin);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();

        rememberMe();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(SigninSignup.this, RegisterActivity.class));
                startActivityWithAnimation(SigninSignup.this,new RegisterActivity());

            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(SigninSignup.this, LoginActivity.class));
                startActivityWithAnimation(SigninSignup.this,new LoginActivity());
            }
        });









    }       ///////





    private void fadeInLogo(){
        animationView = findViewById(R.id.login_signup_logo);
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
        fadeInAnimation.setDuration(2000); // Adjust the duration as needed
    }










    private void startActivityWithAnimation(Activity currentActivity, Activity newActivityClass){

        // Start the new activity with a fade animation
        Intent intent = new Intent(currentActivity, newActivityClass.getClass());
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // Finish the current activity
        //currentActivity.finish();
    }



    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quitter casqueNet");
        builder.setMessage("Voulez-vous vraiment quitter l'application ?");
        builder.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Perform the action to exit the app
                finishAffinity();
            }
        });
        builder.setNegativeButton("Non", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void rememberMe(){

        SharedPreferences preferences=getSharedPreferences("checkBox",MODE_PRIVATE);
        String checkBox=preferences.getString("remember","");

        showProgressDialog(this,"chargement..",true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(checkBox.equals("true")){

                    showProgressDialog(getApplicationContext(),"chargement..",false);
                    checkUserLoggedIn();

                }else{
                    showProgressDialog(getApplicationContext(),"chargement..",false);
                }

            }
        },500);


    }




    private void checkUserLoggedIn() {
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    // User is logged in, redirect to the main activity
                    startActivity(new Intent(getApplicationContext(), MainScreen .class));
                }
            }
        });
    }



    //progressbar
    private void showProgressDialog(Context context, String message, boolean show) {
        if (show) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } else {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }


}   //////
