package com.prometrx.questionscresolver.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.prometrx.questionscresolver.R;

public class AccountSettingsActivity extends AppCompatActivity {

    //Firebase
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //Components
    private TextView usernameText, emailText, passwordText;
    private ImageView backImage, doneUsername, doneEmail, donePassword;
    private EditText newUsername, passForUsername, newEmail, passForEmail, newPass, passForPass;
    private ConstraintLayout usernameDetail, emailDetail, passwordDetail;

    //Var

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        init();
        clickBack();
        clickLayouts();
        clickDoneUsername();
        clickDoneEmail();
        clickDonePassword();

    }

    private void init() {

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        usernameText = findViewById(R.id.AccountSettingsChangeUsernameText);
        emailText = findViewById(R.id.AccountSettingsChangeEmailText);
        passwordText = findViewById(R.id.AccountSettingsChangePasswordText);

        backImage = findViewById(R.id.AccountSettingsBack);
        doneUsername = findViewById(R.id.AccountSettingsDoneUsername);
        doneEmail = findViewById(R.id.AccountSettingsDoneEmail);
        donePassword = findViewById(R.id.AccountSettingsDonePassword);

        newUsername = findViewById(R.id.AccountSettingsEnterUsername);
        passForUsername = findViewById(R.id.AccountSettingsEnterPasswordUsername);
        newEmail = findViewById(R.id.AccountSettingsEnterEmail);
        passForEmail = findViewById(R.id.AccountSettingsEnterPasswordEmail);
        newPass = findViewById(R.id.AccountSettingsNewPassword);
        passForPass = findViewById(R.id.AccountSettingsEnterPasswordForPassword);

        usernameDetail = findViewById(R.id.AccountSettingsUsernameDetailLayout);
        emailDetail = findViewById(R.id.AccountSettingsEmailDetailLayout);
        passwordDetail = findViewById(R.id.AccountSettingsPasswordDetailLayout);

    }

    private void clickBack() {

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AccountSettingsActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void clickLayouts() {

        usernameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (usernameDetail.getVisibility() != View.VISIBLE) {
                    usernameDetail.setVisibility(View.VISIBLE);
                }else{
                    usernameDetail.setVisibility(View.GONE);
                    newUsername.setText("");
                    passForUsername.setText("");
                }
            }
        });

        emailText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (emailDetail.getVisibility() != View.VISIBLE) {
                    emailDetail.setVisibility(View.VISIBLE);
                }else{
                    emailDetail.setVisibility(View.GONE);
                    newEmail.setText("");
                    passForEmail.setText("");
                }
            }
        });

        passwordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (passwordDetail.getVisibility() != View.VISIBLE) {
                    passwordDetail.setVisibility(View.VISIBLE);
                }else{
                    passwordDetail.setVisibility(View.GONE);
                    newPass.setText("");
                    passForPass.setText("");
                }
            }
        });



    }

    private void clickDoneUsername() {

        doneUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                doneUsername.setClickable(false);

                if (!newUsername.getText().toString().trim().isEmpty() && !passForUsername.getText().toString().trim().isEmpty()) {

                    firebaseAuth.signInWithEmailAndPassword(firebaseUser.getEmail(), passForUsername.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            firebaseFirestore.collection("User").document(firebaseUser.getUid()).update("Username", newUsername.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {


                                    Toast.makeText(AccountSettingsActivity.this, "Your username changed.", Toast.LENGTH_SHORT).show();
                                    usernameDetail.setVisibility(View.GONE);
                                    newUsername.setText("");
                                    passForUsername.setText("");
                                    doneUsername.setClickable(true);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AccountSettingsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    usernameDetail.setVisibility(View.GONE);
                                    newUsername.setText("");
                                    passForUsername.setText("");
                                    doneUsername.setClickable(true);

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AccountSettingsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            usernameDetail.setVisibility(View.GONE);
                            newUsername.setText("");
                            passForUsername.setText("");
                            doneUsername.setClickable(true);
                        }
                    });

                }
                else{
                    Toast.makeText(AccountSettingsActivity.this, "Please Fill All the Fields.", Toast.LENGTH_SHORT).show();
                    usernameDetail.setVisibility(View.GONE);
                    newUsername.setText("");
                    passForUsername.setText("");
                    doneUsername.setClickable(true);
                }

            }
        });

    }

    private void clickDoneEmail() {

        doneEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                doneEmail.setClickable(false);

                if (!newEmail.getText().toString().trim().isEmpty() && !passForEmail.getText().toString().trim().isEmpty()) {

                    firebaseAuth.signInWithEmailAndPassword(firebaseUser.getEmail(), passForEmail.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            firebaseUser.updateEmail(newEmail.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    doneEmail.setClickable(true);
                                    emailDetail.setVisibility(View.GONE);
                                    newEmail.setText("");
                                    passForEmail.setText("");
                                    Toast.makeText(AccountSettingsActivity.this, "Your email changed.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    doneEmail.setClickable(true);
                                    emailDetail.setVisibility(View.GONE);
                                    newEmail.setText("");
                                    passForEmail.setText("");
                                    Toast.makeText(AccountSettingsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AccountSettingsActivity.this, "Password is wrong.", Toast.LENGTH_SHORT).show();
                            doneEmail.setClickable(true);
                            newEmail.setText("");
                            passForEmail.setText("");
                            emailDetail.setVisibility(View.GONE);
                        }
                    });

                }
                else{
                    Toast.makeText(AccountSettingsActivity.this, "Please Fill All the Fields.", Toast.LENGTH_SHORT).show();
                    doneEmail.setClickable(true);
                    newEmail.setText("");
                    passForEmail.setText("");
                    emailDetail.setVisibility(View.GONE);
                }

            }
        });

    }

    private void clickDonePassword() {

        donePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                donePassword.setClickable(false);

                if (!newPass.getText().toString().trim().isEmpty() && !passForPass.getText().toString().trim().isEmpty()) {

                    firebaseAuth.signInWithEmailAndPassword(firebaseUser.getEmail(), passForPass.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            firebaseUser.updatePassword(newPass.getText().toString().trim()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    donePassword.setClickable(true);
                                    passwordDetail.setVisibility(View.GONE);
                                    newPass.setText("");
                                    passForPass.setText("");
                                    Toast.makeText(AccountSettingsActivity.this, "Your password changed.", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    donePassword.setClickable(true);
                                    passwordDetail.setVisibility(View.GONE);
                                    newPass.setText("");
                                    passForPass.setText("");
                                    Toast.makeText(AccountSettingsActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            donePassword.setClickable(true);
                            passwordDetail.setVisibility(View.GONE);
                            newPass.setText("");
                            passForPass.setText("");
                            Toast.makeText(AccountSettingsActivity.this, "Password is wrong.", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else{
                    donePassword.setClickable(true);
                    passwordDetail.setVisibility(View.GONE);
                    newPass.setText("");
                    passForPass.setText("");
                    Toast.makeText(AccountSettingsActivity.this, "Please Fill All the Fields.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


}