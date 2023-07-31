package com.example.casquenet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, email, id, phone, password,birthdate;
    private Button btnSingUp;
    private String nameS, emailS, idS, phoneS, passwordS,birthdateS;
    private FirebaseAuth firebaseAuth;
    private int selectedYear, selectedMonth, selectedDay;
    private FirebaseDatabase firebaseDatabase;
    private ProgressDialog progressDialog;
    public TextView havec;

    private ImageView butRetour;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


        havec = findViewById(R.id.textview_hello);
        name = findViewById(R.id.edittext_name);
        email = findViewById(R.id.edittext_email);
        phone = findViewById(R.id.edittext_phone);
        password = findViewById(R.id.edittext_pass);
        id = findViewById(R.id.edittext_id);
        btnSingUp = findViewById(R.id.button_register);
        birthdate = findViewById(R.id.edittext_birthdate);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);

        butRetour = findViewById(R.id.buttRetour);

        havec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));

                startActivityWithAnimation(RegisterActivity.this,new LoginActivity());


            }
        });
        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.setMessage("attendez... !");
                progressDialog.setCancelable(false);
                progressDialog.show();

                String user_email = email.getText().toString().trim();
                String user_password = password.getText().toString().trim();

                if (!validate()) {
                    progressDialog.dismiss();
                    return;

                } else{


                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sendEmailVerification();

                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Done", Toast.LENGTH_SHORT).show();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });



        butRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // Mise à jour de la date sélectionnée
                selectedYear = year;
                selectedMonth = monthOfYear;
                selectedDay = dayOfMonth;

                // Mise à jour du champ de saisie de la date de naissance avec la date sélectionnée
                String dateString = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year);
                birthdate.setText(dateString);
            }
        }, selectedYear, selectedMonth, selectedDay);

        // Affichage du dialogue de sélection de la date
        datePickerDialog.show();
    }


    private void sendEmailVerification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        sendUserData();
                        Toast.makeText(RegisterActivity.this,"Inscription faite ! s'il vous plaît vérifier votre e-mail !",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                    } else {
                        Toast.makeText(RegisterActivity.this, "Échec de l'inscription  !", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserData() {
        DatabaseReference myRef= firebaseDatabase.getReference("Users");
        User user= new User(nameS, emailS, idS, phoneS,birthdateS);
        myRef.child(""+firebaseAuth.getUid()).setValue(user);
    }

    private boolean validate() {
        boolean result = false;

        nameS = name.getText().toString();
        emailS = email.getText().toString();
        idS = id.getText().toString();
        phoneS = phone.getText().toString();
        passwordS = password.getText().toString();
        birthdateS = birthdate.getText().toString().trim();

        if (nameS.isEmpty() || nameS.length() < 7) {
            name.requestFocus();
            name.setError("Name is invalid");
        } else if (emailS.isEmpty() || !emailS.contains("@") || !emailS.contains(".")) {
            email.requestFocus();
            email.setError("Email is invalid");
        } else if (idS.length() != 8) {
            id.requestFocus();
            id.setError("ID is invalid");
        } else if (phoneS.length() != 8) {
            phone.requestFocus();
            phone.setError("Phone is invalid");
        } else if (passwordS.isEmpty() || passwordS.length() < 7) {
            password.requestFocus();
            password.setError("Password is invalid");
        } else if (birthdateS.isEmpty()) {
            birthdate.requestFocus();
            birthdate.setError("Date of Birth is invalid");
        } else {
            result = true;
        }

        return result;
    }



    private void startActivityWithAnimation(Activity currentActivity, Activity newActivityClass){

        // Start the new activity with a fade animation
        Intent intent = new Intent(currentActivity, newActivityClass.getClass());
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // Finish the current activity
        currentActivity.finish();
    }





}