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

public class DispatchActivity extends AppCompatActivity {

    public static final String TOKEN = "token";

    @BindView(R.id.dispatch_line_one_rv)
    RecyclerView dispatchLineOneRv;

    @BindView(R.id.dispatch_line_two_rv)
    RecyclerView dispatchLineTwoRv;

    @BindView(R.id.dispatch_line_three_rv)
    RecyclerView dispatchLineThreeRv;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token;
    List<Position> positionArrayList,dispatchList;
    ProdLineAdapter disOneAdapter,disTwoAdapter,disThreeAdapter;
    List<StagePosition> stagePositionListOne,stagePositionListTwo,stagePositionListThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");
        stagePositionListOne = new ArrayList<>();
        stagePositionListTwo = new ArrayList<>();
        stagePositionListThree = new ArrayList<>();
        positionArrayList = new ArrayList<>();
        fetchPositionData();

        disOneAdapter = new ProdLineAdapter(this,stagePositionListOne);
        dispatchLineOneRv.setLayoutManager(new LinearLayoutManager(this));
        dispatchLineOneRv.setAdapter(disOneAdapter);

        disTwoAdapter = new ProdLineAdapter(this,stagePositionListTwo);
        dispatchLineTwoRv.setLayoutManager(new LinearLayoutManager(this));
        dispatchLineTwoRv.setAdapter(disTwoAdapter);

        disThreeAdapter = new ProdLineAdapter(this,stagePositionListThree);
        dispatchLineThreeRv.setLayoutManager(new LinearLayoutManager(this));
        dispatchLineThreeRv.setAdapter(disThreeAdapter);

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
                    dispatchList = new ArrayList<>();
                    Log.e("SAN","total size : "+ positionArrayList.size());
                    for (int i=0 ; i<positionArrayList.size() ; i++){
                        if(positionArrayList.get(i).getLineName().equalsIgnoreCase("despatch")){
                            dispatchList.add(positionArrayList.get(i));
                        }
                    }
                    for(int j=0 ; j<dispatchList.size() ; j++){
                        switch (dispatchList.get(j).getLineNo()){
                            case 1:
                                stagePositionListOne.add(new StagePosition(dispatchList.get(j).getStage()
                                        ,dispatchList.get(j).getCoachNum()));
                                break;
                            case 2:
                                stagePositionListTwo.add(new StagePosition(dispatchList.get(j).getStage()
                                        ,dispatchList.get(j).getCoachNum()));
                                break;
                            case 3:
                                stagePositionListThree.add(new StagePosition(dispatchList.get(j).getStage()
                                        ,dispatchList.get(j).getCoachNum()));
                                break;
                        }
                    }
                    sortList(stagePositionListOne);
                    sortList(stagePositionListTwo);
                    sortList(stagePositionListThree);

                    disOneAdapter.notifyDataSetChanged();
                    disTwoAdapter.notifyDataSetChanged();
                    disThreeAdapter.notifyDataSetChanged();

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
