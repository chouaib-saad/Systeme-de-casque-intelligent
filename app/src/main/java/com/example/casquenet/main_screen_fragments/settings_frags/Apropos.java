package com.example.casquenet.main_screen_fragments.settings_frags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.casquenet.R;
import com.example.casquenet.main_screen_fragments.Settings;


public class Apropos extends Fragment {

    ImageView butRetour;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.a_propos, container, false);

        butRetour = v.findViewById(R.id.butRetour);




        butRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                backFunction();

            }
        });






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





}  ////