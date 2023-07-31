package com.example.casquenet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassord extends AppCompatActivity {
    private EditText emailReset;
    private Button btnResetPassword;
    private Button btnBack;
    private FirebaseAuth firebaseAuth;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailReset = findViewById(R.id.emailReset);
        btnResetPassword = findViewById(R.id.btnReset_Password);
        btnBack = findViewById(R.id.btnBack);

        firebaseAuth = FirebaseAuth.getInstance();

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailReset.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(ForgetPassord.this, "Veuillez saisir votre adresse e-mail", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(ForgetPassord.this, "Un e-mail de réinitialisation a été envoyé à votre adresse e-mail", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ForgetPassord.this, "Échec de l'envoi de l'e-mail de réinitialisation", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });



    }
}