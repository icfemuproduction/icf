package com.example.application.iricf;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditStatusActivity extends AppCompatActivity {

    public static final String TOKEN = "token";
    public static final String COACH_NUM = "coachNum";
    public static final String SHELL_RECEIVED = "Shell Received";
    public static final String INTAKE = "Intake";
    public static final String AGENCY = "Agency";
    public static final String CONDUIT_LOADING = "Conduit Loading";
    public static final String IV_COUPLER_LOADING = "IV Coupler Loading";
    public static final String EW_PANEL_LOADING = "EW Panel Loading";
    public static final String ROOF_PASSENGER_TRAY_LOADING = "Roof Passenger Tray Loading";
    public static final String HT_ROOM_TRAY_LOADING = "HT Room Tray Loading";
    public static final String HT_EQUIPMENT_LOADING = "HT Equipment Loading";
    public static final String HIGH_DIP = "High Dip";
    public static final String UF_TRAY_LOADING = "UF Tray Loading";
    public static final String UF_TRANSFORMER_LOADING = "UF Transformer Loading";
    public static final String UF_WIRING = "UF Wiring";
    public static final String OFFERING_ROOF_CLEARANCE = "Offering Roof Clearance";
    public static final String ROOF_CLEARANCE = "Roof Clearance";
    public static final String OFFERING_END_WALL_CLEARANCE = "Offering EndWall Clearance";
    public static final String END_WALL_CLEARANCE = "EndWall Clearance";
    public static final String MECHANICAL_PANELING = "Mechanical Paneling";
    public static final String OFFERING_TROUGH_FLOOR_CLEARANCE= "Offering TroughFloor Clearance";
    public static final String TROUGH_FLOOR_CLEARANCE = "TroughFloor Clearance";
    public static final String TROUGH_FLOOR_PROVISION = "TroughFloor Provision";
    public static final String LF_LOADING = "LF Loading";
    public static final String OFFERING_POWER_CONTINUITY = "Offering Power Continuity";
    public static final String POWER_HV = "Power HV";
    public static final String OFFERING_HIDIP_CLEARANCE = "Offering HiDip Clearance";
    public static final String HIDIP_CLEARANCE = "HiDip Clearance";
    public static final String LOWERING = "Lowering";
    public static final String OFFERING_CONTROL_CONTINUITY = "Offering Control Continuity";
    public static final String CONTROL_HV = "Control HV";
    public static final String LOAD_TEST = "Load Test";
    public static final String RMVU_TESTING = "RMVU Testing";
    public static final String PANTOGRAPH = "Pantograph";
    public static final String PCP_CLEARANCE = "PCP Clearance";
    public static final String BASIC_UNIT_FORMATION = "Basic Unit Formation";
    public static final String RAKE_FORMATION = "Rake Formation";
    public static final String REMARKS = "Remarks";

    @BindView(R.id.edit_coach_status_card)
    CardView editCoachStatusCard;

    @BindView(R.id.edit_coach_status_name_tv)
    TextView coachNameTv;

    @BindView(R.id.edit_coach_status_rakeName_tv)
    TextView rakeNameTv;

    @BindView(R.id.edit_coach_status_rv)
    RecyclerView editCoachStatusRv;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token,coachNum;
    ArrayList<String> statusArrayList,statusNameArrayList;
    HashMap<String,String> statusMap;
    CoachStatusEditAdapter coachStatusEditAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);
        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");

        Bundle bundle = getIntent().getExtras();
        coachNum = bundle.getString(COACH_NUM);
        coachNameTv.setText("Coach Number : " + coachNum);

        statusArrayList = new ArrayList<>();
        statusNameArrayList = new ArrayList<>();
        statusMap = new HashMap<>();
        getStatus();
        statusNameArrayList.add(SHELL_RECEIVED);
        statusNameArrayList.add(INTAKE);
        statusNameArrayList.add(AGENCY);
        statusNameArrayList.add(CONDUIT_LOADING);
        statusNameArrayList.add(IV_COUPLER_LOADING);
        statusNameArrayList.add(EW_PANEL_LOADING);
        statusNameArrayList.add(ROOF_PASSENGER_TRAY_LOADING);
        statusNameArrayList.add(HT_ROOM_TRAY_LOADING);
        statusNameArrayList.add(HT_EQUIPMENT_LOADING);
        statusNameArrayList.add(HIGH_DIP);
        statusNameArrayList.add(UF_TRAY_LOADING);
        statusNameArrayList.add(UF_TRANSFORMER_LOADING);
        statusNameArrayList.add(UF_WIRING);
        statusNameArrayList.add(OFFERING_ROOF_CLEARANCE);
        statusNameArrayList.add(ROOF_CLEARANCE);
        statusNameArrayList.add(OFFERING_END_WALL_CLEARANCE);
        statusNameArrayList.add(END_WALL_CLEARANCE);
        statusNameArrayList.add(MECHANICAL_PANELING);
        statusNameArrayList.add(OFFERING_TROUGH_FLOOR_CLEARANCE);
        statusNameArrayList.add(TROUGH_FLOOR_CLEARANCE);
        statusNameArrayList.add(TROUGH_FLOOR_PROVISION);
        statusNameArrayList.add(LF_LOADING);
        statusNameArrayList.add(OFFERING_POWER_CONTINUITY);
        statusNameArrayList.add(POWER_HV);
        statusNameArrayList.add(OFFERING_HIDIP_CLEARANCE);
        statusNameArrayList.add(HIDIP_CLEARANCE);
        statusNameArrayList.add(LOWERING);
        statusNameArrayList.add(OFFERING_CONTROL_CONTINUITY);
        statusNameArrayList.add(CONTROL_HV);
        statusNameArrayList.add(LOAD_TEST);
        statusNameArrayList.add(RMVU_TESTING);
        statusNameArrayList.add(PANTOGRAPH);
        statusNameArrayList.add(PCP_CLEARANCE);
        statusNameArrayList.add(BASIC_UNIT_FORMATION);
        statusNameArrayList.add(RAKE_FORMATION);
        statusNameArrayList.add(REMARKS);




        coachStatusEditAdapter = new CoachStatusEditAdapter(this,statusNameArrayList,statusArrayList);
        editCoachStatusRv.setLayoutManager(new LinearLayoutManager(this));
        editCoachStatusRv.setAdapter(coachStatusEditAdapter);

    }

    private void getStatus() {

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<CoachStatusRegister> call = apiInterface.getCoachStatus(coachNum,token);
        call.enqueue(new Callback<CoachStatusRegister>() {
            @Override
            public void onResponse(Call<CoachStatusRegister> call, Response<CoachStatusRegister> response) {
                int status = response.body().getStatus();

                if(status == 200){
                    CoachStatusRegister coachStatusRegister = response.body();
                    String rakeNum = coachStatusRegister.getDatum().getRakeNum();

                    if(coachStatusRegister.getDatum().getShellReceived() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getShellReceived().toString());
                        statusMap.put(SHELL_RECEIVED,coachStatusRegister.getDatum().getShellReceived().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(SHELL_RECEIVED," ");
                    }

                    if(coachStatusRegister.getDatum().getIntake() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getIntake().toString());
                        statusMap.put(INTAKE,coachStatusRegister.getDatum().getIntake().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(INTAKE," ");
                    }

                    if(coachStatusRegister.getDatum().getAgency() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getAgency());
                        statusMap.put(AGENCY,coachStatusRegister.getDatum().getAgency());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(AGENCY," ");
                    }

                    if(coachStatusRegister.getDatum().getConduitLoad() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getConduitLoad().toString());
                        statusMap.put(CONDUIT_LOADING,coachStatusRegister.getDatum().getConduitLoad().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(CONDUIT_LOADING," ");
                    }

                    if(coachStatusRegister.getDatum().getIvCoupleLoad() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getIvCoupleLoad().toString());
                        statusMap.put(IV_COUPLER_LOADING,coachStatusRegister.getDatum().getIvCoupleLoad().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(IV_COUPLER_LOADING," ");
                    }

                    if(coachStatusRegister.getDatum().getEwPanelLoad() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getEwPanelLoad().toString());
                        statusMap.put(EW_PANEL_LOADING,coachStatusRegister.getDatum().getEwPanelLoad().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(EW_PANEL_LOADING," ");
                    }

                    if(coachStatusRegister.getDatum().getRoofPassTrayLoad() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getRoofPassTrayLoad().toString());
                        statusMap.put(ROOF_PASSENGER_TRAY_LOADING,coachStatusRegister.getDatum().getRoofPassTrayLoad().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(ROOF_PASSENGER_TRAY_LOADING," ");
                    }

                    if(coachStatusRegister.getDatum().getHtRoomTrayLoad() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getHtRoomTrayLoad().toString());
                        statusMap.put(HT_ROOM_TRAY_LOADING,coachStatusRegister.getDatum().getHtRoomTrayLoad().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(HT_ROOM_TRAY_LOADING," ");
                    }

                    if(coachStatusRegister.getDatum().getHtEquipLoad() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getHtEquipLoad().toString());
                        statusMap.put(HT_EQUIPMENT_LOADING,coachStatusRegister.getDatum().getHtEquipLoad().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(HT_EQUIPMENT_LOADING," ");
                    }

                    if(coachStatusRegister.getDatum().getHighDip() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getHighDip().toString());
                        statusMap.put(HIGH_DIP,coachStatusRegister.getDatum().getHighDip().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(HIGH_DIP," ");
                    }

                    if(coachStatusRegister.getDatum().getUfTrayLoad() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getUfTrayLoad().toString());
                        statusMap.put(UF_TRAY_LOADING,coachStatusRegister.getDatum().getUfTrayLoad().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(UF_TRAY_LOADING," ");
                    }

                    if(coachStatusRegister.getDatum().getUfTransLoad() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getUfTransLoad().toString());
                        statusMap.put(UF_TRANSFORMER_LOADING,coachStatusRegister.getDatum().getUfTransLoad().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(UF_TRANSFORMER_LOADING," ");
                    }

                    if(coachStatusRegister.getDatum().getUfWire() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getUfWire());
                        statusMap.put(UF_WIRING,coachStatusRegister.getDatum().getUfWire());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(UF_WIRING," ");
                    }

                    if(coachStatusRegister.getDatum().getOffRoofClear() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getOffRoofClear().toString());
                        statusMap.put(OFFERING_ROOF_CLEARANCE,coachStatusRegister.getDatum().getOffRoofClear().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(OFFERING_ROOF_CLEARANCE," ");
                    }

                    if(coachStatusRegister.getDatum().getRoofClear() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getRoofClear().toString());
                        statusMap.put(ROOF_CLEARANCE,coachStatusRegister.getDatum().getRoofClear().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(ROOF_CLEARANCE," ");
                    }

                    if(coachStatusRegister.getDatum().getOffEwClear() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getOffEwClear().toString());
                        statusMap.put(OFFERING_END_WALL_CLEARANCE,coachStatusRegister.getDatum().getOffEwClear().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(OFFERING_END_WALL_CLEARANCE," ");
                    }

                    if(coachStatusRegister.getDatum().getEwClear() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getEwClear().toString());
                        statusMap.put(END_WALL_CLEARANCE,coachStatusRegister.getDatum().getEwClear().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(END_WALL_CLEARANCE," ");
                    }


                    if(coachStatusRegister.getDatum().getMechPanel() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getMechPanel());
                        statusMap.put(MECHANICAL_PANELING,coachStatusRegister.getDatum().getMechPanel());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(MECHANICAL_PANELING," ");
                    }

                    if(coachStatusRegister.getDatum().getOffTfClear() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getOffTfClear().toString());
                        statusMap.put(OFFERING_TROUGH_FLOOR_CLEARANCE,coachStatusRegister.getDatum().getOffTfClear().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(OFFERING_TROUGH_FLOOR_CLEARANCE," ");
                    }

                    if(coachStatusRegister.getDatum().getTfClear() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getTfClear().toString());
                        statusMap.put(TROUGH_FLOOR_CLEARANCE,coachStatusRegister.getDatum().getTfClear().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(TROUGH_FLOOR_CLEARANCE," ");
                    }


                    if(coachStatusRegister.getDatum().getTfProv() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getTfProv().toString());
                        statusMap.put(TROUGH_FLOOR_PROVISION,coachStatusRegister.getDatum().getTfProv().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(TROUGH_FLOOR_PROVISION," ");
                    }


                    if(coachStatusRegister.getDatum().getLfLoad() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getLfLoad().toString());
                        statusMap.put(LF_LOADING,coachStatusRegister.getDatum().getLfLoad().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(LF_LOADING," ");
                    }


                    if(coachStatusRegister.getDatum().getOffPowerCont() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getOffPowerCont().toString());
                        statusMap.put(OFFERING_POWER_CONTINUITY,coachStatusRegister.getDatum().getOffPowerCont().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(OFFERING_POWER_CONTINUITY," ");
                    }

                    if(coachStatusRegister.getDatum().getPowerHv() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getPowerHv().toString());
                        statusMap.put(POWER_HV,coachStatusRegister.getDatum().getPowerHv().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(POWER_HV," ");
                    }


                    if(coachStatusRegister.getDatum().getOffHiDipClear() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getOffHiDipClear().toString());
                        statusMap.put(OFFERING_HIDIP_CLEARANCE,coachStatusRegister.getDatum().getOffHiDipClear().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(OFFERING_HIDIP_CLEARANCE," ");
                    }


                    if(coachStatusRegister.getDatum().getHiDipClear() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getHiDipClear().toString());
                        statusMap.put(HIDIP_CLEARANCE,coachStatusRegister.getDatum().getHiDipClear().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(HIDIP_CLEARANCE," ");
                    }


                    if(coachStatusRegister.getDatum().getLower() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getLower().toString());
                        statusMap.put(LOWERING,coachStatusRegister.getDatum().getLower().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(LOWERING," ");
                    }


                    if(coachStatusRegister.getDatum().getOffControlCont() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getOffControlCont().toString());
                        statusMap.put(OFFERING_CONTROL_CONTINUITY,coachStatusRegister.getDatum().getOffControlCont().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(OFFERING_CONTROL_CONTINUITY," ");
                    }


                    if(coachStatusRegister.getDatum().getControlHv() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getControlHv().toString());
                        statusMap.put(CONTROL_HV,coachStatusRegister.getDatum().getControlHv().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(CONTROL_HV," ");
                    }

                    if(coachStatusRegister.getDatum().getLoadTest() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getLoadTest().toString());
                        statusMap.put(LOAD_TEST,coachStatusRegister.getDatum().getLoadTest().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(LOAD_TEST," ");
                    }


                    if(coachStatusRegister.getDatum().getRmvuTest() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getRmvuTest().toString());
                        statusMap.put(RMVU_TESTING,coachStatusRegister.getDatum().getRmvuTest().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(RMVU_TESTING," ");
                    }


                    if(coachStatusRegister.getDatum().getPantograph() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getPantograph().toString());
                        statusMap.put(PANTOGRAPH,coachStatusRegister.getDatum().getPantograph().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(PANTOGRAPH," ");
                    }

                    if(coachStatusRegister.getDatum().getPcpClear() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getPcpClear().toString());
                        statusMap.put(PCP_CLEARANCE,coachStatusRegister.getDatum().getPcpClear().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(PCP_CLEARANCE," ");
                    }

                    if(coachStatusRegister.getDatum().getBuFormation() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getBuFormation().toString());
                        statusMap.put(BASIC_UNIT_FORMATION,coachStatusRegister.getDatum().getBuFormation().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(BASIC_UNIT_FORMATION," ");
                    }


                    if(coachStatusRegister.getDatum().getRakeFormation() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getRakeFormation().toString());
                        statusMap.put(RAKE_FORMATION,coachStatusRegister.getDatum().getRakeFormation().toString());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(RAKE_FORMATION," ");
                    }


                    if(coachStatusRegister.getDatum().getRemarks() != null){
                        statusArrayList.add(coachStatusRegister.getDatum().getRemarks());
                        statusMap.put(REMARKS,coachStatusRegister.getDatum().getRemarks());
                    }else {
                        statusArrayList.add(" ");
                        statusMap.put(REMARKS," ");
                    }
                    coachStatusEditAdapter.notifyDataSetChanged();
                    rakeNameTv.setText("Rake Number : "+ rakeNum);
                }
            }

            @Override
            public void onFailure(Call<CoachStatusRegister> call, Throwable t) {

            }
        });

    }
}
