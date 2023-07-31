package com.example.casquenet.main_screen_fragments.settings_frags;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.casquenet.R;
import com.example.casquenet.main_screen_fragments.Settings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;


public class Alert extends Fragment {


    private DatabaseReference alertRef;

    Button appelerNumber,jeVaisBien;

    LottieAnimationView lottie_countdown;

    ImageView butRetour;



    ProgressDialog progressDialog;

    LinearLayout container_alert_disabled,container_alert_enabled;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.alert, container, false);


        appelerNumber = v.findViewById(R.id.appelerNumber);
        jeVaisBien = v.findViewById(R.id.jeVaisBien);
        butRetour = v.findViewById(R.id.butRetour);


        lottie_countdown = v.findViewById(R.id.lottie_countdoun);

        //visibility
        container_alert_disabled = v.findViewById(R.id.container_alert_disabled);
        container_alert_enabled = v.findViewById(R.id.container_alert_enabled);



        // Get a reference to the device node in the Firebase database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        alertRef = firebaseDatabase.getReference("accident");


        //listening to the accident reference
        FirebaseDatabase.getInstance().getReference("accident").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if(Objects.equals(dataSnapshot.getValue(), true)){


                        container_alert_enabled.setVisibility(View.VISIBLE);
                        container_alert_disabled.setVisibility(View.INVISIBLE);

                        // Create an AtomicBoolean to track if the animation was canceled
                        AtomicBoolean animationCanceled = new AtomicBoolean(false);


                        lottie_countdown.playAnimation();

                        // Set up Lottie animation listener
                        lottie_countdown.addAnimatorListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {
                                // Animation started...
                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                // Animation ended, show toast


                                if (!animationCanceled.get()) {

                                    sendMessage("26942218", "Sauvez-moi !");
                                    Toast.makeText(getContext(), "message envoyer", Toast.LENGTH_LONG).show();

                                    //Toast.makeText(getContext(), "Countdown animation ended", Toast.LENGTH_SHORT).show();

                                }

                            }

                            @Override
                            public void onAnimationCancel(@NonNull Animator animation) {

                                animationCanceled.set(true);

                                showProgressDialog(getContext(),"Aucun risque",true);




                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        redirectTotheNormalCase();
                                        showProgressDialog(getContext(),"Aucun risque",false);
                                    }
                                },2500);

                            }

                            @Override
                            public void onAnimationRepeat(@NonNull Animator animation) {

                            }


                        });


                    }else{


                        container_alert_enabled.setVisibility(View.INVISIBLE);
                        container_alert_disabled.setVisibility(View.VISIBLE);



                    }
                    
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                Toast.makeText(getContext(), "quelque chose s'est mal passé", Toast.LENGTH_SHORT).show();
            }
        });




        appelerNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                makePhoneCall("198");

            }
        });



        jeVaisBien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                lottie_countdown.cancelAnimation();

            }
        });



        butRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backFunction();
            }
        });


        //back button check
        if (!(requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0)) {
            butRetour.setVisibility(View.INVISIBLE);
        }

        return v;



    }   ////





    private void backFunction(){

        // Create the instance of the fragment you want to open
        Fragment mainFragment = new Settings();

        // Perform the exit transition animation
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.replace(R.id.fragmentContainer, mainFragment); // Replace with the main fragment
        transaction.commit();

        // Delay the onBackPressed() call to allow the animation to finish
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requireActivity().onBackPressed(); // Navigate to the previous fragment or activity
            }
        }, 200); // Adjust the delay time as needed

    }






    public void sendMessage(String phoneNumber, String message) {


        showProgressDialog(getContext(),"envoie de message ne cours..",true);

        //reset the alert
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                showProgressDialog(getContext(),"envoie de message ne cours..",false);


                Uri uri = Uri.parse("smsto:" + phoneNumber);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", message);
                startActivity(intent);



                //reset the alert
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        redirectTotheNormalCase();
                    }
                }, 1500);


            }
            },2000);
    }






    public void makePhoneCall(String phoneNumber) {
        String dialUri = "tel:" + phoneNumber;
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(dialUri));
        startActivity(intent);
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



    private void redirectTotheNormalCase(){


        container_alert_enabled.setVisibility(View.INVISIBLE);
        container_alert_disabled.setVisibility(View.VISIBLE);
        alertRef.setValue(false); // Set the value to true to indicate connection
        showProgressDialog(getContext(),"Aucun risque",false);


    }


}   ////
