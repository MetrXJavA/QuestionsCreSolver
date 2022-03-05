package com.prometrx.questionscresolver.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.prometrx.questionscresolver.AlertDialog.CustomAlertDialog;
import com.prometrx.questionscresolver.MainActivity;
import com.prometrx.questionscresolver.R;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private EditText emailEditText, passwordEditText;
    private TextView createTextView, resetPass;
    private ImageView nextImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();
        create();
        next();
        clickResetPass();

    }

    private void init() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        emailEditText = findViewById(R.id.SignInActivityEmail);
        passwordEditText = findViewById(R.id.SignInActivityPassword);
        createTextView = findViewById(R.id.SignInActivityCreate);
        resetPass = findViewById(R.id.SignInActivityResetPassword);
        nextImageButton = findViewById(R.id.SignInActivityNext);

        if (firebaseAuth.getCurrentUser() != null && firebaseAuth.getCurrentUser().isEmailVerified()) {
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void create() {

        createTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();

            }
        });



    }

    private void clickResetPass() {

        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignInActivity.this, ResetPassActivity.class);
                startActivity(intent);

            }
        });

    }

    private void next() {

        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final CustomAlertDialog alertDialog = new CustomAlertDialog(SignInActivity.this);

                alertDialog.startDialog();

                String e = emailEditText.getText().toString().trim();
                String p = passwordEditText.getText().toString().trim();

                if (!e.isEmpty() && !p.isEmpty()){

                    firebaseAuth.signInWithEmailAndPassword(e, p).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            alertDialog.dismissDialog();

                            if (firebaseAuth.getCurrentUser().isEmailVerified()){
                                Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }else Toast.makeText(SignInActivity.this, "Please verify email!", Toast.LENGTH_SHORT).show();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            alertDialog.dismissDialog();
                            Toast.makeText(SignInActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    alertDialog.dismissDialog();
                    Toast.makeText(SignInActivity.this, "Please Enter Correct Values!", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

}