package com.prometrx.questionscresolver.Create;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prometrx.questionscresolver.MainActivity;
import com.prometrx.questionscresolver.R;

public class IdActivity extends AppCompatActivity {

    private TextView idTextView, textView, waitText;
    private ImageView finishButton, next;
    private EditText title;
    private Switch repeatable;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    private String id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);

        init();
        clickFinishButton();
        clickNextButton();
        switchChanged();

    }

    private void init() {
        waitText = findViewById(R.id.IdActivityWaitText);
        finishButton = findViewById(R.id.IdActivityFinishButton);
        idTextView = findViewById(R.id.IdActivityIdTextView);
        textView = findViewById(R.id.IdActivityTextView);
        title = findViewById(R.id.IdActivityTitle);
        next = findViewById(R.id.IdActivityNextButton);
        repeatable = findViewById(R.id.IdActivityRepeatableSwitch);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        Intent intent = getIntent();
        id = intent.getStringExtra("QuizId");
        idTextView.setText(id);

    }

    private void clickFinishButton() {

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(IdActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void clickNextButton() {

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String qT = title.getText().toString().trim();

                if (!qT.isEmpty()) {

                    firebaseFirestore.collection("Quiz").document(id+"").update("Title", qT, "Repeatable", repeatable.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            textView.setVisibility(View.VISIBLE);
                            idTextView.setVisibility(View.VISIBLE);
                            finishButton.setVisibility(View.VISIBLE);
                            waitText.setVisibility(View.VISIBLE);
                            next.setVisibility(View.INVISIBLE);
                            title.setVisibility(View.INVISIBLE);
                            repeatable.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(IdActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                else Toast.makeText(IdActivity.this, "Please enter quiz title!", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void switchChanged() {

        repeatable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    repeatable.setText("Not Repeatable");
                }
                else{
                    repeatable.setText("Repeatable");
                }

            }
        });

    }

}