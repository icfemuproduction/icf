package com.example.application.iricf;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinePositionActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TOKEN = "token";

    @BindView(R.id.production_line_button)
    Button productionLineButton;

    @BindView(R.id.despatch_shed_button)
    Button dispatchShedButton;

    @BindView(R.id.outshed_button)
    Button outShedButton;

    @BindView(R.id.paint_shop_button)
    Button paintShopButton;

    @BindView(R.id.commission_shed_button)
    Button commissionShedButton;

    @BindView(R.id.shell_recieved_button)
    Button shellReceivedButton;

    @BindView(R.id.coach_search_bar)
    AutoCompleteTextView coachSearchBar;

    @BindView(R.id.get_coach_position_button)
    ImageView getCoachPositionButton;

    @BindView(R.id.line_position_progress)
    ProgressBar progressBar;

    SharedPreferences preferences;
    String token;
    ApiInterface apiInterface;
    List<Position> positionArrayList,productionList,commissionList,despatchList,paintList,outList,shellList;
    AlertDialog positionDialog;
    TextView showCoachLineName,showCoachLineNo,showCoachLineStage;
    Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        ButterKnife.bind(this);

        positionArrayList = new ArrayList<>();
        productionList = new ArrayList<>();
        commissionList = new ArrayList<>();
        despatchList = new ArrayList<>();
        paintList = new ArrayList<>();
        outList = new ArrayList<>();
        shellList = new ArrayList<>();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");


        productionLineButton.setOnClickListener(this);
        dispatchShedButton.setOnClickListener(this);
        outShedButton.setOnClickListener(this);
        paintShopButton.setOnClickListener(this);
        commissionShedButton.setOnClickListener(this);
        shellReceivedButton.setOnClickListener(this);
        getCoachPositionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.production_line_button:
                Intent prodIntent = new Intent(LinePositionActivity.this,ProductionLineActivity.class);
                startActivity(prodIntent);
                break;
            case R.id.despatch_shed_button:
                startActivity(new Intent(LinePositionActivity.this,DispatchActivity.class));
                break;
            case R.id.paint_shop_button:
                startActivity(new Intent(LinePositionActivity.this,PaintActivity.class));
                break;
            case R.id.outshed_button:
                startActivity(new Intent(LinePositionActivity.this,OutShedActivity.class));
                break;
            case R.id.shell_recieved_button:
                startActivity(new Intent(LinePositionActivity.this,ShellActivity.class));
                break;
            case R.id.commission_shed_button:
                startActivity(new Intent(LinePositionActivity.this,CommissionActivity.class));
                break;
            case R.id.get_coach_position_button:
                getCoachPosition();
                break;
        }
    }

    private void getCoachPosition() {

        String coachNum = coachSearchBar.getText().toString().trim();
        if(coachNum.isEmpty()){
            coachSearchBar.setError("Coach Number Required");
            coachSearchBar.requestFocus();
            return;
        }
        for (int i=0 ; i<coachNum.length() ; i++){
            if(coachNum.charAt(i) =='/'){
                coachNum = coachNum.substring(0,i)+'_'+coachNum.substring(i+1);
            }
        }
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(coachSearchBar.getWindowToken(), 0);

        progressBar.setVisibility(View.VISIBLE);
        Call<CoachPositionRegister> call = apiInterface.getCoachPosition(coachNum,token);
        call.enqueue(new Callback<CoachPositionRegister>() {
            @Override
            public void onResponse(Call<CoachPositionRegister> call, Response<CoachPositionRegister> response) {
                int status = response.body().getStatus();

                if(status == 200){
                    CoachPositionRegister positionRegister = response.body();
                    Position position = positionRegister.getPosition();
                    String lineName=null,lineNo=null,lineStage=null;
                    if(position.getLineName() != null){
                        lineName = position.getLineName().toLowerCase();
                    }
                    if(position.getLineNo() != null){
                       lineNo = String.valueOf(position.getLineNo());
                    }
                    if(position.getStage() != null){
                        lineStage = String.valueOf(position.getStage());
                    }
                    createDialog(lineName,lineNo,lineStage);
                    coachSearchBar.setText("");
                }else {
                    Toast.makeText(getApplicationContext(),"Error fetching data. Try Again.",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<CoachPositionRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error fetching data. Try Again.",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    private void createDialog(String lineName,String lineNo,String lineStage){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.show_position_dialog, null);
        dialogBuilder.setView(dialogView);
        positionDialog = dialogBuilder.create();
        positionDialog.show();

        showCoachLineName = dialogView.findViewById(R.id.show_coach_line_name);
        showCoachLineNo = dialogView.findViewById(R.id.show_coach_line_no);
        showCoachLineStage = dialogView.findViewById(R.id.show_coach_line_stage);
        okButton = dialogView.findViewById(R.id.show_coach_position_ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positionDialog.dismiss();
            }
        });

        if(lineName != null){
            switch (lineName){
                case "production":showCoachLineName.setText("Production Line");
                    break;
                case "commission":showCoachLineName.setText("Commission Shed");
                    break;
                case "out":showCoachLineName.setText("Out Shed");
                    break;
                case "paint":showCoachLineName.setText("Paint Shop");
                    break;
                case "despatch":showCoachLineName.setText("Dispatch Shed");
                    break;
                case "shell":showCoachLineName.setText("In Shells Received");
                    break;
            }

        }
        if(lineNo != null){
            showCoachLineNo.setText("Line No: " + lineNo);
        }
        if(lineStage != null){
            showCoachLineStage.setText("Stage No: " +lineStage);
        }


    }

}
