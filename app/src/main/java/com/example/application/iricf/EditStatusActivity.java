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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.application.iricf.Utils.parseDateReceive;
import static com.example.application.iricf.Utils.parseDateSend;

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
    String token, coachNum, shellRec, intake, agency, coupler, ewPanel, roofTray, htTray, htEquip, highDip, ufTray, ufTrans, ufWire, offRoof, roofClear, offEw, ewClear, mechPan, offTf, tfClear, tfProv, lfLoad, offPow, powerHv, offDip, dipClear, lower, offCont, contHv, loadTest, pcpClear, buForm, rakeForm, remarks, positionName;
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
                openDialog(position);
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

    private void openDialog(final int position) {

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
            addDialogEt.setError("Value required");
            addDialogEt.requestFocus();
            return;
        }

        switch (position) {
            case 0:
                shellRec = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 1:
                intake = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 2:
                agency = editedValue;
                statusArrayList.set(position, editedValue);
                break;
            case 3:
                coupler = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 4:
                ewPanel = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 5:
                roofTray = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 6:
                htTray = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 7:
                htEquip = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 8:
                highDip = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 9:
                ufTray = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 10:
                ufTrans = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 11:
                ufWire = editedValue;
                statusArrayList.set(position, editedValue);
                break;
            case 12:
                offRoof = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 13:
                roofClear = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 14:
                offEw = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 15:
                ewClear = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 16:
                mechPan = editedValue;
                statusArrayList.set(position, editedValue);
                break;
            case 17:
                offTf = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 18:
                tfClear = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 19:
                tfProv = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 20:
                lfLoad = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 21:
                offPow = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 22:
                powerHv = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 23:
                offDip = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 24:
                dipClear = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 25:
                lower = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 26:
                offCont = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 27:
                contHv = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 28:
                loadTest = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 29:
                pcpClear = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 30:
                buForm = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 31:
                rakeForm = parseDateSend(editedValue);
                statusArrayList.set(position, editedValue);
                break;
            case 32:
                remarks = editedValue;
                statusArrayList.set(position, editedValue);
                break;
        }

        loadingDialog.show();
        Call<PostResponse> call = apiInterface.updateStatus(token, coachNum, shellRec, intake, agency, coupler, ewPanel,
                roofTray, htTray, htEquip, highDip, ufTray, ufTrans, ufWire, offRoof, roofClear, offEw, ewClear, mechPan, offTf, tfClear,
                tfProv, lfLoad, offPow, powerHv, offDip, dipClear, lower, offCont, contHv, loadTest, pcpClear, buForm,
                rakeForm, remarks);

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
