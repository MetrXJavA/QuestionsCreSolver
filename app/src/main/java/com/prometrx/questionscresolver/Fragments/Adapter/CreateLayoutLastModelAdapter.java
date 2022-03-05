package com.prometrx.questionscresolver.Fragments.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prometrx.questionscresolver.Answers.AfterCreateClickAnswerActivity;
import com.prometrx.questionscresolver.Fragments.Model.CreateLayoutLastModel;
import com.prometrx.questionscresolver.R;

import java.util.List;

public class CreateLayoutLastModelAdapter extends RecyclerView.Adapter<CreateLayoutLastModelAdapter.CreateLayoutLastModelViewHolder> {

    private List<CreateLayoutLastModel> list;
    private Context context;

    public CreateLayoutLastModelAdapter(List<CreateLayoutLastModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public CreateLayoutLastModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.create_layout_last, parent, false);

        return new CreateLayoutLastModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreateLayoutLastModelViewHolder holder, int position) {

        CreateLayoutLastModel createLayoutLastModel = list.get(position);

        holder.qId.setText("Quiz ID: " + createLayoutLastModel.getqId());
        holder.qTitle.setText("Quiz Title: " + createLayoutLastModel.getqTitle());
        holder.aCount.setText("Answers: " + createLayoutLastModel.getaCount());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AfterCreateClickAnswerActivity.class);
                intent.putExtra("Qid", createLayoutLastModel.getqId());
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class CreateLayoutLastModelViewHolder extends RecyclerView.ViewHolder{

        TextView qId, qTitle, aCount;

        public CreateLayoutLastModelViewHolder(@NonNull View itemView) {
            super(itemView);

            qId = itemView.findViewById(R.id.create_layout_lastId);
            qTitle = itemView.findViewById(R.id.create_layout_lastQTitle);
            aCount = itemView.findViewById(R.id.create_layout_lastAnswers);

        }
    }

}
