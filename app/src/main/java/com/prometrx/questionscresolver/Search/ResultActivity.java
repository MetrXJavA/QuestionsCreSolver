package com.prometrx.questionscresolver.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.prometrx.questionscresolver.MainActivity;
import com.prometrx.questionscresolver.R;
import com.prometrx.questionscresolver.Search.Adapter.ResultAdapter;

public class ResultActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ImageView finishButton;
    private ResultAdapter resultAdapter;

    //Var
    private String size;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        init();
        exit();

    }

    private void init() {

        recyclerView = findViewById(R.id.ResultActivityRecyclerView);
        finishButton = findViewById(R.id.ResultActivityFinishButton);

        Intent intent = getIntent();
        size = intent.getStringExtra("questionSize");
        id = intent.getStringExtra("questionId");

        recyclerView.setLayoutManager(new LinearLayoutManager(ResultActivity.this));

        Integer x = Integer.valueOf(size) + 1;

        resultAdapter = new ResultAdapter(x, id);
        recyclerView.setAdapter(resultAdapter);

    }

    private void exit() {

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}