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

public class StatusPropertyAdapter extends RecyclerView.Adapter<StatusPropertyAdapter.ViewHolder>{

    Context context;
    ArrayList<Property> propertyArrayList;

    public StatusPropertyAdapter(Context context, ArrayList<Property> propertyArrayList) {
        this.context = context;
        this.propertyArrayList = propertyArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_line,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.propertyTv.setText(propertyArrayList.get(position).getProperty() + " : ");
        holder.valueTv.setText(propertyArrayList.get(position).getValue());

    }

    @Override
    public int getItemCount() {
        return propertyArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.single_line_section_tv)
        TextView propertyTv;

        @BindView(R.id.single_line_tv)
        TextView valueTv;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }
}
