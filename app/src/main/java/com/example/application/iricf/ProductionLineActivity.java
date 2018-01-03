package com.example.application.iricf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

public class ProductionLineActivity extends AppCompatActivity {

    public static final String TOKEN = "token";
    public static final String PRODUCTION_LIST = "production_list";
    public static final String BUNDLE = "bundle";

    @BindView(R.id.production_one_rv)
    RecyclerView productionOneRv;

    @BindView(R.id.production_two_rv)
    RecyclerView productionTwoRv;

    @BindView(R.id.production_three_rv)
    RecyclerView productionThreeRv;

    @BindView(R.id.production_four_rv)
    RecyclerView productionFourRv;

    @BindView(R.id.production_five_rv)
    RecyclerView productionFiveRv;

    @BindView(R.id.production_six_rv)
    RecyclerView productionSixRv;

    @BindView(R.id.production_seven_rv)
    RecyclerView productionSevenRv;

    @BindView(R.id.production_eight_rv)
    RecyclerView productionEightRv;

    @BindView(R.id.production_nine_rv)
    RecyclerView productionNineRv;

    @BindView(R.id.production_ten_rv)
    RecyclerView productionTenRv;

    @BindView(R.id.production_eleven_rv)
    RecyclerView productionElevenRv;

    @BindView(R.id.production_twelve_rv)
    RecyclerView productionTwelveRv;

    @BindView(R.id.production_thirteen_rv)
    RecyclerView productionThirteenRv;

    @BindView(R.id.production_line_layout)
    LinearLayout productionLineLayout;

    @BindView(R.id.production_line_progress)
    ProgressBar progressBar;

