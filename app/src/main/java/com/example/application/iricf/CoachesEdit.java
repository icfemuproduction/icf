package com.example.application.iricf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoachesEdit extends AppCompatActivity implements View.OnClickListener {

    public static final String TOKEN = "token";
    public static final String RAKE_NUM = "rakeNum";
    public static final String COACH_NUM = "coachNum";

    @BindView(R.id.coach_per_rake_edit_rv)
    RecyclerView coachPerRakeRv;

    @BindView(R.id.coach_per_rake_edit_card)
    CardView coachPerRakeCard;

    @BindView(R.id.rakeNameTextView)
    TextView rakeNameTv;

    @BindView(R.id.create_coach_button)
    Button createCoachButton;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    CoachStatusAdapter coachesAdapter;
    List<CoachPerRake> coachPerRakesList;
    ArrayList<String> coachNumList, coachTypeList;
    ArrayAdapter<String> coachTypeAdapter;
    String token, rakeNum, editCoachType, oldCoachNum, newCoachNum, newRakeNum;
    AlertDialog coachesDeleteDialog, coachesEditDialog;
    EditText deleteCoachDialogEt, coachesEditOldEt, coachesEditNewEt, coachesEditNewRAkeEt;
    Spinner coachesEditSpinner;
    Button deleteCoachDialogButton, deleteCoachDialogCancelButton, coachesEditDialogCancelButton,
            coachesEditDialogButton;
    AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaches_edit);
        ButterKnife.bind(this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        ((TextView) dialogView.findViewById(R.id.progressDialog_textView)).setText(R.string.loading);
        loadingDialog = new android.support.v7.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN, "");

        coachTypeList = new ArrayList<>();
        coachTypeList.add("trailer");
        coachTypeList.add("driving");
        coachTypeList.add("motor");
        coachTypeList.add("handicapped");

        Bundle bundle = getIntent().getExtras();
        rakeNum = bundle != null ? bundle.getString(RAKE_NUM) : null;
        for (int i = 0; i < (rakeNum != null ? rakeNum.length() : 0); i++) {
            if (rakeNum.charAt(i) == '/') {
                rakeNum = rakeNum.substring(0, i) + '_' + rakeNum.substring(i + 1);
            }

        }
        rakeNameTv.setText(rakeNum);

        coachPerRakesList = new ArrayList<>();
        coachNumList = new ArrayList<>();


        createCoachButton.setOnClickListener(this);
        coachesAdapter = new CoachStatusAdapter(this, coachNumList);
        coachPerRakeRv.setLayoutManager(new LinearLayoutManager(this));
        coachPerRakeRv.setAdapter(coachesAdapter);

        coachesAdapter.setOnClickListener(new CoachStatusAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {

                editStatus(position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        coachNumList.clear();
        coachesAdapter.notifyDataSetChanged();
        loadingDialog.show();
        coachPerRakeCard.setVisibility(View.INVISIBLE);
        getCoaches(rakeNum);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_coach_button:
                Intent intent = new Intent(getApplicationContext(), AddCoachActivity.class);
                intent.putExtra(RAKE_NUM, rakeNum);
                startActivity(intent);
                break;
        }
    }


    private void editStatus(int position) {
        Intent intent = new Intent(getApplicationContext(), EditStatusActivity.class);
        intent.putExtra(COACH_NUM, coachNumList.get(position));
        startActivity(intent);
    }

    private void getCoaches(String rakeNum) {

        Call<CoachPerRakeRegister> call = apiInterface.getRakeCoaches(rakeNum, token);
        call.enqueue(new Callback<CoachPerRakeRegister>() {
            @Override
            public void onResponse(Call<CoachPerRakeRegister> call, Response<CoachPerRakeRegister> response) {


                int status = response.body().getStatus();
                if (status == 200) {
                    CoachPerRakeRegister coachPerRakeRegister = response.body();
                    coachPerRakesList = coachPerRakeRegister.getCoaches();

                    for (int i = 0; i < coachPerRakesList.size(); i++) {
                        coachNumList.add(coachPerRakesList.get(i).getCoachNum());
                    }

                    coachesAdapter.notifyDataSetChanged();
                    coachPerRakeCard.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Error fetching data. Try again.", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();

            }

            @Override
            public void onFailure(Call<CoachPerRakeRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching data. Try again.", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void deleteCoachDialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.coaches_delete_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        coachesDeleteDialog = dialogBuilder.create();
        coachesDeleteDialog.show();

        deleteCoachDialogButton = dialogView.findViewById(R.id.coach_delete_dialog_delete_button);
        deleteCoachDialogCancelButton = dialogView.findViewById(R.id.coach_delete_dialog_cancel_button);
        deleteCoachDialogEt = dialogView.findViewById(R.id.coach_delete_dialog_et);

        deleteCoachDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coachesDeleteDialog.dismiss();
            }
        });

        deleteCoachDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coachDelete();
            }
        });

    }

    private void coachDelete() {

        String coachNum = deleteCoachDialogEt.getText().toString().toLowerCase().trim();

        if (coachNum.isEmpty()) {
            deleteCoachDialogEt.setError("Enter a coach number");
            deleteCoachDialogEt.requestFocus();
            return;
        }

        final String coachnum = coachNum;
        for (int i = 0; i < coachNum.length(); i++) {
            if (coachNum.charAt(i) == '/') {
                coachNum = coachNum.substring(0, i) + '_' + coachNum.substring(i + 1);
            }
        }

        loadingDialog.show();
        Call<PostResponse> call = apiInterface.deleteCoach(coachNum, token);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                int status = response.body().getStatus();

                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "Successfully Deleted.", Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < coachNumList.size(); i++) {
                        if (coachNumList.get(i).equalsIgnoreCase(coachnum)) {
                            coachNumList.remove(i);
                            coachesAdapter.notifyDataSetChanged();

                        }
                    }
                    coachesDeleteDialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Error Deleting Coach. Try Again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error Deleting Coach. Try Again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void editCoachDialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_coaches_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        coachesEditDialog = dialogBuilder.create();
        coachesEditDialog.show();

        coachesEditOldEt = dialogView.findViewById(R.id.edit_coach_dialog_old_et);
        coachesEditNewEt = dialogView.findViewById(R.id.edit_coach_dialog_new_et);
        coachesEditNewRAkeEt = dialogView.findViewById(R.id.edit_coach_dialog_new_rake_et);
        coachesEditSpinner = dialogView.findViewById(R.id.edit_coach_dialog_new_spinner);
        coachesEditDialogCancelButton = dialogView.findViewById(R.id.edit_coach_dialog_cancel_button);
        coachesEditDialogButton = dialogView.findViewById(R.id.edit_coach_dialog_edit_button);

        coachTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, coachTypeList);
        coachTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coachesEditSpinner.setAdapter(coachTypeAdapter);
        coachesEditSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                editCoachType = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        coachesEditDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coachesEditDialog.dismiss();
            }
        });

        coachesEditDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCoach();
            }
        });

    }

    private void editCoach() {

        newRakeNum = null;
        newCoachNum = null;
        oldCoachNum = null;

        oldCoachNum = coachesEditOldEt.getText().toString().trim();
        newRakeNum = coachesEditNewRAkeEt.getText().toString().trim();
        newCoachNum = coachesEditNewEt.getText().toString().trim();

        if (oldCoachNum.isEmpty()) {
            coachesEditOldEt.setError("Enter old coach number");
            coachesEditOldEt.requestFocus();
            return;
        }

        if (newRakeNum.isEmpty()) {
            newRakeNum = null;
        }
        if (newCoachNum.isEmpty()) {
            newCoachNum = null;
        }

        loadingDialog.show();
        Call<PostResponse> call = apiInterface.editCoach(token, oldCoachNum, newCoachNum, newRakeNum, editCoachType);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                int status = response.body().getStatus();

                if (status == 200) {
                    coachesEditDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Edited successfully.", Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < coachNumList.size(); i++) {
                        if (coachNumList.get(i).equalsIgnoreCase(oldCoachNum)) {
                            if (newCoachNum != null) {
                                coachNumList.set(i, newCoachNum);
                                coachesAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    if (newRakeNum != null && !newRakeNum.equalsIgnoreCase(rakeNum)) {
                        for (int i = 0; i < coachNumList.size(); i++) {
                            if (coachNumList.get(i).equalsIgnoreCase(oldCoachNum)
                                    || coachNumList.get(i).equalsIgnoreCase(newCoachNum)) {
                                coachNumList.remove(i);
                                coachesAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error editing. Try again later.", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error editing. Try again later.", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.coach_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.del_coach_menu:
                deleteCoachDialog();
                break;
            case R.id.edit_coach_menu:
                editCoachDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


}
