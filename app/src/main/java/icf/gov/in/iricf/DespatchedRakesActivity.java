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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static icf.gov.in.iricf.Utils.parseDateReceive;

public class DespatchedRakesActivity extends AppCompatActivity {

    public static final String TOKEN = "token";

    @BindView(R.id.despatch_rakes_card)
    CardView despatchCard;

    @BindView(R.id.despatch_rakes_rv)
    RecyclerView despatchRv;

    SharedPreferences preferences;
    ApiInterface apiInterface;
    String token;
    AlertDialog loadingDialog, coachesDialog;
    CoachStatusAdapter rakesAdapter, coachesAdapter;
    ArrayList<String> rakesList, coachNumList;
    List<RakeName> rakeList;
    List<CoachPerRake> coachPerRakesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despatched_rakes);
        ButterKnife.bind(this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        ((TextView) dialogView.findViewById(R.id.progressDialog_textView)).setText(R.string.loading);
        loadingDialog = new android.support.v7.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();
        loadingDialog.show();

        rakeList = new ArrayList<>();
        rakesList = new ArrayList<>();
        coachNumList = new ArrayList<>();
        coachPerRakesList = new ArrayList<>();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN, "");


        rakesAdapter = new CoachStatusAdapter(this, rakesList);

        despatchRv.setLayoutManager(new LinearLayoutManager(this));
        despatchRv.setAdapter(rakesAdapter);

        getDespatchedRakes();

        rakesAdapter.setOnClickListener(new CoachStatusAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                openCoaches(position);
            }
        });


    }

    private void openCoachesDialog(String railwayName, String rakenum, String rakedate) {


        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.despatched_coaches, null);
        dialogBuilder.setView(dialogView);
        coachesDialog = dialogBuilder.create();

        TextView rakeNumTv = dialogView.findViewById(R.id.despatch_coaches_dialog_rake);
        TextView dateTv = dialogView.findViewById(R.id.despatch_coaches_dialog_date);
        rakeNumTv.setText(String.format("%s%s", railwayName, rakenum));
        String temp = "Despatched on " + parseDateReceive(rakedate);
        dateTv.setText(temp);

        ImageView closeDialogButton = dialogView.findViewById(R.id.dialog_close_button);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coachesDialog.dismiss();
            }
        });

        coachesAdapter = new CoachStatusAdapter(this, coachNumList);
        RecyclerView coachesRv = dialogView.findViewById(R.id.despatch_coaches_dialog_rv);
        coachesRv.setLayoutManager(new LinearLayoutManager(this));
        coachesRv.setAdapter(coachesAdapter);

        coachesAdapter.setOnClickListener(new CoachStatusAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                //Do Nothing
            }
        });
    }

    private void openCoaches(int position) {

        String railwayName = rakeList.get(position).getRailway();
        String rakeNum = rakeList.get(position).getRakeNum();
        String rakeDate = rakeList.get(position).getDespatchDate().toString();
        openCoachesDialog(railwayName, rakeNum, rakeDate);

        loadingDialog.show();
        Call<CoachPerRakeRegister> call = apiInterface.getRakeCoaches(rakeNum, token);
        call.enqueue(new Callback<CoachPerRakeRegister>() {
            @Override
            public void onResponse(Call<CoachPerRakeRegister> call, Response<CoachPerRakeRegister> response) {

                int status = response.body().getStatus();
                if (status == 200) {
                    coachNumList.clear();
                    coachPerRakesList.clear();
                    CoachPerRakeRegister coachPerRakeRegister = response.body();
                    coachPerRakesList = coachPerRakeRegister.getCoaches();

                    for (int i = 0; i < coachPerRakesList.size(); i++) {
                        coachNumList.add(coachPerRakesList.get(i).getCoachNum());
                    }

                    coachesAdapter.notifyDataSetChanged();
                    coachesDialog.show();

                } else {
                    Toast.makeText(getApplicationContext(), "Error fetching data. Try again.", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();

            }

            @Override
            public void onFailure(Call<CoachPerRakeRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching data. Try again.", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void getDespatchedRakes() {

        loadingDialog.show();
        Call<RakeRegister> call = apiInterface.getDespatchRakes(token);

        call.enqueue(new Callback<RakeRegister>() {
            @Override
            public void onResponse(Call<RakeRegister> call, Response<RakeRegister> response) {

                Integer statusCode = response.body().getStatus();
                if (statusCode == 200) {
                    rakeList.clear();
                    RakeRegister rakeRegister = response.body();
                    rakeList = rakeRegister.getRakes();

                    for (int i = 0; i < rakeList.size(); i++) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(rakeList.get(i).getRailway());
                        sb.append(rakeList.get(i).getRakeNum());
                        String rakeName = sb.toString();
                        rakesList.add(rakeName);
                    }
                    rakesAdapter.notifyDataSetChanged();
                    despatchCard.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(getApplicationContext(), "Error fetching data. Try again.", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RakeRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching data. Try again.", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

    }
}
