package com.example.casquenet.main_screen_fragments.settings_frags;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.casquenet.R;
import com.example.casquenet.main_screen_fragments.Settings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Santee extends Fragment {
    private TextView heartRateTextView;

    ImageView butRetour;


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.santee, container, false);

        heartRateTextView = view.findViewById(R.id.heartRateTextView);


        //retour button
        butRetour = view.findViewById(R.id.butRetour);



        // Listen for changes in "rythme_cardiaque" field
        FirebaseDatabase.getInstance().getReference("rythme_cardiaque").addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int heartRate = dataSnapshot.getValue(Integer.class);
                    heartRateTextView.setText("Rythme cardiaque : " + String.valueOf(heartRate) + " bpm");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });








        butRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backFunction();

            }
        });

        return view;


    } ////












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



}    /////
