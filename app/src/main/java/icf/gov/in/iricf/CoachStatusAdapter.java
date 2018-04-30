package icf.gov.in.iricf;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoachStatusAdapter extends RecyclerView.Adapter<CoachStatusAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> coaches;
    private OnClickListener onClickListener;

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public CoachStatusAdapter(Context context, ArrayList<String> coaches) {
        this.coaches = coaches;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_coach, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.singleCoachTv.setText(coaches.get(position));

        holder.singleCoachTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.itemClicked(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return coaches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.single_coach_tv)
        TextView singleCoachTv;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnClickListener {
        void itemClicked(View view, int position);
    }


}
