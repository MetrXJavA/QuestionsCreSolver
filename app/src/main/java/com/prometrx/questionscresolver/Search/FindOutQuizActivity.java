package com.prometrx.questionscresolver.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prometrx.questionscresolver.Create.Model.QuestionModel;
import com.prometrx.questionscresolver.MainActivity;
import com.prometrx.questionscresolver.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FindOutQuizActivity extends AppCompatActivity {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private String id = "";
    private String qTitle = "";
    private Integer counter = 0;
    private TextView questionNumber, question, choice1, choice2, choice3, choice4;
    private ImageView exitQuiz, nextQuestion;
    private HashMap<String, Object> answers;
    private String answer = "0";
    private ImageView questionImage;
    private ProgressBar imageProgressBar, timeProgressBar;
    private Handler handler;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_out_quiz);

        init();
        getData();
        selectAnswer();
        clickNext();
        exit();

    }

    private void init() {
        Intent intent = getIntent();
        id = intent.getStringExtra("WriteIdActivityId");
        handler = new Handler();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        questionNumber = findViewById(R.id.FindOutActivityQuestionNumber);
        question = findViewById(R.id.FindOutActivityQuestion);
        choice1 = findViewById(R.id.FindOutActivityChoice1);
        choice2 = findViewById(R.id.FindOutActivityChoice2);
        choice3 = findViewById(R.id.FindOutActivityChoice3);
        choice4 = findViewById(R.id.FindOutActivityChoice4);

        exitQuiz = findViewById(R.id.FindOutActivityExitQuiz);
        nextQuestion = findViewById(R.id.FindOutActivityNextButton);

        questionImage = findViewById(R.id.FindOutActivityImageView);
        imageProgressBar = findViewById(R.id.FindOutActivityProgressBar);
        timeProgressBar = findViewById(R.id.FindOutActivityTimeProgressBar);

        answers = new HashMap<>();

    }

    private void progressBarTiming(int t) {

        timeProgressBar.setProgress(0);

        timeProgressBar.setMax(t*10);//Daha hizli akmasi icin yapilan bir duzenleme.

        countDownTimer = new CountDownTimer(t*1000, 100) {

            @Override
            public void onTick(long l) {

                timeProgressBar.setProgress(timeProgressBar.getProgress() + 1);

            }

            @Override
            public void onFinish() {

                checkDataForNext();

            }
        }.start();

    }

    private void getData() {

        firebaseFirestore.collection("Quiz").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                HashMap<String, Object> hashMap = (HashMap<String, Object>) documentSnapshot.get("0");
                question.setText(hashMap.get("question").toString());

                if (!hashMap.get("imageUrl").toString().equals("0")) {
                    imageProgressBar.setVisibility(View.VISIBLE);
                    Picasso.get().load(hashMap.get("imageUrl").toString()).into(questionImage);
                    questionImage.setVisibility(View.VISIBLE);

                }
                else{
                    imageProgressBar.setVisibility(View.GONE);
                    questionImage.setVisibility(View.GONE);
                }

                if (!hashMap.get("choiceFour").toString().isEmpty()) {
                    choice1.setText(hashMap.get("choiceOne").toString());
                    choice2.setText(hashMap.get("choiceTwo").toString());
                    choice3.setText(hashMap.get("choiceThree").toString());
                    choice4.setText(hashMap.get("choiceFour").toString());
                    choice3.setVisibility(View.VISIBLE);
                    choice4.setVisibility(View.VISIBLE);
                }
                else if(!hashMap.get("choiceThree").toString().isEmpty()) {
                    choice1.setText(hashMap.get("choiceOne").toString());
                    choice2.setText(hashMap.get("choiceTwo").toString());
                    choice3.setText(hashMap.get("choiceThree").toString());
                    choice4.setVisibility(View.GONE);
                }
                else{
                    choice1.setText(hashMap.get("choiceOne").toString());
                    choice2.setText(hashMap.get("choiceTwo").toString());
                    choice3.setVisibility(View.GONE);
                    choice4.setVisibility(View.GONE);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageProgressBar.setVisibility(View.GONE);
                    }
                }, 1000);

                progressBarTiming(Integer.parseInt(hashMap.get("questionTime").toString()));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageProgressBar.setVisibility(View.GONE);
                    }
                }, 1000);

                Toast.makeText(FindOutQuizActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void clickNext() {

        nextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Next
                countDownTimer.cancel();
                checkDataForNext();

            }
        });

    }

    private void checkDataForNext() {

        answers.put("" + counter, answer);

        firebaseFirestore.collection("Quiz").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                HashMap<String, Object> hashMap = (HashMap<String, Object>) documentSnapshot.get("" + (counter + 1));
                qTitle = documentSnapshot.get("Title").toString();

                try {

                    if (!hashMap.get("imageUrl").toString().equals("0")) {
                        imageProgressBar.setVisibility(View.VISIBLE);
                        Picasso.get().load(hashMap.get("imageUrl").toString()).into(questionImage);
                        questionImage.setVisibility(View.VISIBLE);
                    }
                    else{
                        imageProgressBar.setVisibility(View.GONE);
                        questionImage.setVisibility(View.GONE);
                    }

                    if (hashMap.get("question").toString() != null) {
                        question.setText(hashMap.get("question").toString());

                        switch (answer) {
                            case "1":
                                choice1.setBackgroundResource(R.drawable.choice_edit_text);
                                break;
                            case "2":
                                choice2.setBackgroundResource(R.drawable.choice_edit_text);
                                break;
                            case "3":
                                choice3.setBackgroundResource(R.drawable.choice_edit_text);
                                break;
                            case "4":
                                choice4.setBackgroundResource(R.drawable.choice_edit_text);
                                break;

                        }

                        questionNumber.setText(counter + 2 + "");

                        if (!hashMap.get("choiceFour").toString().isEmpty()) {
                            choice1.setText(hashMap.get("choiceOne").toString());
                            choice2.setText(hashMap.get("choiceTwo").toString());
                            choice3.setText(hashMap.get("choiceThree").toString());
                            choice4.setText(hashMap.get("choiceFour").toString());
                            choice3.setVisibility(View.VISIBLE);
                            choice4.setVisibility(View.VISIBLE);

                        }
                        else if(!hashMap.get("choiceThree").toString().isEmpty()) {
                            choice1.setText(hashMap.get("choiceOne").toString());
                            choice2.setText(hashMap.get("choiceTwo").toString());
                            choice3.setText(hashMap.get("choiceThree").toString());
                            choice3.setVisibility(View.VISIBLE);
                            choice4.setVisibility(View.GONE);
                        }
                        else{
                            choice1.setText(hashMap.get("choiceOne").toString());
                            choice2.setText(hashMap.get("choiceTwo").toString());
                            choice3.setVisibility(View.GONE);
                            choice4.setVisibility(View.GONE);
                        }

                        answer = "0";
                        counter++;

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                imageProgressBar.setVisibility(View.GONE);
                            }
                        }, 1000);

                        progressBarTiming(Integer.parseInt(hashMap.get("questionTime").toString()));

                    }
                }
                catch (Exception ex) {
                    //Finish

                    answers.put("Uid", firebaseUser.getUid());
                    answers.put("Qid", id);
                    answers.put("Title", qTitle);
                    answers.put("date", FieldValue.serverTimestamp());
                    answers.put("Qcount", counter + "");

                    firebaseFirestore.collection("Quiz").document(id).collection("Answers").document(firebaseUser.getUid()).set(answers).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            firebaseFirestore.collection("User").document(firebaseUser.getUid()).collection("Answers").document(id).set(answers).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent intent = new Intent(FindOutQuizActivity.this, ResultActivity.class);
                                    intent.putExtra("questionSize", counter + "");
                                    intent.putExtra("questionId", id);
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(FindOutQuizActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FindOutQuizActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FindOutQuizActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void selectAnswer() {

        choice1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!answer.equals("1")) {
                    choice1.setBackgroundResource(R.drawable.choice_edit_text_green);
                    choice2.setBackgroundResource(R.drawable.choice_edit_text);
                    choice3.setBackgroundResource(R.drawable.choice_edit_text);
                    choice4.setBackgroundResource(R.drawable.choice_edit_text);
                    answer = "1";
                }
                else {
                    choice1.setBackgroundResource(R.drawable.choice_edit_text);
                    answer = "0";
                }

            }
        });

        choice2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!answer.equals("2")) {
                    choice2.setBackgroundResource(R.drawable.choice_edit_text_green);
                    choice1.setBackgroundResource(R.drawable.choice_edit_text);
                    choice3.setBackgroundResource(R.drawable.choice_edit_text);
                    choice4.setBackgroundResource(R.drawable.choice_edit_text);
                    answer = "2";
                }
                else {
                    choice2.setBackgroundResource(R.drawable.choice_edit_text);
                    answer = "0";
                }

            }
        });

        choice3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!answer.equals("3")) {
                    choice3.setBackgroundResource(R.drawable.choice_edit_text_green);
                    choice1.setBackgroundResource(R.drawable.choice_edit_text);
                    choice2.setBackgroundResource(R.drawable.choice_edit_text);
                    choice4.setBackgroundResource(R.drawable.choice_edit_text);
                    answer = "3";
                }
                else {
                    choice3.setBackgroundResource(R.drawable.choice_edit_text);
                    answer = "0";
                }

            }
        });

        choice4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!answer.equals("4")) {
                    choice4.setBackgroundResource(R.drawable.choice_edit_text_green);
                    choice1.setBackgroundResource(R.drawable.choice_edit_text);
                    choice2.setBackgroundResource(R.drawable.choice_edit_text);
                    choice3.setBackgroundResource(R.drawable.choice_edit_text);
                    answer = "4";
                }
                else {
                    choice4.setBackgroundResource(R.drawable.choice_edit_text);
                    answer = "0";
                }

            }
        });

    }

    private void exit() {

        exitQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindOutQuizActivity.this, MainActivity.class);
                startActivity(intent);
                countDownTimer.cancel();
                finish();
            }
        });

    }

}