package com.sumit.gymmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sumit.gymmanagement.R;
import com.sumit.gymmanagement.model.FeesDataModel;

import java.util.ArrayList;

public class FeesAdapter extends RecyclerView.Adapter<FeesAdapter.ViewHolder> {
    Context context;
    ArrayList<FeesDataModel> list;


    public FeesAdapter(Context context, ArrayList<FeesDataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FeesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fees_recycler_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FeesAdapter.ViewHolder holder, int position) {
        holder.tv_date.setText(list.get(position).date);
        holder.tv_amount.setText(list.get(position).amount);
        holder.tv_name.setText(list.get(position).member_name);
        holder.tv_txntype.setText(list.get(position).payment_type);
        holder.tv_month.setText(list.get(position).fees_month);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_date, tv_amount, tv_name, tv_txntype, tv_month;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_date = itemView.findViewById(R.id.tv_date);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_txntype = itemView.findViewById(R.id.tv_txntype);
            tv_month = itemView.findViewById(R.id.tv_month);

        }
    }
}
