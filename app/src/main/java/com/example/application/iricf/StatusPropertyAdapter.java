package com.example.application.iricf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatusPropertyAdapter extends RecyclerView.Adapter<StatusPropertyAdapter.ViewHolder> {

    Context context;
    ArrayList<String> statusName, statusValue;

    public StatusPropertyAdapter(Context context, ArrayList<String> statusName, ArrayList<String> statusValue) {
        this.context = context;
        this.statusName = statusName;
        this.statusValue = statusValue;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_line, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.propertyTv.setText(statusName.get(position));
        holder.valueTv.setText(statusValue.get(position));

    }

    @Override
    public int getItemCount() {
        return statusName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.single_line_section_tv)
        TextView propertyTv;

        @BindView(R.id.single_line_tv)
        TextView valueTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
