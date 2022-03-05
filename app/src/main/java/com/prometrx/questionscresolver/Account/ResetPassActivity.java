package com.prometrx.questionscresolver.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.prometrx.questionscresolver.R;

public class ResetPassActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText writeEmail;
    private ImageView sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);

        firebaseAuth = FirebaseAuth.getInstance();

        writeEmail = findViewById(R.id.ResetPassEditText);
        sendButton = findViewById(R.id.ResetPassSendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!writeEmail.getText().toString().trim().isEmpty()){
                    sendButton.setClickable(false);

                    firebaseAuth.sendPasswordResetEmail(writeEmail.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Toast.makeText(ResetPassActivity.this, "We sent email please check your email box.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ResetPassActivity.this, SignInActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            sendButton.setClickable(true);
                            Toast.makeText(ResetPassActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else Toast.makeText(ResetPassActivity.this, "Please write email!", Toast.LENGTH_SHORT).show();

            }
        });

    }

}