package icf.gov.in.iricf;

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

import static icf.gov.in.iricf.Utils.sortList;

public class ProductionLineActivity extends AppCompatActivity {

    public static final String TOKEN = "token";

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

    ProdLineAdapter prodOneAdapter, prodTwoAdapter, prodThreeAdapter, prodFourAdapter, prodFiveAdapter, prodSixAdapter, prodSevenAdapter, prodEightAdapter, prodNineAdapter, prodTenAdapter,
            prodElevenAdapter, prodTwelveAdapter, prodThirteenAdapter;
    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token;
    List<Position> positionArrayList, productionList;
    List<StagePosition> stagePositionListOne, stagePositionListTwo, stagePositionListThree, stagePositionListFour,
            stagePositionListFive, stagePositionListSix, stagePositionListSeven, stagePositionListEight, stagePositionListNine,
            stagePositionListTen, stagePositionListEleven, stagePositionListTwelve, stagePositionListThirteen;
    AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_line);
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
        stagePositionListEleven = new ArrayList<>();
        stagePositionListTwelve = new ArrayList<>();
        stagePositionListThirteen = new ArrayList<>();
        fetchPositionData();

        prodOneAdapter = new ProdLineAdapter(this, stagePositionListOne);
        productionOneRv.setLayoutManager(new LinearLayoutManager(this));
        productionOneRv.setAdapter(prodOneAdapter);

        prodTwoAdapter = new ProdLineAdapter(this, stagePositionListTwo);
        productionTwoRv.setLayoutManager(new LinearLayoutManager(this));
        productionTwoRv.setAdapter(prodTwoAdapter);

        prodThreeAdapter = new ProdLineAdapter(this, stagePositionListThree);
        productionThreeRv.setLayoutManager(new LinearLayoutManager(this));
        productionThreeRv.setAdapter(prodThreeAdapter);

        prodFourAdapter = new ProdLineAdapter(this, stagePositionListFour);
        productionFourRv.setLayoutManager(new LinearLayoutManager(this));
        productionFourRv.setAdapter(prodFourAdapter);

        prodFiveAdapter = new ProdLineAdapter(this, stagePositionListFive);
        productionFiveRv.setLayoutManager(new LinearLayoutManager(this));
        productionFiveRv.setAdapter(prodFiveAdapter);

        prodSixAdapter = new ProdLineAdapter(this, stagePositionListSix);
        productionSixRv.setLayoutManager(new LinearLayoutManager(this));
        productionSixRv.setAdapter(prodSixAdapter);

        prodSevenAdapter = new ProdLineAdapter(this, stagePositionListSeven);
        productionSevenRv.setLayoutManager(new LinearLayoutManager(this));
        productionSevenRv.setAdapter(prodSevenAdapter);

        prodEightAdapter = new ProdLineAdapter(this, stagePositionListEight);
        productionEightRv.setLayoutManager(new LinearLayoutManager(this));
        productionEightRv.setAdapter(prodEightAdapter);

        prodNineAdapter = new ProdLineAdapter(this, stagePositionListNine);
        productionNineRv.setLayoutManager(new LinearLayoutManager(this));
        productionNineRv.setAdapter(prodNineAdapter);

        prodTenAdapter = new ProdLineAdapter(this, stagePositionListTen);
        productionTenRv.setLayoutManager(new LinearLayoutManager(this));
        productionTenRv.setAdapter(prodTenAdapter);

        prodElevenAdapter = new ProdLineAdapter(this, stagePositionListEleven);
        productionElevenRv.setLayoutManager(new LinearLayoutManager(this));
        productionElevenRv.setAdapter(prodElevenAdapter);

        prodTwelveAdapter = new ProdLineAdapter(this, stagePositionListTwelve);
        productionTwelveRv.setLayoutManager(new LinearLayoutManager(this));
        productionTwelveRv.setAdapter(prodTwelveAdapter);

        prodThirteenAdapter = new ProdLineAdapter(this, stagePositionListThirteen);
        productionThirteenRv.setLayoutManager(new LinearLayoutManager(this));
        productionThirteenRv.setAdapter(prodThirteenAdapter);
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
                        if (positionArrayList.get(i).getLineName().equalsIgnoreCase("production")) {
                            productionList.add(positionArrayList.get(i));
                        }
                    }
                    for (int j = 0; j < productionList.size(); j++) {
                        Integer lineNum = productionList.get(j).getLineNo();
                        if (lineNum != null) {
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
                                case 11:
                                    if (productionList.get(j).getStage() != null) {
                                        stagePositionListEleven.add(new StagePosition(productionList.get(j).getStage()
                                                , productionList.get(j).getCoachNum()));
                                    }
                                    break;
                                case 12:
                                    if (productionList.get(j).getStage() != null) {
                                        stagePositionListTwelve.add(new StagePosition(productionList.get(j).getStage()
                                                , productionList.get(j).getCoachNum()));
                                    }

                                    break;
                                case 13:
                                    if (productionList.get(j).getStage() != null) {
                                        stagePositionListThirteen.add(new StagePosition(productionList.get(j).getStage()
                                                , productionList.get(j).getCoachNum()));
                                    }
                                    break;
                                default:
                                    break;
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
