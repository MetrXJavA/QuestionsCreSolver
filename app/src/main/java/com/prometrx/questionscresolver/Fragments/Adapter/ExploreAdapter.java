package com.prometrx.questionscresolver.Fragments.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.prometrx.questionscresolver.Fragments.Model.ExploreModel;
import com.prometrx.questionscresolver.R;
import com.prometrx.questionscresolver.Search.FindOutQuizActivity;

import org.w3c.dom.Text;

import java.util.List;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ExploreAdapterViewHolder> {

    private List<ExploreModel> exploreModelList;
    private Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    public ExploreAdapter(List<ExploreModel> exploreModelList) {
        this.exploreModelList = exploreModelList;
    }

    @NonNull
    @Override
    public ExploreAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.explore_quiz_layout, parent, false);

        return new ExploreAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreAdapterViewHolder holder, int position) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestore.setLoggingEnabled(true);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        ExploreModel exploreModel = exploreModelList.get(position);

        int temp = position;
        int caseC = temp % 4;

        switch (temp){
            case 0:
                holder.layout.setBackgroundResource(R.drawable.green_explore_layout_background);
                break;
            case 1:
                holder.layout.setBackgroundResource(R.drawable.yellow_explore_layout_background);
                break;
            case 2:
                holder.layout.setBackgroundResource(R.drawable.red_explore_layout_background);
                break;
            case 3:
                holder.layout.setBackgroundResource(R.drawable.purple_explore_layout_background);
                break;
        }

        holder.qCount.setText(exploreModelList.get(position).getqCount());
        holder.qTitle.setText(exploreModelList.get(position).getqTitle());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseFirestore.collection("Quiz").document(exploreModelList.get(temp).getqId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String temp = documentSnapshot.get("Repeatable").toString();

                        if (temp.equals("Not Repeatable")){

                            firebaseFirestore.collection("User").document(firebaseUser.getUid()).collection("Answers").document(exploreModel.getqId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    try {
                                        if (!documentSnapshot.get("Title").toString().isEmpty()) {
                                            Toast.makeText(context, "You can do this test once!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    catch (Exception e) {

                                        Intent intent = new Intent(context, FindOutQuizActivity.class);
                                        intent.putExtra("WriteIdActivityId", exploreModel.getqId());
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
        return exploreModelList.size();
    }

    class ExploreAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView qCount, qTitle;
        LinearLayout layout;

        public ExploreAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            qCount = itemView.findViewById(R.id.explore_quiz_layoutQCount);
            qTitle = itemView.findViewById(R.id.explore_quiz_layoutQTitle);
            layout = itemView.findViewById(R.id.explore_quiz_layoutMainLayout);

        }
    }


}
