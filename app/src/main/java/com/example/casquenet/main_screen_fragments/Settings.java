package com.example.casquenet.main_screen_fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.casquenet.R;
import com.example.casquenet.main_screen_fragments.settings_frags.Alert;
import com.example.casquenet.main_screen_fragments.settings_frags.Apropos;
import com.example.casquenet.main_screen_fragments.settings_frags.Santee;


public class Settings extends Fragment implements View.OnClickListener {

    private CardView sante, profil, propos, alert;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);



        sante = view.findViewById(R.id.sante);
        profil = view.findViewById(R.id.profil);
        propos = view.findViewById(R.id.propos);
        alert = view.findViewById(R.id.alert);

        sante.setOnClickListener(this);
        profil.setOnClickListener(this);
        propos.setOnClickListener(this);
        alert.setOnClickListener(this);

        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profil:
                UserProfile userProfileFragment = new UserProfile();
                startFragment(userProfileFragment);
                break;
            case R.id.propos:
                Apropos aproposFragment = new Apropos();
                startFragment(aproposFragment);
                break;
            case R.id.alert:
                Alert alertFragment = new Alert();
                startFragment(alertFragment);
                break;
            case R.id.sante:
                Santee santeeFragment = new Santee();
                startFragment(santeeFragment);
                break;
        }
    }

    private void startFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        transaction.replace(R.id.fragmentContainer, fragment);
        transaction.addToBackStack(null); // Add the transaction to the back stack
        transaction.commit();
    }
}