    ProdLineAdapter prodOneAdapter,prodTwoAdapter,prodThreeAdapter,prodFourAdapter,prodFiveAdapter
            ,prodSixAdapter,prodSevenAdapter,prodEightAdapter,prodNineAdapter,prodTenAdapter,
            prodElevenAdapter,prodTwelveAdapter,prodThirteenAdapter;
    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token;
    List<Position> positionArrayList,productionList;
    List<StagePosition> stagePositionListOne,stagePositionListTwo,stagePositionListThree,stagePositionListFour,
            stagePositionListFive,stagePositionListSix,stagePositionListSeven,stagePositionListEight,stagePositionListNine,
            stagePositionListTen,stagePositionListEleven,stagePositionListTwelve,stagePositionListThirteen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_line);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");
        stagePositionListOne = new ArrayList<>();
        stagePositionListTwo = new ArrayList<>();
        stagePositionListThree = new ArrayList<>();
        stagePositionListFour = new ArrayList<>();
        stagePositionListFive = new ArrayList<>();
        stagePositionListSix = new ArrayList<>();
        stagePositionListSeven = new ArrayList<>();
        stagePositionListEight = new ArrayList<>();
        stagePositionListNine = new ArrayList<>();
        stagePositionListTen = new ArrayList<>();
        stagePositionListEleven = new ArrayList<>();
        stagePositionListTwelve = new ArrayList<>();
        stagePositionListThirteen = new ArrayList<>();
        fetchPositionData();

        prodOneAdapter = new ProdLineAdapter(this,stagePositionListOne);
        productionOneRv.setLayoutManager(new LinearLayoutManager(this));
        productionOneRv.setAdapter(prodOneAdapter);

        prodTwoAdapter = new ProdLineAdapter(this,stagePositionListTwo);
        productionTwoRv.setLayoutManager(new LinearLayoutManager(this));
        productionTwoRv.setAdapter(prodTwoAdapter);

        prodThreeAdapter = new ProdLineAdapter(this,stagePositionListThree);
        productionThreeRv.setLayoutManager(new LinearLayoutManager(this));
        productionThreeRv.setAdapter(prodThreeAdapter);

        prodFourAdapter = new ProdLineAdapter(this,stagePositionListFour);
        productionFourRv.setLayoutManager(new LinearLayoutManager(this));
        productionFourRv.setAdapter(prodFourAdapter);

        prodFiveAdapter = new ProdLineAdapter(this,stagePositionListFive);
        productionFiveRv.setLayoutManager(new LinearLayoutManager(this));
        productionFiveRv.setAdapter(prodFiveAdapter);

        prodSixAdapter = new ProdLineAdapter(this,stagePositionListSix);
        productionSixRv.setLayoutManager(new LinearLayoutManager(this));
        productionSixRv.setAdapter(prodSixAdapter);

        prodSevenAdapter = new ProdLineAdapter(this,stagePositionListSeven);
        productionSevenRv.setLayoutManager(new LinearLayoutManager(this));
        productionSevenRv.setAdapter(prodSevenAdapter);

        prodEightAdapter = new ProdLineAdapter(this,stagePositionListEight);
        productionEightRv.setLayoutManager(new LinearLayoutManager(this));
        productionEightRv.setAdapter(prodEightAdapter);

        prodNineAdapter = new ProdLineAdapter(this,stagePositionListNine);
        productionNineRv.setLayoutManager(new LinearLayoutManager(this));
        productionNineRv.setAdapter(prodNineAdapter);

        prodTenAdapter = new ProdLineAdapter(this,stagePositionListTen);
        productionTenRv.setLayoutManager(new LinearLayoutManager(this));
        productionTenRv.setAdapter(prodTenAdapter);

        prodElevenAdapter = new ProdLineAdapter(this,stagePositionListEleven);
        productionElevenRv.setLayoutManager(new LinearLayoutManager(this));
        productionElevenRv.setAdapter(prodElevenAdapter);

        prodTwelveAdapter = new ProdLineAdapter(this,stagePositionListTwelve);
        productionTwelveRv.setLayoutManager(new LinearLayoutManager(this));
        productionTwelveRv.setAdapter(prodTwelveAdapter);

        prodThirteenAdapter = new ProdLineAdapter(this,stagePositionListThirteen);
        productionThirteenRv.setLayoutManager(new LinearLayoutManager(this));
        productionThirteenRv.setAdapter(prodThirteenAdapter);
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
                    productionList = new ArrayList<>();
                    Log.e("SAN","total size : "+ positionArrayList.size());
                    for (int i=0 ; i<positionArrayList.size() ; i++){
                        if(positionArrayList.get(i).getLineName().equalsIgnoreCase("production")){
                            productionList.add(positionArrayList.get(i));
                        }
                    }
                    for(int j=0 ; j<productionList.size() ; j++){
                        switch (productionList.get(j).getLineNo()){
                            case 1:
                                stagePositionListOne.add(new StagePosition(productionList.get(j).getStage()
                                        ,productionList.get(j).getCoachNum()));
                                break;
                            case 2:
                                stagePositionListTwo.add(new StagePosition(productionList.get(j).getStage()
                                        ,productionList.get(j).getCoachNum()));
                                break;
                            case 3:
                                stagePositionListThree.add(new StagePosition(productionList.get(j).getStage()
                                    ,productionList.get(j).getCoachNum()));
                                break;
                            case 4:
                                stagePositionListFour.add(new StagePosition(productionList.get(j).getStage()
                                        ,productionList.get(j).getCoachNum()));
                                break;
                            case 5:
                                stagePositionListFive.add(new StagePosition(productionList.get(j).getStage()
                                        ,productionList.get(j).getCoachNum()));
                                break;
                            case 6:
                                stagePositionListSix.add(new StagePosition(productionList.get(j).getStage()
                                        ,productionList.get(j).getCoachNum()));
                                break;
                            case 7:
                                stagePositionListSeven.add(new StagePosition(productionList.get(j).getStage()
                                        ,productionList.get(j).getCoachNum()));
                                break;
                            case 8:
                                stagePositionListEight.add(new StagePosition(productionList.get(j).getStage()
                                        ,productionList.get(j).getCoachNum()));
                                break;
                            case 9:
                                stagePositionListNine.add(new StagePosition(productionList.get(j).getStage()
                                        ,productionList.get(j).getCoachNum()));
                                break;
                            case 10:
                                stagePositionListTen.add(new StagePosition(productionList.get(j).getStage()
                                        ,productionList.get(j).getCoachNum()));
                                break;
                            case 11:
                                stagePositionListEleven.add(new StagePosition(productionList.get(j).getStage()
                                        ,productionList.get(j).getCoachNum()));
                                break;
                            case 12:
                                stagePositionListTwelve.add(new StagePosition(productionList.get(j).getStage()
                                        ,productionList.get(j).getCoachNum()));
                                break;
                            case 13:
                                stagePositionListThirteen.add(new StagePosition(productionList.get(j).getStage()
                                        ,productionList.get(j).getCoachNum()));
                                break;

                        }
                    }
                    sortList(stagePositionListOne);
                    sortList(stagePositionListTwo);
                    sortList(stagePositionListThree);
                    sortList(stagePositionListFour);
                    sortList(stagePositionListFive);
                    sortList(stagePositionListSix);
                    sortList(stagePositionListSeven);
                    sortList(stagePositionListEight);
                    sortList(stagePositionListNine);
                    sortList(stagePositionListTen);
                    sortList(stagePositionListEleven);
                    sortList(stagePositionListTwelve);
                    sortList(stagePositionListThirteen);

                    prodOneAdapter.notifyDataSetChanged();
                    prodTwoAdapter.notifyDataSetChanged();
                    prodThreeAdapter.notifyDataSetChanged();
                    prodFourAdapter.notifyDataSetChanged();
                    prodFiveAdapter.notifyDataSetChanged();
                    prodSixAdapter.notifyDataSetChanged();
                    prodSevenAdapter.notifyDataSetChanged();
                    prodEightAdapter.notifyDataSetChanged();
                    prodNineAdapter.notifyDataSetChanged();
                    prodTenAdapter.notifyDataSetChanged();
                    prodElevenAdapter.notifyDataSetChanged();
                    prodTwelveAdapter.notifyDataSetChanged();
                    prodThirteenAdapter.notifyDataSetChanged();
                    productionLineLayout.setVisibility(View.VISIBLE);

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
