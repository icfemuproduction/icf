package icf.gov.in.iricf;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static icf.gov.in.iricf.Utils.parseDateReceive;
import static icf.gov.in.iricf.Utils.parseDateSend;

public class EditStatusActivity extends AppCompatActivity {

    public static final String TOKEN = "token";
    public static final String COACH_NUM = "coachNum";
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
    public static final String OFFERING_TROUGH_FLOOR_CLEARANCE = "Offering TroughFloor Clearance";
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

    @BindView(R.id.edit_coach_status_name_tv)
    TextView coachNameTv;

    @BindView(R.id.edit_coach_status_rakeName_tv)
    TextView rakeNameTv;

    @BindView(R.id.edit_coach_status_rv)
    RecyclerView editCoachStatusRv;

    @BindView(R.id.edit_coach_position_tv)
    TextView editCoachPositionTv;

    @BindView(R.id.edit_coach_position_button)
    ImageView editCoachPositionButton;

    @BindView(R.id.edit_coach_line_tv)
    TextView editCoachLineTv;

    @BindView(R.id.edit_coach_stage_tv)
    TextView editCoachStageTv;

    @BindView(R.id.edit_coach_status_card)
    CardView cardView;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token, coachNum, shellRec, intake, agency, coupler, ewPanel, roofTray, htTray, htEquip, highDip,
            ufTray, ufTrans, ufWire, offRoof, roofClear, offEw, ewClear, mechPan, offTf, tfClear, tfProv, lfLoad,
            offPow, powerHv, offDip, dipClear, lower, offCont, contHv, loadTest, pcpClear, buForm, rakeForm, remarks,
            positionName, fieldName;
    ArrayList<String> statusArrayList, statusNameArrayList, statusKeyArrayList, editedValuesList, coachPositionList;
    ArrayAdapter<String> coachPositionAdapter;
    CoachStatusEditAdapter coachStatusEditAdapter;
    AlertDialog b, a;
    TextView addDialogTv;
    EditText addDialogEt, coachStageNoEt, coachLineNoEt;
    Spinner editCoachPosSpinner;
    Button dialogCancelButton, dialogAddButton, coachPositionCancelButton, coachPositionUpdateButton;
    Integer line, stage;
    AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_status);
        ButterKnife.bind(this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        ((TextView) dialogView.findViewById(R.id.progressDialog_textView)).setText(R.string.loading);
        loadingDialog = new android.support.v7.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();
        loadingDialog.show();

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN, "");

        Bundle bundle = getIntent().getExtras();
        coachNum = bundle.getString(COACH_NUM);

        coachNameTv.setText(String.format("Coach Number : %s", coachNum));
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        editedValuesList = new ArrayList<>();
        statusArrayList = new ArrayList<>();
        statusNameArrayList = new ArrayList<>();
        statusKeyArrayList = new ArrayList<>();
        coachPositionList = new ArrayList<>();
        getPosition(coachNum);
        getStatus(coachNum);
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

        coachStatusEditAdapter = new CoachStatusEditAdapter(this, statusNameArrayList, statusArrayList);
        editCoachStatusRv.setLayoutManager(new LinearLayoutManager(this));
        editCoachStatusRv.setAdapter(coachStatusEditAdapter);

        coachStatusEditAdapter.setOnClickListener(new CoachStatusEditAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                openStatusEditDialog(position);
            }
        });

        editCoachPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coachPositionEditDialog();
            }
        });
    }

    private void coachPositionEditDialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.coach_position_edit_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        a = dialogBuilder.create();
        a.show();

        editCoachPosSpinner = dialogView.findViewById(R.id.edit_coach_position_spinner);
        coachLineNoEt = dialogView.findViewById(R.id.edit_coach_position_line_et);
        coachStageNoEt = dialogView.findViewById(R.id.edit_coach_position_stage_et);
        coachPositionCancelButton = dialogView.findViewById(R.id.edit_coach_position_cancel_button);
        coachPositionUpdateButton = dialogView.findViewById(R.id.edit_coach_position_button);

        coachPositionCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a.dismiss();
            }
        });

        coachPositionUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatePosition(coachNum);
            }
        });

        coachPositionList = new ArrayList<>();
        coachPositionList.add("Shell");
        coachPositionList.add("Production");
        coachPositionList.add("Commission");
        coachPositionList.add("Despatch");
        coachPositionList.add("Paint");
        coachPositionList.add("Out");
        coachPositionList.add("Shop36");
        coachPositionList.add("Coach-despatched");

        coachPositionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, coachPositionList);
        coachPositionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCoachPosSpinner.setAdapter(coachPositionAdapter);
        editCoachPosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                positionName = adapterView.getItemAtPosition(i).toString().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updatePosition(String coachnum) {

        final String lineString = coachLineNoEt.getText().toString();
        final String stageString = coachStageNoEt.getText().toString();

        if (!lineString.isEmpty()) {
            line = Integer.parseInt(lineString);
        } else {
            line = 0;
        }
        if (!stageString.isEmpty()) {
            stage = Integer.parseInt(stageString);
        } else {
            stage = 0;
        }

        loadingDialog.show();
        Call<PostResponse> call = apiInterface.updatePosition(token, coachNum, positionName, line, stage);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                int status = response.body().getStatus();
                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                    editCoachPositionTv.setText(positionName);
                    if (!lineString.isEmpty()) {
                        editCoachLineTv.setText(lineString);
                    } else {
                        editCoachLineTv.setText(String.valueOf(0));
                    }
                    if (!stageString.isEmpty()) {
                        editCoachStageTv.setText(stageString);
                    } else {
                        editCoachStageTv.setText(String.valueOf(0));
                    }
                    a.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Error updating. Try Again.", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error updating. Try Again.", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void getPosition(String coachnum) {
        for (int i = 0; i < coachnum.length(); i++) {
            if (coachnum.charAt(i) == '/') {
                coachnum = coachnum.substring(0, i) + '_' + coachnum.substring(i + 1);
            }
        }

        Call<CoachPositionRegister> call = apiInterface.getCoachPosition(coachnum, token);
        call.enqueue(new Callback<CoachPositionRegister>() {
            @Override
            public void onResponse(Call<CoachPositionRegister> call, Response<CoachPositionRegister> response) {
                int status = response.body().getStatus();

                if (status == 200) {
                    CoachPositionRegister positionRegister = response.body();
                    Position position = positionRegister.getPosition();
                    if (position.getLineName() != null) {
                        editCoachPositionTv.setText(position.getLineName());
                    }
                    if (position.getLineNo() != null) {
                        editCoachLineTv.setText(String.valueOf(position.getLineNo()));
                    }
                    if (position.getStage() != null) {
                        editCoachStageTv.setText(String.valueOf(position.getStage()));
                    }

                }
            }

            @Override
            public void onFailure(Call<CoachPositionRegister> call, Throwable t) {

            }
        });
    }

    private void openStatusEditDialog(final int position) {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_status_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.show();
        addDialogTv = dialogView.findViewById(R.id.add_dialog_tv);
        addDialogEt = dialogView.findViewById(R.id.add_dialog_et);
        dialogAddButton = dialogView.findViewById(R.id.add_dialog_button);
        dialogAddButton.setText(R.string.update);
        dialogCancelButton = dialogView.findViewById(R.id.add_dialog_cancel_button);

        addDialogTv.setText(statusNameArrayList.get(position));
        addDialogEt.setText(statusArrayList.get(position));
        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        dialogAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStatus(position, coachNum);
            }
        });

    }

    private void updateStatus(int position, String coachnum) {

        String editedValue = addDialogEt.getText().toString().trim();
        if (editedValue.isEmpty()) {
            editedValue = null;
        }

        Map<String,String> statusMap = new HashMap<>();
        if(editedValue != null){
            switch (position) {
                case 0:
                    fieldName = "shell_rec";
                    shellRec = parseDateSend(editedValue);
                    statusMap.put("shell_rec",shellRec);
                    statusArrayList.set(position, editedValue);
                    break;
                case 1:
                    fieldName = "intake";
                    intake = parseDateSend(editedValue);
                    statusMap.put("intake",intake);
                    statusArrayList.set(position, editedValue);
                    break;
                case 2:
                    fieldName = "agency";
                    agency = editedValue;
                    statusMap.put("agency",agency);
                    statusArrayList.set(position, editedValue);
                    break;
                case 3:
                    fieldName = "coupler";
                    coupler = parseDateSend(editedValue);
                    statusMap.put("coupler",coupler);
                    statusArrayList.set(position, editedValue);
                    break;
                case 4:
                    fieldName = "ew_panel";
                    ewPanel = parseDateSend(editedValue);
                    statusMap.put("ew_panel",ewPanel);
                    statusArrayList.set(position, editedValue);
                    break;
                case 5:
                    fieldName = "roof_tray";
                    roofTray = parseDateSend(editedValue);
                    statusMap.put("roof_tray",roofTray);
                    statusArrayList.set(position, editedValue);
                    break;
                case 6:
                    fieldName = "ht_tray";
                    htTray = parseDateSend(editedValue);
                    statusMap.put("ht_tray",htTray);
                    statusArrayList.set(position, editedValue);
                    break;
                case 7:
                    fieldName = "ht_equip";
                    htEquip = parseDateSend(editedValue);
                    statusMap.put("ht_equip",htEquip);
                    statusArrayList.set(position, editedValue);
                    break;
                case 8:
                    fieldName = "high_dip";
                    highDip = parseDateSend(editedValue);
                    statusMap.put("high_dip",highDip);
                    statusArrayList.set(position, editedValue);
                    break;
                case 9:
                    fieldName = "uf_tray";
                    ufTray = parseDateSend(editedValue);
                    statusMap.put("uf_tray",ufTray);
                    statusArrayList.set(position, editedValue);
                    break;
                case 10:
                    fieldName = "uf_trans";
                    ufTrans = parseDateSend(editedValue);
                    statusMap.put("uf_trans",ufTrans);
                    statusArrayList.set(position, editedValue);
                    break;
                case 11:
                    fieldName = "uf_wire";
                    ufWire = editedValue;
                    statusMap.put("uf_wire",ufWire);
                    statusArrayList.set(position, editedValue);
                    break;
                case 12:
                    fieldName = "off_roof";
                    offRoof = parseDateSend(editedValue);
                    statusMap.put("off_roof",offRoof);
                    statusArrayList.set(position, editedValue);
                    break;
                case 13:
                    fieldName = "roof_clear";
                    roofClear = parseDateSend(editedValue);
                    statusMap.put("roof_clear",roofClear);
                    statusArrayList.set(position, editedValue);
                    break;
                case 14:
                    fieldName = "off_ew";
                    offEw = parseDateSend(editedValue);
                    statusMap.put("off_ew",offEw);
                    statusArrayList.set(position, editedValue);
                    break;
                case 15:
                    fieldName = "ew_clear";
                    ewClear = parseDateSend(editedValue);
                    statusMap.put("ew_clear",ewClear);
                    statusArrayList.set(position, editedValue);
                    break;
                case 16:
                    fieldName = "mech_pan";
                    mechPan = editedValue;
                    statusMap.put("mech_pan",mechPan);
                    statusArrayList.set(position, editedValue);
                    break;
                case 17:
                    fieldName = "off_tf";
                    offTf = parseDateSend(editedValue);
                    statusMap.put("off_tf",offTf);
                    statusArrayList.set(position, editedValue);
                    break;
                case 18:
                    fieldName = "tf_clear";
                    tfClear = parseDateSend(editedValue);
                    statusMap.put("tf_clear",tfClear);
                    statusArrayList.set(position, editedValue);
                    break;
                case 19:
                    fieldName = "tf_prov";
                    tfProv = parseDateSend(editedValue);
                    statusMap.put("tf_prov",tfProv);
                    statusArrayList.set(position, editedValue);
                    break;
                case 20:
                    fieldName = "lf_load";
                    lfLoad = parseDateSend(editedValue);
                    statusMap.put("lf_load",lfLoad);
                    statusArrayList.set(position, editedValue);
                    break;
                case 21:
                    fieldName = "off_pow";
                    offPow = parseDateSend(editedValue);
                    statusMap.put("off_pow",offPow);
                    statusArrayList.set(position, editedValue);
                    break;
                case 22:
                    fieldName = "power_hv";
                    powerHv = parseDateSend(editedValue);
                    statusMap.put("power_hv",powerHv);
                    statusArrayList.set(position, editedValue);
                    break;
                case 23:
                    fieldName = "off_dip";
                    offDip = parseDateSend(editedValue);
                    statusMap.put("off_dip",offDip);
                    statusArrayList.set(position, editedValue);
                    break;
                case 24:
                    fieldName = "dip_clear";
                    dipClear = parseDateSend(editedValue);
                    statusMap.put("dip_clear",dipClear);
                    statusArrayList.set(position, editedValue);
                    break;
                case 25:
                    fieldName = "lower";
                    lower = parseDateSend(editedValue);
                    statusMap.put("lower",lower);
                    statusArrayList.set(position, editedValue);
                    break;
                case 26:
                    fieldName = "off_cont";
                    offCont = parseDateSend(editedValue);
                    statusMap.put("off_cont",offCont);
                    statusArrayList.set(position, editedValue);
                    break;
                case 27:
                    fieldName = "cont_hv";
                    contHv = parseDateSend(editedValue);
                    statusMap.put("cont_hv",contHv);
                    statusArrayList.set(position, editedValue);
                    break;
                case 28:
                    fieldName = "load_test";
                    loadTest = parseDateSend(editedValue);
                    statusMap.put("load_test",loadTest);
                    statusArrayList.set(position, editedValue);
                    break;
                case 29:
                    fieldName = "pcp_clear";
                    pcpClear = parseDateSend(editedValue);
                    statusMap.put("pcp_clear",pcpClear);
                    statusArrayList.set(position, editedValue);
                    break;
                case 30:
                    fieldName = "bu_form";
                    buForm = parseDateSend(editedValue);
                    statusMap.put("bu_form",buForm);
                    statusArrayList.set(position, editedValue);
                    break;
                case 31:
                    fieldName = "rake_form";
                    rakeForm = parseDateSend(editedValue);
                    statusMap.put("rake_form",rakeForm);
                    statusArrayList.set(position, editedValue);
                    break;
                case 32:
                    fieldName = "remarks";
                    remarks = editedValue;
                    statusMap.put("remarks",remarks);
                    statusArrayList.set(position, editedValue);
                    break;
            }
        } else {
            switch (position) {
                case 0:
                    fieldName = "shell_rec";
                    shellRec = " ";
                    statusMap.put("shell_rec",shellRec);
                    statusArrayList.set(position, editedValue);
                    break;
                case 1:
                    fieldName = "intake";
                    intake = " ";
                    statusMap.put("intake",intake);
                    statusArrayList.set(position, editedValue);
                    break;
                case 2:
                    fieldName = "agency";
                    agency = " ";;
                    statusMap.put("agency",agency);
                    statusArrayList.set(position, editedValue);
                    break;
                case 3:
                    fieldName = "coupler";
                    coupler = " ";
                    statusMap.put("coupler",coupler);
                    statusArrayList.set(position, editedValue);
                    break;
                case 4:
                    fieldName = "ew_panel";
                    ewPanel =" ";
                    statusMap.put("ew_panel",ewPanel);
                    statusArrayList.set(position, editedValue);
                    break;
                case 5:
                    fieldName = "roof_tray";
                    roofTray = " ";
                    statusMap.put("roof_tray",roofTray);
                    statusArrayList.set(position, editedValue);
                    break;
                case 6:
                    fieldName = "ht_tray";
                    htTray = " ";
                    statusMap.put("ht_tray",htTray);
                    statusArrayList.set(position, editedValue);
                    break;
                case 7:
                    fieldName = "ht_equip";
                    htEquip = " ";
                    statusMap.put("ht_equip",htEquip);
                    statusArrayList.set(position, editedValue);
                    break;
                case 8:
                    fieldName = "high_dip";
                    highDip = " ";
                    statusMap.put("high_dip",highDip);
                    statusArrayList.set(position, editedValue);
                    break;
                case 9:
                    fieldName = "uf_tray";
                    ufTray = " ";
                    statusMap.put("uf_tray",ufTray);
                    statusArrayList.set(position, editedValue);
                    break;
                case 10:
                    fieldName = "uf_trans";
                    ufTrans = " ";
                    statusMap.put("uf_trans",ufTrans);
                    statusArrayList.set(position, editedValue);
                    break;
                case 11:
                    fieldName = "uf_wire";
                    ufWire = " ";
                    statusMap.put("uf_wire",ufWire);
                    statusArrayList.set(position, editedValue);
                    break;
                case 12:
                    fieldName = "off_roof";
                    offRoof = " ";
                    statusMap.put("off_roof",offRoof);
                    statusArrayList.set(position, editedValue);
                    break;
                case 13:
                    fieldName = "roof_clear";
                    roofClear = " ";
                    statusMap.put("roof_clear",roofClear);
                    statusArrayList.set(position, editedValue);
                    break;
                case 14:
                    fieldName = "off_ew";
                    offEw = " ";
                    statusMap.put("off_ew",offEw);
                    statusArrayList.set(position, editedValue);
                    break;
                case 15:
                    fieldName = "ew_clear";
                    ewClear = " ";
                    statusMap.put("ew_clear",ewClear);
                    statusArrayList.set(position, editedValue);
                    break;
                case 16:
                    fieldName = "mech_pan";
                    mechPan = " ";
                    statusMap.put("mech_pan",mechPan);
                    statusArrayList.set(position, editedValue);
                    break;
                case 17:
                    fieldName = "off_tf";
                    offTf = " ";
                    statusMap.put("off_tf",offTf);
                    statusArrayList.set(position, editedValue);
                    break;
                case 18:
                    fieldName = "tf_clear";
                    tfClear = " ";
                    statusMap.put("tf_clear",tfClear);
                    statusArrayList.set(position, editedValue);
                    break;
                case 19:
                    fieldName = "tf_prov";
                    tfProv = " ";
                    statusMap.put("tf_prov",tfProv);
                    statusArrayList.set(position, editedValue);
                    break;
                case 20:
                    fieldName = "lf_load";
                    lfLoad = " ";
                    statusMap.put("lf_load",lfLoad);
                    statusArrayList.set(position, editedValue);
                    break;
                case 21:
                    fieldName = "off_pow";
                    offPow = " ";
                    statusMap.put("off_pow",offPow);
                    statusArrayList.set(position, editedValue);
                    break;
                case 22:
                    fieldName = "power_hv";
                    powerHv = " ";
                    statusMap.put("power_hv",powerHv);
                    statusArrayList.set(position, editedValue);
                    break;
                case 23:
                    fieldName = "off_dip";
                    offDip = " ";
                    statusMap.put("off_dip",offDip);
                    statusArrayList.set(position, editedValue);
                    break;
                case 24:
                    fieldName = "dip_clear";
                    dipClear = " ";
                    statusMap.put("dip_clear",dipClear);
                    statusArrayList.set(position, editedValue);
                    break;
                case 25:
                    fieldName = "lower";
                    lower = " ";
                    statusMap.put("lower",lower);
                    statusArrayList.set(position, editedValue);
                    break;
                case 26:
                    fieldName = "off_cont";
                    offCont = " ";
                    statusMap.put("off_cont",offCont);
                    statusArrayList.set(position, editedValue);
                    break;
                case 27:
                    fieldName = "cont_hv";
                    contHv = " ";
                    statusMap.put("cont_hv",contHv);
                    statusArrayList.set(position, editedValue);
                    break;
                case 28:
                    fieldName = "load_test";
                    loadTest = " ";
                    statusMap.put("load_test",loadTest);
                    statusArrayList.set(position, editedValue);
                    break;
                case 29:
                    fieldName = "pcp_clear";
                    pcpClear = " ";
                    statusMap.put("pcp_clear",pcpClear);
                    statusArrayList.set(position, editedValue);
                    break;
                case 30:
                    fieldName = "bu_form";
                    buForm = " ";
                    statusMap.put("bu_form",buForm);
                    statusArrayList.set(position, editedValue);
                    break;
                case 31:
                    fieldName = "rake_form";
                    rakeForm = " ";
                    statusMap.put("rake_form",rakeForm);
                    statusArrayList.set(position, editedValue);
                    break;
                case 32:
                    fieldName = "remarks";
                    remarks = " ";
                    statusMap.put("remarks",remarks);
                    statusArrayList.set(position, editedValue);
                    break;
            }
        }


        loadingDialog.show();
        Call<PostResponse> call = apiInterface.editStatus(fieldName, token, coachNum, statusMap);

        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                int status = response.body().getStatus();
                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
                    coachStatusEditAdapter.notifyDataSetChanged();
                    b.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Error updating. Try Again.", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error updating. Try Again.", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });


    }

    private void getStatus(String coachnum) {

        for (int i = 0; i < coachnum.length(); i++) {
            if (coachnum.charAt(i) == '/') {
                coachnum = coachnum.substring(0, i) + '_' + coachnum.substring(i + 1);
            }
        }
        Call<CoachStatusRegister> call = apiInterface.getCoachStatus(coachnum, token);
        call.enqueue(new Callback<CoachStatusRegister>() {
            @Override
            public void onResponse(Call<CoachStatusRegister> call, Response<CoachStatusRegister> response) {
                int status = response.body().getStatus();

                if (status == 200) {
                    CoachStatusRegister coachStatusRegister = response.body();
                    String rakeNum = coachStatusRegister.getDatum().getRakeNum();

                    if (coachStatusRegister.getDatum().getShellReceived() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getShellReceived().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getIntake() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getIntake().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getAgency() != null) {
                        statusArrayList.add(coachStatusRegister.getDatum().getAgency());
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getIvCoupleLoad() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getIvCoupleLoad().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getEwPanelLoad() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getEwPanelLoad().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getRoofPassTrayLoad() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getRoofPassTrayLoad().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getHtRoomTrayLoad() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getHtRoomTrayLoad().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getHtEquipLoad() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getHtEquipLoad().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getHighDip() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getHighDip().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getUfTrayLoad() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getUfTrayLoad().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getUfTransLoad() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getUfTransLoad().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getUfWire() != null) {
                        statusArrayList.add(coachStatusRegister.getDatum().getUfWire());
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getOffRoofClear() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getOffRoofClear().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getRoofClear() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getRoofClear().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getOffEwClear() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getOffEwClear().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getEwClear() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getEwClear().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }


                    if (coachStatusRegister.getDatum().getMechPanel() != null) {
                        statusArrayList.add(coachStatusRegister.getDatum().getMechPanel());
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getOffTfClear() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getOffTfClear().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getTfClear() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getTfClear().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }


                    if (coachStatusRegister.getDatum().getTfProv() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getTfProv().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }


                    if (coachStatusRegister.getDatum().getLfLoad() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getLfLoad().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }


                    if (coachStatusRegister.getDatum().getOffPowerCont() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getOffPowerCont().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getPowerHv() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getPowerHv().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }


                    if (coachStatusRegister.getDatum().getOffHiDipClear() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getOffHiDipClear().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }


                    if (coachStatusRegister.getDatum().getHiDipClear() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getHiDipClear().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }


                    if (coachStatusRegister.getDatum().getLower() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getLower().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }


                    if (coachStatusRegister.getDatum().getOffControlCont() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getOffControlCont().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }


                    if (coachStatusRegister.getDatum().getControlHv() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getControlHv().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getLoadTest() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getLoadTest().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getPcpClear() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getPcpClear().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getBuFormation() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getBuFormation().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }


                    if (coachStatusRegister.getDatum().getRakeFormation() != null) {
                        statusArrayList.add(parseDateReceive(coachStatusRegister.getDatum().getRakeFormation().toString()));
                    } else {
                        statusArrayList.add(" ");
                    }

                    if (coachStatusRegister.getDatum().getRemarks() != null) {
                        statusArrayList.add(coachStatusRegister.getDatum().getRemarks());
                    } else {
                        statusArrayList.add(" ");
                    }

                    coachStatusEditAdapter.notifyDataSetChanged();
                    rakeNameTv.setText(String.format("Rake Number : %s", rakeNum));
                    cardView.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Error fetching status. Try Again", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<CoachStatusRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching status. Try Again", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

    }


}
