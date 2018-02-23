package com.example.application.iricf;

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
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class CommissionActivity extends AppCompatActivity {

    public static final String TOKEN = "token";

    @BindView(R.id.commission_line_one_rv)
    RecyclerView commissionLineOneRv;

    @BindView(R.id.commission_line_two_rv)
    RecyclerView commissionLineTwoRv;

    @BindView(R.id.commission_line_three_rv)
    RecyclerView commissionLineThreeRv;

    @BindView(R.id.commission_shed_layout)
    LinearLayout commissionShedLayout;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token;
    List<Position> positionArrayList,commissionList;
    ProdLineAdapter disOneAdapter,disTwoAdapter,disThreeAdapter;
    List<StagePosition> stagePositionListOne,stagePositionListTwo,stagePositionListThree;
    AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission);
        ButterKnife.bind(this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        ((TextView) dialogView.findViewById(R.id.progressDialog_textView)).setText(R.string.loading);
        loadingDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");

        stagePositionListOne = new ArrayList<>();
        stagePositionListTwo = new ArrayList<>();
        stagePositionListThree = new ArrayList<>();
        positionArrayList = new ArrayList<>();
        fetchPositionData();

        disOneAdapter = new ProdLineAdapter(this,stagePositionListOne);
        commissionLineOneRv.setLayoutManager(new LinearLayoutManager(this));
        commissionLineOneRv.setAdapter(disOneAdapter);

        disTwoAdapter = new ProdLineAdapter(this,stagePositionListTwo);
        commissionLineTwoRv.setLayoutManager(new LinearLayoutManager(this));
        commissionLineTwoRv.setAdapter(disTwoAdapter);

        disThreeAdapter = new ProdLineAdapter(this,stagePositionListThree);
        commissionLineThreeRv.setLayoutManager(new LinearLayoutManager(this));
        commissionLineThreeRv.setAdapter(disThreeAdapter);

    }

    private void fetchPositionData() {

        loadingDialog.show();
        Call<PositionRegister> call = apiInterface.getAllPosition(token);
        call.enqueue(new Callback<PositionRegister>() {
            @Override
            public void onResponse(Call<PositionRegister> call, Response<PositionRegister> response) {

                int status = response.body().getStatus();
                if(status == 200){
                    PositionRegister positionRegister = response.body();
                    positionArrayList = positionRegister.getPositionList();
                    commissionList = new ArrayList<>();
                    for (int i=0 ; i<positionArrayList.size() ; i++){
                        if(positionArrayList.get(i).getLineName().equalsIgnoreCase("despatch")){
                            commissionList.add(positionArrayList.get(i));
                        }
                    }
                    for(int j=0 ; j<commissionList.size() ; j++){
                        if(commissionList.get(j).getLineNo() != null){
                            switch (commissionList.get(j).getLineNo()){
                                case 1:
                                    stagePositionListOne.add(new StagePosition(commissionList.get(j).getStage()
                                            ,commissionList.get(j).getCoachNum()));
                                    break;
                                case 2:
                                    stagePositionListTwo.add(new StagePosition(commissionList.get(j).getStage()
                                            ,commissionList.get(j).getCoachNum()));
                                    break;
                                case 3:
                                    stagePositionListThree.add(new StagePosition(commissionList.get(j).getStage()
                                            ,commissionList.get(j).getCoachNum()));
                                    break;
                                default:
                                    break;
                            }
                        }

                    }
                    sortList(stagePositionListOne);
                    sortList(stagePositionListTwo);
                    sortList(stagePositionListThree);

                    disOneAdapter.notifyDataSetChanged();
                    disTwoAdapter.notifyDataSetChanged();
                    disThreeAdapter.notifyDataSetChanged();

                    commissionShedLayout.setVisibility(View.VISIBLE);

                }else{
                    Toast.makeText(getApplicationContext(),"Error getting positions. Try again later"
                            ,Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();

            }

            @Override
            public void onFailure(Call<PositionRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error getting positions. Try again later"
                        ,Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

    }

    private void sortList(List<StagePosition> list) {
        Collections.sort(list, new Comparator<StagePosition>() {
            public int compare(StagePosition ideaVal1, StagePosition ideaVal2) {

                Integer idea1 = ideaVal1.getStage();
                Integer idea2 = ideaVal2.getStage();
                return idea1.compareTo(idea2);
            }
        });
    }
}
