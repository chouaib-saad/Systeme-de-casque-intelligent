package com.example.casquenet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private TextView goToForget,goToSignUp;
    private EditText email,password;
    private Button signIn;
    private CheckBox remember;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    ImageView butRetour;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signIn = findViewById(R.id.button_login);
        goToSignUp =findViewById(R.id.textview_register);
        goToForget=findViewById(R.id.forgetpass);
        email=findViewById(R.id.emailSignIn);
        password=findViewById(R.id.passwordSgnIn);
        remember=findViewById(R.id.remember);
        progressDialog=new ProgressDialog(this);
        firebaseAuth= FirebaseAuth.getInstance();

        butRetour = findViewById(R.id.butRetour);


        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    SharedPreferences preferences=getSharedPreferences("checkBox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                } else if (!buttonView.isChecked()) {
                    SharedPreferences preferences=getSharedPreferences("checkBox",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();

                }

            }
        });





        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().isEmpty() || !email.getText().toString().contains("@") || !email.getText().toString().contains(".")){
                    email.setError("Email is invalid!");
                }else if (password.getText().toString().isEmpty() || password.getText().toString().length()<7){
                    password.setError("Password is invalid");
                }else{
                    validate(email.getText().toString(),password.getText().toString());
                }
            }
        });
        goToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(LoginActivity.this,RegisterActivity.class));

                startActivityWithAnimation(LoginActivity.this,new RegisterActivity());
            }
        });
        goToForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetPassord.class));
            }
        });






        butRetour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



    }  ///////

    private void validate(String emailS, String passwordS) {
        progressDialog.setMessage("attendez... !");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(emailS,passwordS).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                    checkEmailVerification();
                }else{
                    Toast.makeText(LoginActivity.this, "Veuillez vérifier que vos données sont correctes !", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }

            }
        });
    }

    private void checkEmailVerification() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        boolean emailFlag = user.isEmailVerified();

        if (emailFlag) {
            finish();
            startActivity(new Intent(LoginActivity.this, MainScreen.class));
        } else {
            Toast.makeText(this, "Merci de consulter vos emails !", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }



    private void startActivityWithAnimation(Activity currentActivity,Activity newActivityClass){

        // Start the new activity with a fade animation
        Intent intent = new Intent(currentActivity, newActivityClass.getClass());
        currentActivity.startActivity(intent);
        currentActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        // Finish the current activity
        currentActivity.finish();
    }






}  ////////////
