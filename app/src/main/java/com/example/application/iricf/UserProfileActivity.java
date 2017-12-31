package com.example.application.iricf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TOKEN = "token";
    public static final String RAKE_NUM = "rakeNum";

    @BindView(R.id.create_rake_button)
    Button buttonCreateRake;

    @BindView(R.id.create_coach_button)
    Button buttonCreateCoach;

    @BindView(R.id.rakes_edit_rv)
    RecyclerView rakesEditRv;

    @BindView(R.id.rakes_edit_card)
    CardView rakesEditCard;

    SharedPreferences preferences;
    CoachStatusAdapter rakesAdapter;
    ArrayList<String> rakeNames;
    List<RakeName> rakeList;
    ApiInterface apiInterface;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        buttonCreateCoach.setOnClickListener(this);
        buttonCreateRake.setOnClickListener(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");
        Log.d("SAN","Token" + token);

        rakeNames = new ArrayList<>();
        rakeList = new ArrayList<>();
        Log.d("SAN","Getting Rakes");
        getRakes();

        rakesAdapter = new CoachStatusAdapter(this,rakeNames);

        rakesEditRv.setLayoutManager(new LinearLayoutManager(this));
        rakesEditRv.setAdapter(rakesAdapter);

        rakesAdapter.setOnClickListener(new CoachStatusAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                openCoaches(view,position);
            }
        });


    }

    private void openCoaches(View view,int position) {

        Intent coachesIntent = new Intent(getApplicationContext(),CoachesEdit.class);
        coachesIntent.putExtra(RAKE_NUM,rakeList.get(position).getRakeNum());
        startActivity(coachesIntent);

    }


    private void getRakes() {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);


        Call<RakeRegister> call = apiInterface.getRakes(token);

        call.enqueue(new Callback<RakeRegister>() {
            @Override
            public void onResponse(Call<RakeRegister> call, Response<RakeRegister> response) {
                RakeRegister rakeRegister = response.body();
                Log.d("SAN",response.body().toString());

                Integer statusCode = rakeRegister.getStatus();
                rakeList = rakeRegister.getRakes();
                for(int i=0 ; i<rakeList.size() ; i++){
                    StringBuilder sb = new StringBuilder();
                    sb.append(rakeList.get(i).getRailway());
                    sb.append(rakeList.get(i).getRakeNum());
                    String rakeName = sb.toString();
                    rakeNames.add(rakeName);
                }
                rakesAdapter.notifyDataSetChanged();
                rakesEditCard.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<RakeRegister> call, Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_rake_button:
                break;
            case R.id.create_coach_button:
                break;
        }
    }
}
