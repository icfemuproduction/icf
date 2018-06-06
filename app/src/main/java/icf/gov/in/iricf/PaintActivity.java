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

public class PaintActivity extends AppCompatActivity {

    public static final String TOKEN = "token";

    @BindView(R.id.paint_shop_rv)
    RecyclerView paintShopRv;

    @BindView(R.id.paint_shop_card)
    CardView cardView;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token;
    List<Position> positionArrayList, paintList;
    //LineAdapters paintAdapter;
    //List<StagePosition> stagePositionList;
    CoachStatusAdapter paintAdapter;
    ArrayList<String> stagePositionList;
    AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
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
        paintList = new ArrayList<>();
        stagePositionList = new ArrayList<>();
        fetchPositionData();

        paintAdapter = new CoachStatusAdapter(this, stagePositionList);
        paintShopRv.setLayoutManager(new LinearLayoutManager(this));
        paintShopRv.setAdapter(paintAdapter);
        paintAdapter.setOnClickListener(new CoachStatusAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                //Do nothing
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
                        if (positionArrayList.get(i).getLineName().equalsIgnoreCase("paint")) {
                            paintList.add(positionArrayList.get(i));
                        }
                    }
                    for (int j = 0; j < paintList.size(); j++) {
                        if (paintList.get(j).getLineNo() != null) {
                            /*stagePositionList.add(new StagePosition(paintList.get(j).getLineNo()
                                    , paintList.get(j).getCoachNum()));*/
                            stagePositionList.add(paintList.get(j).getCoachNum());
                        }
                    }
                    //sortList(stagePositionList);

                    paintAdapter.notifyDataSetChanged();
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
