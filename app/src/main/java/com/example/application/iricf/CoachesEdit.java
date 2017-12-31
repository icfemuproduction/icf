package com.example.application.iricf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoachesEdit extends AppCompatActivity {

    public static final String TOKEN = "token";
    public static final String RAKE_NUM = "rakeNum";
    public static final String COACH_NUM = "coachNum";

    @BindView(R.id.coach_per_rake_edit_rv)
    RecyclerView coachPerRakeRv;

    @BindView(R.id.coach_per_rake_edit_card)
    CardView coachPerRakeCard;

    @BindView(R.id.rakeNameTextView)
    TextView rakeNameTv;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    CoachStatusAdapter coachesAdapter;
    List<CoachPerRake> coachPerRakesList;
    ArrayList<String> coachNum;
    String token;
    String rakeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaches_edit);
        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");

        Bundle bundle = getIntent().getExtras();
        rakeNum = bundle.getString(RAKE_NUM);
        rakeNameTv.setText(rakeNum);

        coachPerRakesList = new ArrayList<>();
        coachNum = new ArrayList<>();
        getCoaches(rakeNum);

        coachesAdapter = new CoachStatusAdapter(this,coachNum);
        coachPerRakeRv.setLayoutManager(new LinearLayoutManager(this));
        coachPerRakeRv.setAdapter(coachesAdapter);

        coachesAdapter.setOnClickListener(new CoachStatusAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {

                editStatus(view,position);
            }
        });
    }

    private void editStatus(View view,int position) {
        Intent intent = new Intent(getApplicationContext(),EditStatusActivity.class);
        intent.putExtra(COACH_NUM,coachNum.get(position));
        startActivity(intent);
    }

    private void getCoaches(String rakeNum) {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<CoachPerRakeRegister> call = apiInterface.getRakeCoaches(rakeNum,token);
        call.enqueue(new Callback<CoachPerRakeRegister>() {
            @Override
            public void onResponse(Call<CoachPerRakeRegister> call, Response<CoachPerRakeRegister> response) {
                CoachPerRakeRegister coachPerRakeRegister = response.body();

                coachPerRakesList = coachPerRakeRegister.getCoaches();

                for(int i=0; i<coachPerRakesList.size() ; i++){
                    coachNum.add(coachPerRakesList.get(i).getCoachNum());
                }

                coachesAdapter.notifyDataSetChanged();
                coachPerRakeCard.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<CoachPerRakeRegister> call, Throwable t) {

            }
        });



    }
}
