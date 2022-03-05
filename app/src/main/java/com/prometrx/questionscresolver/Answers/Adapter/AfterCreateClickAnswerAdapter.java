package com.prometrx.questionscresolver.Answers.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.prometrx.questionscresolver.Answers.Model.AfterCreateClickAnswerModel;
import com.prometrx.questionscresolver.R;

import org.w3c.dom.Text;

import java.util.List;

public class AfterCreateClickAnswerAdapter extends RecyclerView.Adapter<AfterCreateClickAnswerAdapter.AfterCreateClickAnswerViewHolder>{

    private List<AfterCreateClickAnswerModel> list;
    private Context context;

    //Firebase
    private FirebaseFirestore firebaseFirestore;

    public AfterCreateClickAnswerAdapter(List<AfterCreateClickAnswerModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AfterCreateClickAnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.after_create_click_answer_layout, parent, false);

        return new AfterCreateClickAnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AfterCreateClickAnswerViewHolder holder, int position) {

        AfterCreateClickAnswerModel model = list.get(position);

        holder.usernameText.setText("Username: " + model.getUsername());

        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("Quiz").document(model.getqId()).collection("Answers").document(model.getuId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                int qC = Integer.parseInt(model.getqCount());

                for (int i = 0; i<=qC; i++) {

                    String tf = "TF" + i;

                    if ((Boolean) documentSnapshot.get(tf)) {
                        System.out.println("Deneme: " + i+"Dogru");
                        switch (i){
                            case 0:
                                holder.tf0.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf0.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                holder.tf1.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf1.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                holder.tf2.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf2.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                holder.tf3.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf3.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                holder.tf4.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf4.setVisibility(View.VISIBLE);
                                break;
                            case 5:
                                holder.tf5.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf5.setVisibility(View.VISIBLE);
                                break;
                            case 6:
                                holder.tf6.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf6.setVisibility(View.VISIBLE);
                                break;
                            case 7:
                                holder.tf7.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf7.setVisibility(View.VISIBLE);
                                break;
                            case 8:
                                holder.tf8.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf8.setVisibility(View.VISIBLE);
                                break;
                            case 9:
                                holder.tf9.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf9.setVisibility(View.VISIBLE);
                                break;
                            case 10:
                                holder.tf10.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf10.setVisibility(View.VISIBLE);
                                break;
                            case 11:
                                holder.tf11.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf11.setVisibility(View.VISIBLE);
                            case 12:
                                holder.tf12.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf12.setVisibility(View.VISIBLE);
                                break;
                            case 13:
                                holder.tf13.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf13.setVisibility(View.VISIBLE);
                                break;
                            case 14:
                                holder.tf14.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf14.setVisibility(View.VISIBLE);
                                break;
                            case 15:
                                holder.tf15.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf15.setVisibility(View.VISIBLE);
                                break;
                            case 16:
                                holder.tf16.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf16.setVisibility(View.VISIBLE);
                                break;
                            case 17:
                                holder.tf17.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf17.setVisibility(View.VISIBLE);
                                break;
                            case 18:
                                holder.tf18.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf18.setVisibility(View.VISIBLE);
                                break;
                            case 19:
                                holder.tf19.setImageResource(R.drawable.green_user_answer_ellipse);
                                holder.tf19.setVisibility(View.VISIBLE);
                                break;
                        }
                    }

                    else{
                        System.out.println("Deneme: " + i+"Yanlis");
                        switch (i){
                            case 0:
                                holder.tf0.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf0.setVisibility(View.VISIBLE);
                                break;
                            case 1:
                                holder.tf1.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf1.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                holder.tf2.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf2.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                holder.tf3.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf3.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                holder.tf4.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf4.setVisibility(View.VISIBLE);
                                break;
                            case 5:
                                holder.tf5.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf5.setVisibility(View.VISIBLE);
                                break;
                            case 6:
                                holder.tf6.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf6.setVisibility(View.VISIBLE);
                                break;
                            case 7:
                                holder.tf7.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf7.setVisibility(View.VISIBLE);
                                break;
                            case 8:
                                holder.tf8.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf8.setVisibility(View.VISIBLE);
                                break;
                            case 9:
                                holder.tf9.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf9.setVisibility(View.VISIBLE);
                                break;
                            case 10:
                                holder.tf10.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf10.setVisibility(View.VISIBLE);
                                break;
                            case 11:
                                holder.tf11.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf11.setVisibility(View.VISIBLE);
                            case 12:
                                holder.tf12.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf12.setVisibility(View.VISIBLE);
                                break;
                            case 13:
                                holder.tf13.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf13.setVisibility(View.VISIBLE);
                                break;
                            case 14:
                                holder.tf14.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf14.setVisibility(View.VISIBLE);
                                break;
                            case 15:
                                holder.tf15.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf15.setVisibility(View.VISIBLE);
                                break;
                            case 16:
                                holder.tf16.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf16.setVisibility(View.VISIBLE);
                                break;
                            case 17:
                                holder.tf17.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf17.setVisibility(View.VISIBLE);
                                break;
                            case 18:
                                holder.tf18.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf18.setVisibility(View.VISIBLE);
                                break;
                            case 19:
                                holder.tf19.setImageResource(R.drawable.red_user_answer_ellipse);
                                holder.tf19.setVisibility(View.VISIBLE);
                                break;
                        }
                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AfterCreateClickAnswerViewHolder extends RecyclerView.ViewHolder{

        TextView usernameText;
        ImageView tf0, tf1, tf2, tf3, tf4, tf5, tf6, tf7, tf8, tf9, tf10, tf11, tf12, tf13, tf14, tf15, tf16, tf17, tf18, tf19;

        public AfterCreateClickAnswerViewHolder(@NonNull View itemView) {
            super(itemView);

            usernameText = itemView.findViewById(R.id.answer_username);

            tf0 = itemView.findViewById(R.id.answer_1);
            tf1 = itemView.findViewById(R.id.answer_2);
            tf2 = itemView.findViewById(R.id.answer_3);
            tf3 = itemView.findViewById(R.id.answer_4);
            tf4 = itemView.findViewById(R.id.answer_5);
            tf5 = itemView.findViewById(R.id.answer_6);
            tf6 = itemView.findViewById(R.id.answer_7);
            tf7 = itemView.findViewById(R.id.answer_8);
            tf8 = itemView.findViewById(R.id.answer_9);
            tf9 = itemView.findViewById(R.id.answer_10);
            tf10 = itemView.findViewById(R.id.answer_11);
            tf11 = itemView.findViewById(R.id.answer_12);
            tf12 = itemView.findViewById(R.id.answer_13);
            tf13 = itemView.findViewById(R.id.answer_14);
            tf14 = itemView.findViewById(R.id.answer_15);
            tf15 = itemView.findViewById(R.id.answer_16);
            tf16 = itemView.findViewById(R.id.answer_17);
            tf17 = itemView.findViewById(R.id.answer_18);
            tf18 = itemView.findViewById(R.id.answer_19);
            tf19 = itemView.findViewById(R.id.answer_20);

        }
    }
}
