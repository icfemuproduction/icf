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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.application.iricf.Utils.parseDateReceive;

public class CoachStatusActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static final String TOKEN = "token";

    public static final String SHELL_RECEIVED = "Shell Received";
    public static final String INTAKE = "Intake";
    public static final String AGENCY = "Agency";
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
    public static final String PCP_CLEARANCE = "PreCommission Points Clearance";
    public static final String BASIC_UNIT_FORMATION = "Basic Unit Formation";
    public static final String RAKE_FORMATION = "Rake Formation";
    public static final String REMARKS = "Remarks";

    /*@BindView(R.id.rake_search_bar)
    AutoCompleteTextView rakeSearchBar;

    @BindView(R.id.rake_search_button)
    ImageView rakeSearchButton;*/

    @BindView(R.id.coach_status_coaches_card)
    CardView coachesCard;

    @BindView(R.id.coach_status_rv)
    RecyclerView coachesRv;

    @BindView(R.id.rake_search_layout)
    LinearLayout rakeSearchLayout;

    @BindView(R.id.rake_search_spinner)
    Spinner rakeSearchSpinner;

    List<CoachPerRake> coachPerRakeArrayList;
    ArrayList<String> coachNamesArrayList;
    CoachStatusAdapter coachStatusAdapter;

    ArrayList<Property> propertyArrayList;
    ArrayList<String> rakeNames,statusArrayList,statusNameArrayList,rakeNumbersList;;
    ArrayAdapter<String> predictionAdapter;
    List<RakeName> rakeList;
    StatusPropertyAdapter statusPropertyAdapter;
    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token,rakeNum;
    AlertDialog b,loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_status);
        ButterKnife.bind(this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        ((TextView) dialogView.findViewById(R.id.progressDialog_textView)).setText(R.string.loading);
        loadingDialog = new android.support.v7.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();
        loadingDialog.show();

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        rakeNames = new ArrayList<>();
        rakeNumbersList = new ArrayList<>();
        coachPerRakeArrayList = new ArrayList<>();
        statusArrayList = new ArrayList<>();
        statusNameArrayList = new ArrayList<>();
        statusNameArrayList.add(SHELL_RECEIVED);
        statusNameArrayList.add(INTAKE);
        statusNameArrayList.add(AGENCY);
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
        statusNameArrayList.add(PCP_CLEARANCE);
        statusNameArrayList.add(BASIC_UNIT_FORMATION);
        statusNameArrayList.add(RAKE_FORMATION);
        statusNameArrayList.add(REMARKS);

        fetchRakes();

        propertyArrayList = new ArrayList<>();
        coachNamesArrayList = new ArrayList<>();

        coachStatusAdapter = new CoachStatusAdapter(this,coachNamesArrayList);
        coachesRv.setLayoutManager(new LinearLayoutManager(this));
        coachesRv.setAdapter(coachStatusAdapter);

        coachStatusAdapter.setOnClickListener(new CoachStatusAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                String coachNum = coachNamesArrayList.get(position);
                displayStatus(coachNum);
            }
        });


        rakeSearchSpinner.setOnItemSelectedListener(this);
    }

    private void fetchCoaches(String rakenum) {

        for (int i=0 ; i<rakenum.length() ; i++){
            if(rakenum.charAt(i) =='/'){

                rakenum = rakenum.substring(0,i)+'_'+rakenum.substring(i+1);
            }

        }
        loadingDialog.show();
        Call<CoachPerRakeRegister> call = apiInterface.getRakeCoaches(rakenum,token);
        call.enqueue(new Callback<CoachPerRakeRegister>() {
            @Override
            public void onResponse(Call<CoachPerRakeRegister> call, Response<CoachPerRakeRegister> response) {
                int status = response.body().getStatus();

                if(status == 200){
                    CoachPerRakeRegister coachPerRakeRegister = response.body();

                    coachPerRakeArrayList = coachPerRakeRegister.getCoaches();

                    for(int i=0 ; i<coachPerRakeArrayList.size() ; i++){
                        coachNamesArrayList.add(coachPerRakeArrayList.get(i).getCoachNum());
                    }
                    coachStatusAdapter.notifyDataSetChanged();
                    coachesCard.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(getApplicationContext(),"Error Fetching Data. Try Again.",Toast.LENGTH_SHORT).show();
                }

                loadingDialog.dismiss();

            }

            @Override
            public void onFailure(Call<CoachPerRakeRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error Fetching Data. Try Again.",Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void fetchRakes() {

        Call<RakeRegister> call = apiInterface.getRakes(token);
        call.enqueue(new Callback<RakeRegister>() {
            @Override
            public void onResponse(Call<RakeRegister> call, Response<RakeRegister> response) {

                Integer statusCode =response.body().getStatus();
                if(statusCode == 200){
                    RakeRegister rakeRegister = response.body();
                    rakeList = rakeRegister.getRakes();
                    for(int i=0 ; i<rakeList.size() ; i++){
                        StringBuilder sb = new StringBuilder();
                        sb.append(rakeList.get(i).getRailway());
                        sb.append(rakeList.get(i).getRakeNum());
                        String rakeName = sb.toString();
                        rakeNames.add(rakeName);
                        rakeNumbersList.add(rakeList.get(i).getRakeNum());
                    }
                    predictionAdapter = new ArrayAdapter<>(CoachStatusActivity.this,android.R.layout.simple_dropdown_item_1line,rakeNames);
                    predictionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    rakeSearchSpinner.setAdapter(predictionAdapter);
                }else {
                    Toast.makeText(getApplicationContext(),"Error Fetching Data. Try Again.",Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
                rakeSearchLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Call<RakeRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error Fetching Data. Try Again.",Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
                rakeSearchLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void displayStatus(String coachName) {

        for (int i=0 ; i<coachName.length() ; i++){
            if(coachName.charAt(i) =='/'){
                coachName = coachName.substring(0,i)+'_'+coachName.substring(i+1);
            }
        }

        statusPropertyAdapter = new StatusPropertyAdapter(this,statusNameArrayList,statusArrayList);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.coach_status_dialog, null);
        dialogBuilder.setView(dialogView);
        b = dialogBuilder.create();

        TextView coachNameTv = dialogView.findViewById(R.id.coach_status_dialog_coachName_tv);
        final TextView rakeNameTv = dialogView.findViewById(R.id.coach_status_dialog_rakeName_tv);
        coachNameTv.setText(coachName);

        ImageView closeDialogButton = dialogView.findViewById(R.id.dialog_close_button);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        RecyclerView statusPropertyRv = dialogView.findViewById(R.id.coach_status_dialog_rv);
        statusPropertyRv.setLayoutManager(new LinearLayoutManager(this));
        statusPropertyRv.setAdapter(statusPropertyAdapter);

        loadingDialog.show();
        Call<CoachStatusRegister> call = apiInterface.getCoachStatus(coachName,token);
        call.enqueue(new Callback<CoachStatusRegister>() {
           @Override
           public void onResponse(Call<CoachStatusRegister> call, Response<CoachStatusRegister> response) {

               int status =response.body().getStatus();

               if(status == 200){
                   statusArrayList.clear();
                   CoachStatusRegister coachStatusRegister = response.body();
                   String rakeNum = coachStatusRegister.getDatum().getRakeNum();

                   if(coachStatusRegister.getDatum().getShellReceived() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getShellReceived().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getIntake() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getIntake().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getAgency() != null){
                       statusArrayList.add(coachStatusRegister.getDatum().getAgency());
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getIvCoupleLoad() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getIvCoupleLoad().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getEwPanelLoad() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getEwPanelLoad().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getRoofPassTrayLoad() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getRoofPassTrayLoad().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getHtRoomTrayLoad() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getHtRoomTrayLoad().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getHtEquipLoad() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getHtEquipLoad().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getHighDip() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getHighDip().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getUfTrayLoad() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getUfTrayLoad().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getUfTransLoad() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getUfTransLoad().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getUfWire() != null){
                       statusArrayList.add(coachStatusRegister.getDatum().getUfWire());
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getOffRoofClear() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getOffRoofClear().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getRoofClear() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getRoofClear().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getOffEwClear() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getOffEwClear().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getEwClear() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getEwClear().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }


                   if(coachStatusRegister.getDatum().getMechPanel() != null){
                       statusArrayList.add(coachStatusRegister.getDatum().getMechPanel());
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getOffTfClear() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getOffTfClear().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getTfClear() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getTfClear().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }


                   if(coachStatusRegister.getDatum().getTfProv() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getTfProv().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }


                   if(coachStatusRegister.getDatum().getLfLoad() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getLfLoad().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }


                   if(coachStatusRegister.getDatum().getOffPowerCont() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getOffPowerCont().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getPowerHv() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getPowerHv().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }


                   if(coachStatusRegister.getDatum().getOffHiDipClear() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getOffHiDipClear().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }


                   if(coachStatusRegister.getDatum().getHiDipClear() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getHiDipClear().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }


                   if(coachStatusRegister.getDatum().getLower() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getLower().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }


                   if(coachStatusRegister.getDatum().getOffControlCont() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getOffControlCont().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }


                   if(coachStatusRegister.getDatum().getControlHv() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getControlHv().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getLoadTest() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getLoadTest().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getPcpClear() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getPcpClear().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }

                   if(coachStatusRegister.getDatum().getBuFormation() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getBuFormation().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }


                   if(coachStatusRegister.getDatum().getRakeFormation() != null){
                       statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getRakeFormation().toString()));
                   }else {
                       statusArrayList.add(" ");
                   }


                   if(coachStatusRegister.getDatum().getRemarks() != null){
                       statusArrayList.add(coachStatusRegister.getDatum().getRemarks());
                   }else {
                       statusArrayList.add(" ");
                   }

                   statusPropertyAdapter.notifyDataSetChanged();
                   rakeNameTv.setText("Rake Number : " + rakeNum);
                   b.show();
               }else {
                   Toast.makeText(getApplicationContext(),"Error Fetching Data. Try Again.",Toast.LENGTH_SHORT).show();
               }
               loadingDialog.dismiss();
           }

           @Override
           public void onFailure(Call<CoachStatusRegister> call, Throwable t) {
               Toast.makeText(getApplicationContext(),"Error Fetching Data. Try Again.",Toast.LENGTH_SHORT).show();
               loadingDialog.dismiss();
           }
       });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        rakeNum = rakeNumbersList.get(position);
        coachNamesArrayList.clear();

        fetchCoaches(rakeNum);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
