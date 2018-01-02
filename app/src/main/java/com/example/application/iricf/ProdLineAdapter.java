package com.example.application.iricf;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ProdLineAdapter extends RecyclerView.Adapter<ProdLineAdapter.ViewHolder> {

    Context context;
    List<StagePosition> positionArrayList;

    public ProdLineAdapter(Context context, List<StagePosition> positionArrayList) {
        this.context = context;
        this.positionArrayList = positionArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_line,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.singleSection.setText("Stage " + positionArrayList.get(position).getStage()+ " : ");
        holder.singleText.setText(positionArrayList.get(position).getCoachNum());
    }

    @Override
    public int getItemCount() {
        return positionArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView singleText,singleSection;

        public ViewHolder(View itemView) {
            super(itemView);

            singleText = itemView.findViewById(R.id.single_line_tv);
            singleSection = itemView.findViewById(R.id.single_line_section_tv);
        }
    }
}
