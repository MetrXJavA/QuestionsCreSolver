package com.prometrx.questionscresolver.AfterSearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.prometrx.questionscresolver.AfterSearch.Adapter.AfterSearchAdapter;
import com.prometrx.questionscresolver.AfterSearch.Model.AfterSearchResultModel;
import com.prometrx.questionscresolver.MainActivity;
import com.prometrx.questionscresolver.R;

import java.util.ArrayList;
import java.util.List;

public class AfterSearchActivity extends AppCompatActivity {

    private ImageView backImageView;
    private RecyclerView recyclerView;
    private List<AfterSearchResultModel> afterSearchResultModelList;
    private AfterSearchAdapter adapter;


    //Firebase
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    //Var
    private String yTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_search);

        init();
        back();
        getData();

    }

    private void init() {

        Intent intent = getIntent();
        yTitle = intent.getStringExtra("Words").trim().toLowerCase();

        backImageView = findViewById(R.id.AfterSearchActivityBack);
        recyclerView = findViewById(R.id.AfterSearchActivityRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AfterSearchActivity.this));
        firebaseFirestore = FirebaseFirestore.getInstance();
        afterSearchResultModelList = new ArrayList<>();

    }

    private void back() {

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AfterSearchActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    private void getData() {

        firebaseFirestore.collection("Quiz").orderBy("date", Query.Direction.ASCENDING).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot dc : queryDocumentSnapshots) {

                    String dTitle = dc.get("Title").toString().trim().toLowerCase();

                    if (dTitle.contains(yTitle)){

                        AfterSearchResultModel model = new AfterSearchResultModel(dc.get("Qid").toString(), dc.get("Qcount").toString(), dc.get("Title").toString());

                        afterSearchResultModelList.add(model);

                    }

                    adapter = new AfterSearchAdapter(afterSearchResultModelList);
                    recyclerView.setAdapter(adapter);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

}