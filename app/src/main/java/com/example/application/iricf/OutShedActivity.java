package com.example.application.iricf;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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

public class OutShedActivity extends AppCompatActivity {

    public static final String TOKEN = "token";

    @BindView(R.id.out_shed_rv)
    RecyclerView outShedRv;

    @BindView(R.id.out_shed_card)
    CardView cardView;

    @BindView(R.id.out_shed_progress)
    ProgressBar progressBar;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token;
    List<Position> positionArrayList,outShedList;
    LineAdapters outShedAdapter;
    List<StagePosition> stagePositionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_shed);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");

        positionArrayList = new ArrayList<>();
        outShedList = new ArrayList<>();
        stagePositionList = new ArrayList<>();
        fetchPositionData();

        outShedAdapter = new LineAdapters(this,stagePositionList);
        outShedRv.setLayoutManager(new LinearLayoutManager(this));
        outShedRv.setAdapter(outShedAdapter);
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
                        if(positionArrayList.get(i).getLineName().equalsIgnoreCase("out")){
                            outShedList.add(positionArrayList.get(i));
                        }
                    }
                    for(int j=0 ; j<outShedList.size() ; j++){
                        stagePositionList.add(new StagePosition(outShedList.get(j).getLineNo()
                                ,outShedList.get(j).getCoachNum()));
                    }
                    sortList(stagePositionList);

                    outShedAdapter.notifyDataSetChanged();
                    cardView.setVisibility(View.VISIBLE);

                }else{
                    Toast.makeText(getApplicationContext(),"Error getting positions. Try again later"
                            ,Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<PositionRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error getting positions. Try again later"
                        ,Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void sortList(List<StagePosition> list) {
        Collections.sort(list, new Comparator<StagePosition>() {
            public int compare(StagePosition ideaVal1, StagePosition ideaVal2) {

                Integer idea1 = new Integer(ideaVal1.getStage());
                Integer idea2 = new Integer(ideaVal2.getStage());
                return idea1.compareTo(idea2);
            }
        });
    }
}
