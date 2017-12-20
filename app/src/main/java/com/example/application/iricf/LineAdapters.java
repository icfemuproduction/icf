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

public class LineAdapters extends RecyclerView.Adapter<LineAdapters.ViewHolder> {

    Context context;
    ArrayList<String> coaches;

    public LineAdapters(Context context, ArrayList<String> coaches) {
        this.coaches = coaches;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_line,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.lineNumberTv.setText("Line " +  String.valueOf(position+1)+ " : ");
        holder.coachNameTv.setText(coaches.get(position));
    }

    @Override
    public int getItemCount() {
        return coaches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.single_line_section_tv)
        TextView lineNumberTv;

        @BindView(R.id.single_line_tv)
        TextView coachNameTv;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
