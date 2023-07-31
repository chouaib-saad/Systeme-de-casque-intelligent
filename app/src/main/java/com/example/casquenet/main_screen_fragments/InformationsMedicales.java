package com.example.casquenet.main_screen_fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.casquenet.R;
import com.example.casquenet.main_screen_fragments.settings_frags.CustomSpinnerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class InformationsMedicales extends Fragment {



    ProgressDialog progressDialog;

    Spinner spinner;
    private SeekBar poids_seekbar;
    TextView poids_Txt;


    RadioGroup operationsRadioGroup;

    EditText allergiesEditText,autreEditText;
    private Button saveButton;

    String allergiesEditText_string,autreEditText_string;

    //databse
    FirebaseFirestore db;
    FirebaseAuth auth;
    String userId;


    RadioButton ouiRadioButtonOperation ;
    RadioButton nonRadioButtonOperation ;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.informations_medicales, container, false);




        ouiRadioButtonOperation = rootView.findViewById(R.id.ouiRadioButton2);
        nonRadioButtonOperation = rootView.findViewById(R.id.nonRadioButton2);



        //initialisation
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();

        saveButton = rootView.findViewById(R.id.saveButton);

        //spinner hook
        spinner = rootView.findViewById(R.id.spinner);
        poids_seekbar = rootView.findViewById(R.id.poids_seekbar);
        poids_Txt = rootView.findViewById(R.id.poids_Txt);


        //
        operationsRadioGroup = rootView.findViewById(R.id.operationsRadioGroup);

        allergiesEditText = rootView.findViewById(R.id.allergiesEditText);
        autreEditText = rootView.findViewById(R.id.autreEditText);




        // Create an array of items for the Spinner
        CharSequence[] items = getResources().getStringArray(R.array.spinner_entries);

        // Create the custom adapter
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), R.layout.spinner_dropdown_item, R.layout.spinner_dropdown_item, items);

        // Apply the adapter to the Spinner
        spinner.setAdapter(adapter);

        spinner.setSelection(0, false); // Set the hint item as the default selection





        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!validateAutre() | !validateAllergies() | !validatePoids() | !validateSanguim()) {
                    return;

                } else {


                    String selectedOperationsValue = "";



                    // Get selected value from operationsRadioGroup
                    int selectedOperationsRadioButtonId = operationsRadioGroup.getCheckedRadioButtonId();
                    if (selectedOperationsRadioButtonId != -1) {
                        RadioButton selectedOperationsRadioButton = rootView.findViewById(selectedOperationsRadioButtonId);
                        selectedOperationsValue = selectedOperationsRadioButton.getTag().toString();
                    }



                    //allergies
                    allergiesEditText_string = allergiesEditText.getText().toString();
                    //autres
                    autreEditText_string = autreEditText.getText().toString();
                    //poids
                    int seekBarValue = poids_seekbar.getProgress();
                    //list
                    String selectedValue = spinner.getSelectedItem().toString();
                    int selectedValuePosition = spinner.getSelectedItemPosition();



                    Map<String, Object> data = new HashMap<>();
                    data.put("Operations", selectedOperationsValue);
                    data.put("allergies", allergiesEditText_string);
                    data.put("autres", autreEditText_string);
                    data.put("poids", seekBarValue);
                    data.put("typeSanguim", selectedValue);
                    data.put("sanguimPosition", selectedValuePosition);

                    showProgressDialog(getContext(),"chargement",true);

                    CollectionReference utilisateursCollection = db.collection("utilisateurs");
                    DocumentReference documentReference = utilisateursCollection.document(userId).collection("information_medicales").document(userId);
                    documentReference.set(data)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Document created successfully
                                    Toast.makeText(getContext(), "Votre donnée a été ajoutée avec succès", Toast.LENGTH_SHORT).show();
                                    showProgressDialog(getContext(),"chargement",false);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Error occurred while creating the document
                                    Toast.makeText(getContext(), "Échec de la création du document :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    showProgressDialog(getContext(),"chargement",false);

                                }
                            });



                }


            }
        });  //save button end





        poids_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the text of the TextView with the selected number
                poids_Txt.setText(progress+" Kg");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for this example
            }
        });









        /*
        //spinner listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    // Handle the selection of the hint item
                    // You can display a message or perform any desired action
                    Log.d("TAG", "veuillez sélectionner un type !");
                } else {
                    // Handle the selection of other items
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    // Process the selected item as needed
                    Toast.makeText(getContext(), selectedItem, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where no item is selected
            }
        });

*/



        updatedData();


        return rootView;

    }   ////












    private Boolean validateAllergies() {
        String val = allergiesEditText.getText().toString();
        /*en peut utiliser aussi val.equals("")
        pour verifier que le champ est vide*/
        if (val.trim().isEmpty()) {
            allergiesEditText.requestFocus();
            allergiesEditText.setError("le champ ne peut pas être vide");
            return false;
        } else {
            allergiesEditText.setError(null);
            return true;

        }
    }


    private Boolean validateAutre() {
        String val = autreEditText.getText().toString();
        /*en peut utiliser aussi val.equals("")
        pour verifier que le champ est vide*/
        if (val.trim().isEmpty()) {
            autreEditText.requestFocus();
            autreEditText.setError("le champ ne peut pas être vide");
            return false;
        } else {
            autreEditText.setError(null);
            return true;

        }
    }



    private Boolean validatePoids() {

        String seekBarValue = String.valueOf(poids_seekbar.getProgress());

        if (seekBarValue.equals("0")) {

            poids_seekbar.requestFocus();
            Toast.makeText(getContext(), "sélectionner votre poids", Toast.LENGTH_SHORT).show();

            return false;

        } else {

            return true;
        }

    }

        private Boolean validateSanguim(){

            String selectedValue = spinner.getSelectedItem().toString();

            if(selectedValue.equals("Sélectionnez")){

                spinner.requestFocus();
                Toast.makeText(getContext(), "sélectionner votre groupe sanguin", Toast.LENGTH_SHORT).show();

                return false;

            }else {
                return true;
            }

        }







    private void updatedData() {

        showProgressDialog(getContext(),"chargement",true);
        CollectionReference utilisateursCollection = db.collection("utilisateurs");
        DocumentReference documentReference = utilisateursCollection.document(userId)
                .collection("information_medicales").document(userId);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Document exists, retrieve the data
                        String donneurOrgane = document.getString("DonneurOrgane");
                        String operations = document.getString("Operations");
                        String allergies = document.getString("allergies");
                        String autres = document.getString("autres");
                        int poids = Math.toIntExact(document.getLong("poids"));
                        //String typeSanguim = document.getString("typeSanguim");
                        int positionSanguim = Math.toIntExact(document.getLong("sanguimPosition"));




                        allergiesEditText.setText(allergies);
                        autreEditText.setText(autres);
                        poids_seekbar.setProgress(poids);
                        spinner.setSelection(positionSanguim);



                        if (operations.equals("yes")) {
                            ouiRadioButtonOperation.setChecked(true);
                        } else if (operations.equals("no")) {
                            nonRadioButtonOperation.setChecked(true);
                        }




                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                showProgressDialog(getContext(),"chargement",false);

                            }
                        },2500);



                        // Perform the desired action with the retrieved data
                    } else {
                        // Document does not exist
                        Toast.makeText(getContext(), "remplir votre informations medicales s'il vous plais !", Toast.LENGTH_SHORT).show();
                        showProgressDialog(getContext(),"chargement",false);

                    }
                } else {
                    // Error occurred while fetching the document
                    Toast.makeText(getContext(), "Erreur lors de la récupération du document :" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    showProgressDialog(getContext(),"chargement",false);

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




}   ////
