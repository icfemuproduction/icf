package com.example.application.iricf;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ProdOneAdapter extends RecyclerView.Adapter<ProdOneAdapter.ViewHolder> {

    Context context;
    ArrayList<String> sections;

    public ProdOneAdapter(Context context, ArrayList<String> sections) {
        this.context = context;
        this.sections = sections;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_line,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.singleSection.setText("Section " + position + " : ");
        holder.singleText.setText(sections.get(position));
    }

    @Override
    public int getItemCount() {
        return sections.size();
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
