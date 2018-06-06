package icf.gov.in.iricf;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static icf.gov.in.iricf.Utils.sortList;

public class OutShedActivity extends AppCompatActivity {

    public static final String TOKEN = "token";

    @BindView(R.id.out_shed_rv)
    RecyclerView outShedRv;

    @BindView(R.id.out_shed_card)
    CardView cardView;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token;
    List<Position> positionArrayList, outShedList;
    //LineAdapters outShedAdapter;
    CoachStatusAdapter outShedAdapter;
    ArrayList<String> stagePositionList;

    //List<StagePosition> stagePositionList;
    AlertDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_shed);
        ButterKnife.bind(this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        ((TextView) dialogView.findViewById(R.id.progressDialog_textView)).setText(R.string.loading);
        loadingDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();
        loadingDialog.show();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN, "");

        positionArrayList = new ArrayList<>();
        outShedList = new ArrayList<>();
        stagePositionList = new ArrayList<>();
        fetchPositionData();

        outShedAdapter = new CoachStatusAdapter(this, stagePositionList);
        outShedRv.setLayoutManager(new LinearLayoutManager(this));
        outShedRv.setAdapter(outShedAdapter);
        outShedAdapter.setOnClickListener(new CoachStatusAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                //Do Nothing
            }
        });
    }

    private void fetchPositionData() {

        Call<PositionRegister> call = apiInterface.getAllPosition(token);
        call.enqueue(new Callback<PositionRegister>() {
            @Override
            public void onResponse(Call<PositionRegister> call, Response<PositionRegister> response) {

                int status = response.body().getStatus();
                if (status == 200) {
                    PositionRegister positionRegister = response.body();
                    positionArrayList = positionRegister.getPositionList();
                    for (int i = 0; i < positionArrayList.size(); i++) {
                        if (positionArrayList.get(i).getLineName().equalsIgnoreCase("out")) {
                            outShedList.add(positionArrayList.get(i));
                        }
                    }
                    for (int j = 0; j < outShedList.size(); j++) {
                        if (outShedList.get(j).getLineNo() != null) {
                            /*stagePositionList.add(new StagePosition(outShedList.get(j).getLineNo()
                                    , outShedList.get(j).getCoachNum()));*/
                            stagePositionList.add(outShedList.get(j).getCoachNum());
                        }

                    }
                    //sortList(stagePositionList);

                    outShedAdapter.notifyDataSetChanged();
                    cardView.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(getApplicationContext(), "Error getting positions. Try again later"
                            , Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PositionRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error getting positions. Try again later"
                        , Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

    }
}
