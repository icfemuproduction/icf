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

public class PaintActivity extends AppCompatActivity {

    public static final String TOKEN = "token";

    @BindView(R.id.paint_shop_rv)
    RecyclerView paintShopRv;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token;
    List<Position> positionArrayList,paintList;
    LineAdapters paintAdapter;
    List<StagePosition> stagePositionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");

        positionArrayList = new ArrayList<>();
        paintList = new ArrayList<>();
        stagePositionList = new ArrayList<>();
        fetchPositionData();

        paintAdapter = new LineAdapters(this,stagePositionList);
        paintShopRv.setLayoutManager(new LinearLayoutManager(this));
        paintShopRv.setAdapter(paintAdapter);
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
                        if(positionArrayList.get(i).getLineName().equalsIgnoreCase("paint")){
                            paintList.add(positionArrayList.get(i));
                        }
                    }
                    for(int j=0 ; j<paintList.size() ; j++){
                        stagePositionList.add(new StagePosition(paintList.get(j).getLineNo()
                                ,paintList.get(j).getCoachNum()));
                    }
                    sortList(stagePositionList);

                    paintAdapter.notifyDataSetChanged();

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