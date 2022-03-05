package com.prometrx.questionscresolver.Account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prometrx.questionscresolver.AlertDialog.CustomAlertDialog;
import com.prometrx.questionscresolver.MainActivity;
import com.prometrx.questionscresolver.R;

import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private EditText emailEditText, passwordEditText, usernameEditText;
    private ImageView createImageButton, backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
        create();
        back();

    }

    private void init() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        emailEditText = findViewById(R.id.SignUpActivityEmail);
        passwordEditText = findViewById(R.id.SignUpActivityPassword);
        usernameEditText = findViewById(R.id.SignUpActivityUsername);
        createImageButton = findViewById(R.id.SignUpActivityCreate);
        backImageView = findViewById(R.id.SignUpActivityBack);

    }

    private void create(){

        final CustomAlertDialog alertDialog = new CustomAlertDialog(SignUpActivity.this);

        createImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.startDialog();

                String e = emailEditText.getText().toString().trim();
                String p = passwordEditText.getText().toString().trim();
                String u = usernameEditText.getText().toString().trim();

                if (!e.isEmpty() && !p.isEmpty() && !u.isEmpty()){
                    firebaseAuth.createUserWithEmailAndPassword(e, p).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    Toast.makeText(SignUpActivity.this, "We sent your email verification later 1-2 minutes. If you don't allow it, you can't sign in app!", Toast.LENGTH_LONG).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("Uid", firebaseAuth.getCurrentUser().getUid());
                            hashMap.put("ImageUrl", "0");
                            hashMap.put("Username", u);

                            firebaseFirestore.collection("User").document(firebaseAuth.getCurrentUser().getUid()).set(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    alertDialog.dismissDialog();
                                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    alertDialog.dismissDialog();
                                    Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            alertDialog.dismissDialog();
                            Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    alertDialog.dismissDialog();
                    Toast.makeText(SignUpActivity.this, "Please Fill All the Fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void back() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

}