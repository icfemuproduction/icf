package com.example.application.iricf;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShellActivity extends AppCompatActivity {

    public static final String TOKEN = "token";

    @BindView(R.id.shell_received_rv)
    RecyclerView shellReceivedRv;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token;
    List<Position> positionArrayList;
    CoachStatusAdapter shellAdapter;
    ArrayList<String> shellNamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shell);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");

        positionArrayList = new ArrayList<>();
        shellNamesList = new ArrayList<>();
        fetchPositionData();

        shellAdapter = new CoachStatusAdapter(this,shellNamesList);
        shellReceivedRv.setLayoutManager(new LinearLayoutManager(this));
        shellReceivedRv.setAdapter(shellAdapter);
    }

    private void fetchPositionData() {

        Call<PositionRegister> call = apiInterface.getAllPosition(token);
        call.enqueue(new Callback<PositionRegister>() {
            @Override
            public void onResponse(Call<PositionRegister> call, Response<PositionRegister> response) {
                PositionRegister positionRegister = response.body();
                int status = positionRegister.getStatus();
                if(status == 200){
                    positionArrayList = positionRegister.getPositionList();
                    Log.e("SAN","total size : "+ positionArrayList.size());
                    for (int i=0 ; i<positionArrayList.size() ; i++){
                        if(positionArrayList.get(i).getLineName().equalsIgnoreCase("shell")){
                            shellNamesList.add(positionArrayList.get(i).getCoachNum());
                        }
                    }
                    shellAdapter.notifyDataSetChanged();

                }else{
                    Toast.makeText(getApplicationContext(),"Error getting positions. Try again later"
                            ,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PositionRegister> call, Throwable t) {

            }
        });

    }
}