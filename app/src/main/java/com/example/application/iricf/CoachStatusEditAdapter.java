package com.example.application.iricf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoachStatusEditAdapter extends RecyclerView.Adapter<CoachStatusEditAdapter.ViewHolder> {

    OnClickListener onClickListener;
    Context context;
    ArrayList<String> statusName,statusValue;

    public CoachStatusEditAdapter(Context context, ArrayList<String> statusName,ArrayList<String> statusValue) {
        this.context = context;
        this.statusName = statusName;
        this.statusValue = statusValue;
    }

    public void setOnClickListener(OnClickListener onClickListener){
        this.onClickListener = onClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_coach_status_edit,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.statusNameTv.setText(statusName.get(position));
        holder.statusValueTv.setText(statusValue.get(position));

        holder.statusEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.itemClicked(view,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return statusValue.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.status_name_tv)
        TextView statusNameTv;

        @BindView(R.id.status_value_tv)
        TextView statusValueTv;

        @BindView(R.id.status_edit_button)
        ImageView statusEditButton;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnClickListener{
        void itemClicked(View view,int position);
    }
}
