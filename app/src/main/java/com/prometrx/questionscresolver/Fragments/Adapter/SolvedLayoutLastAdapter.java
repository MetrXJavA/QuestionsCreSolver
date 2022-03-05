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

import com.google.android.material.transition.Hold;
import com.prometrx.questionscresolver.Fragments.Model.SolvedLayoutLastModel;
import com.prometrx.questionscresolver.Fragments.SolvedFragment;
import com.prometrx.questionscresolver.R;
import com.prometrx.questionscresolver.Search.ResultActivity;

import org.w3c.dom.Text;

import java.util.List;

public class SolvedLayoutLastAdapter extends RecyclerView.Adapter<SolvedLayoutLastAdapter.SolvedLayoutViewHolder> {

    private Context context;
    private List<SolvedLayoutLastModel> list;

    public SolvedLayoutLastAdapter(List<SolvedLayoutLastModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SolvedLayoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.solve_layout_last, parent, false);

        return new SolvedLayoutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SolvedLayoutViewHolder holder, int position) {

        SolvedLayoutLastModel m = list.get(position);

        holder.qId.setText("Quiz ID: " + m.getqId());
        holder.qTitle.setText("Quiz Title: " + m.getqTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ResultActivity.class);
                intent.putExtra("questionSize", m.getqCount());
                intent.putExtra("questionId", m.getqId());
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SolvedLayoutViewHolder extends RecyclerView.ViewHolder{

        TextView qId, qTitle;

        public SolvedLayoutViewHolder(@NonNull View itemView) {
            super(itemView);

            qId = itemView.findViewById(R.id.solve_layout_lastQid);
            qTitle = itemView.findViewById(R.id.solve_layout_lastQTitle);

        }
    }
}
