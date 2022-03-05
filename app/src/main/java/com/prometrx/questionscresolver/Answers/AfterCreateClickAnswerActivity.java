package com.prometrx.questionscresolver.Answers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.prometrx.questionscresolver.Answers.Adapter.AfterCreateClickAnswerAdapter;
import com.prometrx.questionscresolver.Answers.Model.AfterCreateClickAnswerModel;
import com.prometrx.questionscresolver.MainActivity;
import com.prometrx.questionscresolver.R;

import java.util.ArrayList;
import java.util.List;

public class AfterCreateClickAnswerActivity extends AppCompatActivity {
    //Var
    private String qId;
    private List<AfterCreateClickAnswerModel> afterCreateClickAnswerModelList;
    private AfterCreateClickAnswerAdapter adapter;

    //Components
    private RecyclerView recyclerView;
    private ImageView backImage;

    //Firebase
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_create_click_answer);

        init();
        getData();
        back();

    }

    private void init() {

        Intent intent = getIntent();
        qId = intent.getStringExtra("Qid");

        backImage = findViewById(R.id.AfterCreateClickAnswerActivityBack);
        recyclerView = findViewById(R.id.AfterCreateClickAnswerActivityRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AfterCreateClickAnswerActivity.this));
        afterCreateClickAnswerModelList = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);


    }

    private void getData() {

        //-----

        firebaseFirestore.collection("Quiz").document(qId).collection("Answers").orderBy("date", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                afterCreateClickAnswerModelList.clear();

                for (DocumentSnapshot dc: queryDocumentSnapshots) {

                    String uId = dc.get("Uid").toString();
                    String qCount = dc.get("Qcount").toString();
                    String qId = dc.get("Qid").toString();

                    firebaseFirestore.collection("User").document(uId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            String username = documentSnapshot.get("Username").toString();

                            AfterCreateClickAnswerModel m = new AfterCreateClickAnswerModel(uId, username, qCount, qId);
                            afterCreateClickAnswerModelList.add(m);
                            adapter = new AfterCreateClickAnswerAdapter(afterCreateClickAnswerModelList);
                            recyclerView.setAdapter(adapter);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AfterCreateClickAnswerActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AfterCreateClickAnswerActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //-----

    }

    private void back() {

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AfterCreateClickAnswerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }

}