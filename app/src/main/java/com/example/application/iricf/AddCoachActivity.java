package com.example.application.iricf;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCoachActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String TOKEN = "token";
    public static final String RAKE_NUM = "rakeNum";


    @BindView(R.id.add_coach_card)
    CardView addCoachCard;

    @BindView(R.id.add_coach_position_card)
    CardView addCoachPositionCard;

    @BindView(R.id.add_coach_status_card)
    CardView addCoachStatusCard;

    @BindView(R.id.add_coach_num_et)
    EditText coachNumberEt;

    @BindView(R.id.add_coach_type_spinner)
    Spinner coachTypeSpinner;

    @BindView(R.id.add_coach_button)
    Button addCoachButton;

    @BindView(R.id.add_coach_position_spinner)
    Spinner coachPositionSpinner;

    @BindView(R.id.add_coach_position_line_et)
    EditText coachLineEt;

    @BindView(R.id.add_coach_position_stage_et)
    EditText coachStageEt;

    @BindView(R.id.add_coach_position_button)
    Button addCoachPositionButton;

    @BindView(R.id.shell_recieved_et)
    EditText shellRecEt;

    @BindView(R.id.intake_et)
    EditText intakeEt;

    @BindView(R.id.agency_et)
    EditText agencyEt;

    @BindView(R.id.conduit_et)
    EditText conduitEt;

    @BindView(R.id.coupler_et)
    EditText couplerEt;

    @BindView(R.id.ew_panel_et)
    EditText ewPanelEt;

    @BindView(R.id.roof_tray_et)
    EditText roofTrayEt;

    @BindView(R.id.ht_tray_et)
    EditText htTrayEt;

    @BindView(R.id.ht_equip_et)
    EditText htEquipEt;

    @BindView(R.id.high_dip_et)
    EditText highDipEt;

    @BindView(R.id.uf_tray_et)
    EditText ufTrayEt;

    @BindView(R.id.uf_trans_et)
    EditText ufTransEt;

    @BindView(R.id.uf_wire_et)
    EditText ufWireEt;

    @BindView(R.id.off_roof_et)
    EditText offRoofEt;

    @BindView(R.id.roof_clear_et)
    EditText roofClearEt;

    @BindView(R.id.off_ew_et)
    EditText offEwEt;

    @BindView(R.id.ew_clear_et)
    EditText ewClearEt;

    @BindView(R.id.mech_pan_et)
    EditText mechPanEt;

    @BindView(R.id.off_tf_et)
    EditText offTfEt;

    @BindView(R.id.tf_clear_et)
    EditText tfClearEt;

    @BindView(R.id.tf_prov_et)
    EditText tfProvEt;

    @BindView(R.id.lf_load_et)
    EditText lfLoadEt;

    @BindView(R.id.off_pow_et)
    EditText offPowEt;

    @BindView(R.id.pow_hv_et)
    EditText powerHvEt;

    @BindView(R.id.off_dip_et)
    EditText offDipEt;

    @BindView(R.id.dip_clear_et)
    EditText dipClearEt;

    @BindView(R.id.lower_et)
    EditText lowerEt;

    @BindView(R.id.off_cont_et)
    EditText offContEt;

    @BindView(R.id.cont_hv_et)
    EditText contHvEt;

    @BindView(R.id.load_test_et)
    EditText loadTestEt;

    @BindView(R.id.rmvu_et)
    EditText rmvuEt;

    @BindView(R.id.panto_et)
    EditText pantoEt;

    @BindView(R.id.pcp_clear_et)
    EditText pcpClearEt;

    @BindView(R.id.bu_form_et)
    EditText buFormEt;

    @BindView(R.id.rake_form_et)
    EditText rakeFormEt;

    @BindView(R.id.remarks_et)
    EditText remarksEt;

    @BindView(R.id.update_status_button)
    Button updateStatusButton;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token,rakeNum,coachNum,coachType,coachPosition,shellRec,intake,agency,conduit,coupler,ewPanel,roofTray
            ,htTray,htEquip,highDip,ufTray,ufTrans,ufWire,offRoof,roofClear,offEw,ewClear
            ,mechPan,offTf,tfClear,tfProv,lfLoad,offPow,powerHv,offDip,dipClear,lower,offCont
            ,contHv,loadTest,rmvu,panto,pcpClear,buForm,rakeForm,remarks;
    ArrayAdapter<String> coachTypeAdapter,coachPositionAdapter;
    ArrayList<String> coachTypeList,coachPositionList;
    Integer stage,line;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coach);
        ButterKnife.bind(this);

        addCoachButton.setOnClickListener(this);
        addCoachPositionButton.setOnClickListener(this);
        updateStatusButton.setOnClickListener(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");

        coachTypeList = new ArrayList<>();
        coachTypeList.add("trailer");
        coachTypeList.add("driving");
        coachTypeList.add("motor");
        coachTypeList.add("handicapped");

        coachTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, coachTypeList);
        coachTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coachTypeSpinner.setAdapter(coachTypeAdapter);
        coachTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                coachType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        coachPositionList = new ArrayList<>();
        coachPositionList.add("Shell");
        coachPositionList.add("Production");
        coachPositionList.add("Commission");
        coachPositionList.add("Despatch");
        coachPositionList.add("Paint");
        coachPositionList.add("Out");

        coachPositionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, coachPositionList);
        coachPositionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coachPositionSpinner.setAdapter(coachPositionAdapter);
        coachPositionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                coachPosition = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Bundle bundle = getIntent().getExtras();
        rakeNum = bundle.getString(RAKE_NUM);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add_coach_button:
                addCoach();
                break;
            case R.id.add_coach_position_button:
                addCoachPosition();
                break;
            case R.id.update_status_button:
                addCoachStatus();
                break;
        }
    }

    private void addCoachStatus() {

        shellRec = shellRecEt.getText().toString().trim();
        intake = intakeEt.getText().toString().trim();
        agency = agencyEt.getText().toString().trim();
        conduit = conduitEt.getText().toString().trim();
        coupler = couplerEt.getText().toString().trim();
        ewPanel = ewPanelEt.getText().toString().trim();
        roofTray = roofTrayEt.getText().toString().trim();
        htTray = htTrayEt.getText().toString().trim();
        htEquip = htEquipEt.getText().toString().trim();
        highDip = highDipEt.getText().toString().trim();
        ufTray = ufTrayEt.getText().toString().trim();
        ufTrans = ufTransEt.getText().toString().trim();
        ufWire = ufWireEt.getText().toString().trim();
        offRoof = offRoofEt.getText().toString().trim();
        roofClear = roofClearEt.getText().toString().trim();
        offEw = offEwEt.getText().toString().trim();
        ewClear = ewClearEt.getText().toString().trim();
        mechPan = mechPanEt.getText().toString().trim();
        offTf = offTfEt.getText().toString().trim();
        tfClear = tfClearEt.getText().toString().trim();
        tfProv = tfProvEt.getText().toString().trim();
        lfLoad = lfLoadEt.getText().toString().trim();
        offPow = offPowEt.getText().toString().trim();
        powerHv = powerHvEt.getText().toString().trim();
        offDip = offDipEt.getText().toString().trim();
        dipClear = dipClearEt.getText().toString().trim();
        lower = lowerEt.getText().toString().trim();
        offCont = offContEt.getText().toString().trim();
        contHv = contHvEt.getText().toString().trim();
        loadTest = loadTestEt.getText().toString().trim();
        rmvu = rmvuEt.getText().toString().trim();
        panto = pantoEt.getText().toString().trim();
        pcpClear = pcpClearEt.getText().toString().trim();
        buForm = buFormEt.getText().toString().trim();
        rakeForm = rakeFormEt.getText().toString().trim();
        remarks = remarksEt.getText().toString().trim();

        if(shellRec.isEmpty() && remarks.isEmpty() &&rakeForm.isEmpty() &&buForm.isEmpty() &&pcpClear.isEmpty()
                &&panto.isEmpty() &&rmvu.isEmpty() &&loadTest.isEmpty() &&contHv.isEmpty() &&offCont.isEmpty()
                &&lower.isEmpty() &&dipClear.isEmpty() &&offDip.isEmpty() &&powerHv.isEmpty() &&offPow.isEmpty() &&lfLoad.isEmpty()
                &&tfProv.isEmpty() &&tfClear.isEmpty() &&offTf.isEmpty() &&mechPan.isEmpty() &&ewClear.isEmpty() &&offEw.isEmpty()
                &&roofClear.isEmpty() &&offRoof.isEmpty() &&ufWire.isEmpty() &&ufTrans.isEmpty() &&ufTray.isEmpty() &&roofTray.isEmpty()
                &&ewPanel.isEmpty() &&coupler.isEmpty() &&conduit.isEmpty() &&agency.isEmpty() &&intake.isEmpty()){

            Toast.makeText(getApplicationContext(),"Enter some status",Toast.LENGTH_SHORT).show();
            return;
        }

        if(shellRec.isEmpty()){
            shellRec = null;
        }
        if(intake.isEmpty()){
            intake = null;
        }
        if(agency.isEmpty()){
            agency = null;
        }
        if(conduit.isEmpty()){
            conduit = null;
        }
        if(coupler.isEmpty()){
            coupler = null;
        }
        if(ewPanel.isEmpty()){
            ewPanel = null;
        }
        if(roofTray.isEmpty()){
            roofTray = null;
        }
        if(htTray.isEmpty()){
            htTray = null;
        }
        if(htEquip.isEmpty()){
            htEquip = null;
        }
        if(highDip.isEmpty()){
            highDip = null;
        }
        if(ufTray.isEmpty()){
            ufTray = null;
        }
        if(ufTrans.isEmpty()){
            ufTrans = null;
        }
        if(ufWire.isEmpty()){
            ufWire = null;
        }
        if(offRoof.isEmpty()){
            offRoof = null;
        }
        if(roofClear.isEmpty()){
            roofClear = null;
        }
        if(offEw.isEmpty()){
            offEw = null;
        }
        if(ewClear.isEmpty()){
            ewClear = null;
        }
        if(mechPan.isEmpty()){
            mechPan = null;
        }
        if(offTf.isEmpty()){
            offTf = null;
        }
        if(tfClear.isEmpty()){
            tfClear = null;
        }
        if(tfProv.isEmpty()){
            tfProv = null;
        }
        if(lfLoad.isEmpty()){
            lfLoad = null;
        }
        if(offPow.isEmpty()){
            offPow = null;
        }
        if(powerHv.isEmpty()){
            powerHv = null;
        }
        if(offDip.isEmpty()){
            offDip = null;
        }
        if(dipClear.isEmpty()){
            dipClear = null;
        }
        if(lower.isEmpty()){
            lower = null;
        }
        if(offCont.isEmpty()){
            offCont = null;
        }
        if(contHv.isEmpty()){
            contHv = null;
        }
        if(loadTest.isEmpty()){
            loadTest = null;
        }
        if(rmvu.isEmpty()){
            rmvu = null;
        }
        if(panto.isEmpty()){
            panto = null;
        }
        if(pcpClear.isEmpty()){
            pcpClear = null;
        }
        if(buForm.isEmpty()){
            buForm = null;
        }
        if(rakeForm.isEmpty()){
            rakeForm = null;
        }
        if(remarks.isEmpty()){
            remarks = null;
        }




        Log.e("SAN","Coach Num: " + coachNum);
        Call<PostResponse> call = apiInterface.updateStatus(token,coachNum,shellRec,intake,agency,conduit,coupler,ewPanel,
                roofTray,htTray,htEquip,highDip,ufTray,ufTrans,ufWire,offRoof,roofClear,offEw,ewClear,mechPan,offTf,tfClear,
                tfProv,lfLoad,offPow,powerHv,offDip,dipClear,lower,offCont,contHv,loadTest,rmvu,panto,pcpClear,buForm,
                rakeForm,remarks);

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                PostResponse postResponse = response.body();

                int status = postResponse.getStatus();

                if(status == 200){
                    Toast.makeText(getApplicationContext(),"Updated Successfully",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(),"Error Uploading. Try Again",Toast.LENGTH_SHORT).show();
                    Log.e("SAN","FAiled : " + status);
                }


            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_SHORT).show();
                Log.e("SAN","FAiled : " +t.toString());

            }
        });

    }

    private void addCoachPosition() {

        String stageString = coachStageEt.getText().toString();
        String lineString = coachLineEt.getText().toString();
        if(!stageString.isEmpty()){
            stage = Integer.parseInt(stageString);
        }

        if(!lineString.isEmpty()){
            line = Integer.parseInt(lineString);
        }



        Call<PostResponse> call = apiInterface.updatePosition(token,coachNum,coachPosition,line,stage);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                PostResponse postResponse = response.body();

                int status = postResponse.getStatus();
                if(status == 200){
                    addCoachPositionCard.setVisibility(View.INVISIBLE);
                    addCoachStatusCard.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(getApplicationContext(),"Error Updating Position. Try Again",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error Updating Position. Try Again",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void addCoach() {

        coachNum = coachNumberEt.getText().toString().trim();
        if(coachNum.isEmpty()){
            coachNumberEt.setError("Enter coach number");
            coachNumberEt.requestFocus();
            return;
        }

        Log.e("SAN","Coachnum: " +  coachNum);
        Log.e("SAN","rakenum : " +  rakeNum);
        Log.e("SAN","Coachtype" +  coachType);

        Call<PostResponse> call = apiInterface.createCoach(token,coachNum,rakeNum,coachType);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                PostResponse postResponse = response.body();

                int status = postResponse.getStatus();
                if(status == 200){
                    addCoachCard.setVisibility(View.INVISIBLE);
                    addCoachPositionCard.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(getApplicationContext(),"Error Creating Coach. Try Again",Toast.LENGTH_SHORT).show();
                    Log.e("SAN","Failed : "+ status );

                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_SHORT).show();
                Log.e("SAN","Failed : " + t.toString());
            }
        });


    }


}
