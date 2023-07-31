package com.example.casquenet.main_screen_fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.casquenet.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Appareil extends Fragment {

    private DatabaseReference deviceReference;
    private Button connectButton;
    private Button disconnectButton;

    ImageView casque_connected;


    @SuppressLint("NonConstantResourceId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.appareil, container, false);

        connectButton = rootView.findViewById(R.id.connectButton);
        disconnectButton = rootView.findViewById(R.id.disconnectButton);

        casque_connected = rootView.findViewById(R.id.casque_connected);



        // Get a reference to the device node in the Firebase database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        deviceReference = firebaseDatabase.getReference("devices").child("device1");

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onClick(View v) {



                if(getConnectedState()) {

                    casque_connected.setColorFilter(getResources().getColor(R.color.red));
                    connectButton.setText("casque deconnecté");
                    connectButton.setBackground(getResources().getDrawable(R.drawable.button_red));
                    //other butt inv..
                    disconnectButton.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.button_green));
                    disconnectButton.setText("connecter");
                    //disconnect the device
                    disconnectFromDevice();

                    SharedPreferences sharedPreferences1 = requireContext().getSharedPreferences("appareil", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putBoolean("connected", false);
                    editor1.apply();


                    Toast.makeText(getContext(), "casque deconnecté", Toast.LENGTH_SHORT).show();


                }
                else{

                    casque_connected.setColorFilter(getResources().getColor(R.color.green_settings));
                    connectButton.setText("casque connecté");
                    connectButton.setBackground(getResources().getDrawable(R.drawable.button_green));
                    //other butt inv..
                    disconnectButton.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.button_red));
                    disconnectButton.setText("deconnecter");
                    //disconnect the device
                    //connect the device
                    connectToDevice();

                    SharedPreferences sharedPreferences1 = requireContext().getSharedPreferences("appareil", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putBoolean("connected", true);
                    editor1.apply();


                    Toast.makeText(getContext(), "casque connecté", Toast.LENGTH_SHORT).show();



                }




            }
        });


        casqueState();

        return rootView;


    }  ///////





    private void connectToDevice() {

        // Connect to the device
        deviceReference.setValue(true); // Set the value to true to indicate connection

    }

    private void disconnectFromDevice() {

        // Disconnect from the device
        deviceReference.setValue(false); // Set the value to false to indicate disconnection
    }




    private Boolean getConnectedState(){
        //initialise sharedPreference state
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("appareil", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("connected",false);
    }



    private void casqueState(){

        if(!getConnectedState()) {

            casque_connected.setColorFilter(getResources().getColor(R.color.red));
            connectButton.setText("casque deconnecté");
            connectButton.setBackground(getResources().getDrawable(R.drawable.button_red));
            //other butt inv..
            disconnectButton.setBackground(getResources().getDrawable(R.drawable.button_green));
            disconnectButton.setText("connecter");
            //disconnect the device
            disconnectFromDevice();

            SharedPreferences sharedPreferences1 = requireContext().getSharedPreferences("appareil", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putBoolean("connected", false);
            editor1.apply();

        }

        else{

            casque_connected.setColorFilter(getResources().getColor(R.color.green_settings));
            connectButton.setText("casque connecté");
            connectButton.setBackground(getResources().getDrawable(R.drawable.button_green));
            //other butt inv..
            disconnectButton.setBackground(getResources().getDrawable(R.drawable.button_red));
            disconnectButton.setText("deconnecter");
            //disconnect the device

            //connect the device
            connectToDevice();

            SharedPreferences sharedPreferences1 = requireContext().getSharedPreferences("appareil", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor1 = sharedPreferences1.edit();
            editor1.putBoolean("connected", true);
            editor1.apply();


        }


    }

}  //////
