package com.prometrx.questionscresolver.Search.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prometrx.questionscresolver.R;

import java.util.HashMap;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultViewHolder> {

    private Context context;
    private Integer size;
    private String id;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    //Var

    public ResultAdapter(Integer size, String id) {
        this.size = size;
        this.id = id;
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.result_activity_layout, parent, false);

        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder holder, int position) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        holder.questionNumber.setText((position + 1) + "");

        //Var
        int temp = position;

        if (holder.progressBarYA.getVisibility() == View.GONE) {
            holder.progressBarYA.setVisibility(View.VISIBLE);
        }
        if (holder.progressBarCA.getVisibility() == View.GONE) {
            holder.progressBarCA.setVisibility(View.VISIBLE);
        }

        //Compare Answers
        firebaseFirestore.collection("Quiz").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot1) {

                HashMap<String, Object> questionMap = (HashMap<String, Object>) documentSnapshot1.get("" + temp);

                String correctAns = questionMap.get("questionAnswer").toString();

                //Get User Answer
                firebaseFirestore.collection("Quiz").document(id).collection("Answers").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot2) {

                        String userAns = documentSnapshot2.get("" + temp).toString();

                        //Answers Word

                        if (correctAns.equals("1")) holder.correctAnswer.setText("A");
                        else if (correctAns.equals("2")) holder.correctAnswer.setText("B");
                        else if (correctAns.equals("3")) holder.correctAnswer.setText("C");
                        else if (correctAns.equals("4")) holder.correctAnswer.setText("D");
                        else holder.correctAnswer.setText("Blank");

                        if (userAns.equals("1")) holder.yourAnswer.setText("A");
                        else if (userAns.equals("2")) holder.yourAnswer.setText("B");
                        else if (userAns.equals("3")) holder.yourAnswer.setText("C");
                        else if (userAns.equals("4")) holder.yourAnswer.setText("D");
                        else holder.yourAnswer.setText("Blank");

                        //Color
                        if (userAns.equals(correctAns)){
                            holder.yourAnswer.setBackgroundResource(R.drawable.green_result_answer_box);

                            firebaseFirestore.collection("Quiz").document(id).collection("Answers").document(firebaseUser.getUid()).update("TF" + temp, true).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }

                        else{
                            holder.yourAnswer.setBackgroundResource(R.drawable.red_result_answer_box);
                            firebaseFirestore.collection("Quiz").document(id).collection("Answers").document(firebaseUser.getUid()).update("TF" + temp, false).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                        }

                        holder.progressBarYA.setVisibility(View.GONE);
                        holder.progressBarCA.setVisibility(View.GONE);

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        holder.progressBarYA.setVisibility(View.GONE);
                        holder.progressBarCA.setVisibility(View.GONE);
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                holder.progressBarYA.setVisibility(View.GONE);
                holder.progressBarCA.setVisibility(View.GONE);
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return size;
    }

    class ResultViewHolder extends RecyclerView.ViewHolder{

        TextView questionNumber, yourAnswer, correctAnswer;
        ProgressBar progressBarYA, progressBarCA;

        public ResultViewHolder(@NonNull View itemView) {
            super(itemView);

            questionNumber = itemView.findViewById(R.id.result_activity_layout_questionNubmer);
            yourAnswer = itemView.findViewById(R.id.result_activity_layout_yourAnswer);
            correctAnswer = itemView.findViewById(R.id.result_activity_layout_correctAnswer);
            progressBarYA = itemView.findViewById(R.id.result_activity_layout_ProgressBarUserAnswer);
            progressBarCA = itemView.findViewById(R.id.result_activity_layout_ProgressBarCorrectAnswer);

        }
    }

}
