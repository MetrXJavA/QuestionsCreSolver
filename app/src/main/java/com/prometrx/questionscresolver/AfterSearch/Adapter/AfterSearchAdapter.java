package com.prometrx.questionscresolver.AfterSearch.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.prometrx.questionscresolver.AfterSearch.Model.AfterSearchResultModel;
import com.prometrx.questionscresolver.R;
import com.prometrx.questionscresolver.Search.FindOutQuizActivity;

import java.util.List;

public class AfterSearchAdapter extends RecyclerView.Adapter<AfterSearchAdapter.AfterSearchViewHolder> {

    private List<AfterSearchResultModel> list;
    private Context context;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    public AfterSearchAdapter(List<AfterSearchResultModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AfterSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.after_search_adapter_layout, parent, false);

        return new AfterSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AfterSearchViewHolder holder, int position) {

        AfterSearchResultModel m = list.get(position);

        holder.title.setText(m.getqTitle());
        holder.count.setText(m.getqCount());

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseFirestore.collection("Quiz").document(m.getqId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String temp = documentSnapshot.get("Repeatable").toString();

                        if (temp.equals("Not Repeatable")){

                            firebaseFirestore.collection("User").document(firebaseUser.getUid()).collection("Answers").document(m.getqId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    try {
                                        if (!documentSnapshot.get("Title").toString().isEmpty()) {
                                            Toast.makeText(context, "You can do this test once!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (Exception e) {

                                        Intent intent = new Intent(context, FindOutQuizActivity.class);
                                        intent.putExtra("WriteIdActivityId", m.getqId());
                                        context.startActivity(intent);
                                        ((Activity)context).finish();

                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                    }
                });



            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AfterSearchViewHolder extends RecyclerView.ViewHolder{

        TextView title, count;

        public AfterSearchViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.after_search_adapter_layoutTitle);
            count = itemView.findViewById(R.id.after_search_adapter_layoutCount);

        }
    }

}
