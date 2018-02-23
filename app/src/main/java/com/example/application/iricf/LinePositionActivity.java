package com.example.application.iricf;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinePositionActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TOKEN = "token";
    public static final String ROLE = "role";

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

    @BindView(R.id.shop36_button)
    Button shop36Button;

    @BindView(R.id.coach_search_bar)
    AutoCompleteTextView coachSearchBar;

    @BindView(R.id.get_coach_position_button)
    ImageView getCoachPositionButton;

    @BindView(R.id.update_position_button)
    Button updatePositionButton;

    SharedPreferences preferences;
    String token, role, editCoachPosition;
    Integer line, stage;
    ApiInterface apiInterface;
    ArrayList<String> coachPositionList;
    ArrayAdapter<String> coachPositionAdapter;
    AlertDialog positionDialog, editPositionDialog;
    Spinner editCoachPosSpinner;
    EditText editCoachLineNoEt, editCoachStageNoEt, editCoachNameEt;
    Button coachPositionCancelButton, coachPositionUpdateButton;
    TextView showCoachLineName, showCoachLineNo, showCoachLineStage;
    Button okButton;
    AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        ButterKnife.bind(this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        ((TextView) dialogView.findViewById(R.id.progressDialog_textView)).setText(R.string.loading);
        loadingDialog = new android.support.v7.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        coachPositionList = new ArrayList<>();
        coachPositionList.add("Shell");
        coachPositionList.add("Production");
        coachPositionList.add("Commission");
        coachPositionList.add("Despatch");
        coachPositionList.add("Paint");
        coachPositionList.add("Out");

        coachPositionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, coachPositionList);
        coachPositionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN, "");
        role = preferences.getString(ROLE, null);


        productionLineButton.setOnClickListener(this);
        dispatchShedButton.setOnClickListener(this);
        outShedButton.setOnClickListener(this);
        paintShopButton.setOnClickListener(this);
        commissionShedButton.setOnClickListener(this);
        shellReceivedButton.setOnClickListener(this);
        getCoachPositionButton.setOnClickListener(this);
        updatePositionButton.setOnClickListener(this);
        shop36Button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.production_line_button:
                Intent prodIntent = new Intent(LinePositionActivity.this, ProductionLineActivity.class);
                startActivity(prodIntent);
                break;
            case R.id.despatch_shed_button:
                startActivity(new Intent(LinePositionActivity.this, DispatchActivity.class));
                break;
            case R.id.paint_shop_button:
                startActivity(new Intent(LinePositionActivity.this, PaintActivity.class));
                break;
            case R.id.outshed_button:
                startActivity(new Intent(LinePositionActivity.this, OutShedActivity.class));
                break;
            case R.id.shell_recieved_button:
                startActivity(new Intent(LinePositionActivity.this, ShellActivity.class));
                break;
            case R.id.commission_shed_button:
                startActivity(new Intent(LinePositionActivity.this, CommissionActivity.class));
                break;
            case R.id.get_coach_position_button:
                getCoachPosition();
                break;
            case R.id.shop36_button:
                startActivity(new Intent(LinePositionActivity.this,Shop36Activity.class));
                break;
            case R.id.update_position_button:
                if (role.equals("admin") || role.equals("write")) {
                    updatePositionDialog();
                } else {
                    Toast.makeText(getApplicationContext(), "You don't have editing privileges", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    private void updatePositionDialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_coach_position_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        editPositionDialog = dialogBuilder.create();
        editPositionDialog.show();


        editCoachPosSpinner = dialogView.findViewById(R.id.edit_coach_position_line_spinner_line);
        editCoachPosSpinner.setAdapter(coachPositionAdapter);
        editCoachPosSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editCoachPosition = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        editCoachNameEt = dialogView.findViewById(R.id.edit_coach_position_name_et_line);
        editCoachLineNoEt = dialogView.findViewById(R.id.edit_coach_position_line_et_line);
        editCoachStageNoEt = dialogView.findViewById(R.id.edit_coach_position_stage_et_line);
        coachPositionCancelButton = dialogView.findViewById(R.id.edit_coach_position_cancel_button_line);
        coachPositionUpdateButton = dialogView.findViewById(R.id.edit_coach_position_button_line);

        coachPositionCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPositionDialog.dismiss();
            }
        });


        coachPositionUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editPosition();
            }
        });


    }

    private void editPosition() {

        String coachNum = editCoachNameEt.getText().toString().trim();
        String lineString = editCoachLineNoEt.getText().toString().trim();
        String stageString = editCoachStageNoEt.getText().toString().trim();

        if (coachNum.isEmpty()) {
            editCoachNameEt.setError("Enter coach number");
            editCoachNameEt.requestFocus();
            return;
        }

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
        Call<PostResponse> call = apiInterface.updatePosition(token, coachNum, editCoachPosition, line, stage);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                int status = response.body().getStatus();
                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();

                    editPositionDialog.dismiss();
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

    private void getCoachPosition() {

        String coachNum = coachSearchBar.getText().toString().trim();
        if (coachNum.isEmpty()) {
            coachSearchBar.setError("Coach Number Required");
            coachSearchBar.requestFocus();
            return;
        }
        for (int i = 0; i < coachNum.length(); i++) {
            if (coachNum.charAt(i) == '/') {
                coachNum = coachNum.substring(0, i) + '_' + coachNum.substring(i + 1);
            }
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(coachSearchBar.getWindowToken(), 0);

        loadingDialog.show();
        Call<CoachPositionRegister> call = apiInterface.getCoachPosition(coachNum, token);
        call.enqueue(new Callback<CoachPositionRegister>() {
            @Override
            public void onResponse(Call<CoachPositionRegister> call, Response<CoachPositionRegister> response) {
                int status = response.body().getStatus();

                if (status == 200) {
                    CoachPositionRegister positionRegister = response.body();
                    Position position = positionRegister.getPosition();
                    String lineName = null, lineNo = null, lineStage = null;
                    if (position.getLineName() != null) {
                        lineName = position.getLineName().toLowerCase();
                    }
                    if (position.getLineNo() != null) {
                        lineNo = String.valueOf(position.getLineNo());
                    }
                    if (position.getStage() != null) {
                        lineStage = String.valueOf(position.getStage());
                    }
                    createDialog(lineName, lineNo, lineStage);
                    coachSearchBar.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "Error fetching data. Try Again.", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();

            }

            @Override
            public void onFailure(Call<CoachPositionRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching data. Try Again.", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void createDialog(String lineName, String lineNo, String lineStage) {

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

        if (lineName != null) {
            switch (lineName) {
                case "production":
                    showCoachLineName.setText(R.string.production_line);
                    break;
                case "commission":
                    showCoachLineName.setText(R.string.commission_shed);
                    break;
                case "out":
                    showCoachLineName.setText(R.string.out_shed);
                    break;
                case "paint":
                    showCoachLineName.setText(R.string.paint_shop);
                    break;
                case "despatch":
                    showCoachLineName.setText(R.string.dispatch_shed);
                    break;
                case "shell":
                    showCoachLineName.setText(R.string.in_shells_rec);
                    break;
            }

        }
        if (lineNo != null) {
            showCoachLineNo.setText("Line No: " + lineNo);
        }
        if (lineStage != null) {
            showCoachLineStage.setText("Stage No: " + lineStage);
        }


    }

}
