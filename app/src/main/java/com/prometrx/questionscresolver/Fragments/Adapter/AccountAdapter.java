package com.prometrx.questionscresolver.Fragments.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prometrx.questionscresolver.Fragments.Model.AccountModel;
import com.prometrx.questionscresolver.R;

import java.util.List;

public class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.AccountAdapterViewHolder> {

    private Context context;
    private List<AccountModel> list;

    public AccountAdapter(List<AccountModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public AccountAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.account_adapter_layout, parent, false);

        return new AccountAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountAdapterViewHolder holder, int position) {

        AccountModel model = list.get(position);

        holder.qId.setText("Quiz ID: " + model.getqId());
        holder.qTitle.setText("Title: " + model.getqTitle());

        switch (model.getPending()){
            case "0":
                holder.pendingImage.setImageResource(R.drawable.wait);
                break;
            case "-1":
                holder.pendingImage.setImageResource(R.drawable.error);
                break;
            default:
                holder.pendingImage.setImageResource(R.drawable.wait);
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class AccountAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView qId, qTitle;
        ImageView pendingImage;

        public AccountAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            qId = itemView.findViewById(R.id.account_adapter_layoutQid);
            qTitle = itemView.findViewById(R.id.account_adapter_layoutQtitle);
            pendingImage = itemView.findViewById(R.id.account_adapter_layoutPendingImage);
        }
    }
}
