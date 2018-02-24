package com.example.application.iricf;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.application.iricf.Utils.sortList;

public class Shop36Activity extends AppCompatActivity {

    public static final String TOKEN = "token";

    @BindView(R.id.shop36_shed_layout)
    LinearLayout shop36layout;

    @BindView(R.id.shop36_line_one_rv)
    RecyclerView shop36OneRv;

    @BindView(R.id.shop36_line_two_rv)
    RecyclerView shop36TwoRv;

    @BindView(R.id.shop36_line_three_rv)
    RecyclerView shop36ThreeRv;

    @BindView(R.id.shop36_line_four_rv)
    RecyclerView shop36FourRv;

    @BindView(R.id.shop36_line_five_rv)
    RecyclerView shop36FiveRv;

    @BindView(R.id.shop36_line_six_rv)
    RecyclerView shop36SixRv;

    @BindView(R.id.shop36_line_seven_rv)
    RecyclerView shop36SevenRv;

    @BindView(R.id.shop36_line_eight_rv)
    RecyclerView shop36EightRv;

    @BindView(R.id.shop36_line_nine_rv)
    RecyclerView shop36NineRv;

    @BindView(R.id.shop36_line_ten_rv)
    RecyclerView shop36TenRv;


    ProdLineAdapter prodOneAdapter, prodTwoAdapter, prodThreeAdapter, prodFourAdapter, prodFiveAdapter,
            prodSixAdapter, prodSevenAdapter, prodEightAdapter, prodNineAdapter, prodTenAdapter;
    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token;
    List<Position> positionArrayList, productionList;
    List<StagePosition> stagePositionListOne, stagePositionListTwo, stagePositionListThree, stagePositionListFour,
            stagePositionListFive, stagePositionListSix, stagePositionListSeven, stagePositionListEight, stagePositionListNine,
            stagePositionListTen;
    AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop36);
        ButterKnife.bind(this);


        View dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        ((TextView) dialogView.findViewById(R.id.progressDialog_textView)).setText(R.string.loading);
        loadingDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN, "");

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

        fetchPositionData();

        prodOneAdapter = new ProdLineAdapter(this, stagePositionListOne);
        shop36OneRv.setLayoutManager(new LinearLayoutManager(this));
        shop36OneRv.setAdapter(prodOneAdapter);

        prodTwoAdapter = new ProdLineAdapter(this, stagePositionListTwo);
        shop36TwoRv.setLayoutManager(new LinearLayoutManager(this));
        shop36TwoRv.setAdapter(prodTwoAdapter);

        prodThreeAdapter = new ProdLineAdapter(this, stagePositionListThree);
        shop36ThreeRv.setLayoutManager(new LinearLayoutManager(this));
        shop36ThreeRv.setAdapter(prodThreeAdapter);

        prodFourAdapter = new ProdLineAdapter(this, stagePositionListFour);
        shop36FourRv.setLayoutManager(new LinearLayoutManager(this));
        shop36FourRv.setAdapter(prodFourAdapter);

        prodFiveAdapter = new ProdLineAdapter(this, stagePositionListFive);
        shop36FiveRv.setLayoutManager(new LinearLayoutManager(this));
        shop36FiveRv.setAdapter(prodFiveAdapter);

        prodSixAdapter = new ProdLineAdapter(this, stagePositionListSix);
        shop36SixRv.setLayoutManager(new LinearLayoutManager(this));
        shop36SixRv.setAdapter(prodSixAdapter);

        prodSevenAdapter = new ProdLineAdapter(this, stagePositionListSeven);
        shop36SevenRv.setLayoutManager(new LinearLayoutManager(this));
        shop36SevenRv.setAdapter(prodSevenAdapter);

        prodEightAdapter = new ProdLineAdapter(this, stagePositionListEight);
        shop36EightRv.setLayoutManager(new LinearLayoutManager(this));
        shop36EightRv.setAdapter(prodEightAdapter);

        prodNineAdapter = new ProdLineAdapter(this, stagePositionListNine);
        shop36NineRv.setLayoutManager(new LinearLayoutManager(this));
        shop36NineRv.setAdapter(prodNineAdapter);

        prodTenAdapter = new ProdLineAdapter(this, stagePositionListTen);
        shop36TenRv.setLayoutManager(new LinearLayoutManager(this));
        shop36TenRv.setAdapter(prodTenAdapter);


    }

    private void fetchPositionData() {

        loadingDialog.show();
        Call<PositionRegister> call = apiInterface.getAllPosition(token);
        call.enqueue(new Callback<PositionRegister>() {
            @Override
            public void onResponse(Call<PositionRegister> call, Response<PositionRegister> response) {

                int status = response.body().getStatus();
                if (status == 200) {
                    PositionRegister positionRegister = response.body();
                    positionArrayList = positionRegister.getPositionList();
                    productionList = new ArrayList<>();
                    for (int i = 0; i < positionArrayList.size(); i++) {
                        if (positionArrayList.get(i).getLineName().equalsIgnoreCase("shop36")) {
                            productionList.add(positionArrayList.get(i));
                        }
                    }
                    for (int j = 0; j < productionList.size(); j++) {
                        Integer lineNum = productionList.get(j).getLineNo();
                        if (lineNum != null) {
                            try {
                                switch (productionList.get(j).getLineNo()) {
                                    case 1:
                                        if (productionList.get(j).getStage() != null) {
                                            stagePositionListOne.add(new StagePosition(productionList.get(j).getStage()
                                                    , productionList.get(j).getCoachNum()));
                                        }
                                        break;
                                    case 2:
                                        if (productionList.get(j).getStage() != null) {
                                            stagePositionListTwo.add(new StagePosition(productionList.get(j).getStage()
                                                    , productionList.get(j).getCoachNum()));
                                        }
                                        break;
                                    case 3:
                                        if (productionList.get(j).getStage() != null) {
                                            stagePositionListThree.add(new StagePosition(productionList.get(j).getStage()
                                                    , productionList.get(j).getCoachNum()));
                                        }
                                        break;
                                    case 4:
                                        if (productionList.get(j).getStage() != null) {
                                            stagePositionListFour.add(new StagePosition(productionList.get(j).getStage()
                                                    , productionList.get(j).getCoachNum()));
                                        }
                                        break;
                                    case 5:
                                        if (productionList.get(j).getStage() != null) {
                                            stagePositionListFive.add(new StagePosition(productionList.get(j).getStage()
                                                    , productionList.get(j).getCoachNum()));
                                        }
                                        break;
                                    case 6:
                                        if (productionList.get(j).getStage() != null) {
                                            stagePositionListSix.add(new StagePosition(productionList.get(j).getStage()
                                                    , productionList.get(j).getCoachNum()));
                                        }
                                        break;
                                    case 7:
                                        if (productionList.get(j).getStage() != null) {
                                            stagePositionListSeven.add(new StagePosition(productionList.get(j).getStage()
                                                    , productionList.get(j).getCoachNum()));
                                        }
                                        break;
                                    case 8:
                                        if (productionList.get(j).getStage() != null) {
                                            stagePositionListEight.add(new StagePosition(productionList.get(j).getStage()
                                                    , productionList.get(j).getCoachNum()));
                                        }
                                        break;
                                    case 9:
                                        if (productionList.get(j).getStage() != null) {
                                            stagePositionListNine.add(new StagePosition(productionList.get(j).getStage()
                                                    , productionList.get(j).getCoachNum()));
                                        }
                                        break;
                                    case 10:
                                        if (productionList.get(j).getStage() != null) {
                                            stagePositionListTen.add(new StagePosition(productionList.get(j).getStage()
                                                    , productionList.get(j).getCoachNum()));
                                        }
                                        break;
                                    default:
                                        break;

                                }
                            } catch (NullPointerException e) {

                            }


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

                    shop36layout.setVisibility(View.VISIBLE);

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
