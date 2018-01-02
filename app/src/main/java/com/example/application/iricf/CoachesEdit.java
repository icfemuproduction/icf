package com.example.application.iricf;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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

public class CoachesEdit extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

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
    ArrayList<String> coachNumList,coachTypeList;
    ArrayAdapter<String> coachTypeAdapter;
    String token,rakeNum,coachNumber,coachType;
    AlertDialog b;
    EditText addDialogEt;
    Spinner addDialogSpinner;
    Button dialogAddButton,dialogCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coaches_edit);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");

        Bundle bundle = getIntent().getExtras();
        rakeNum = bundle.getString(RAKE_NUM);
        rakeNameTv.setText(rakeNum);

        coachPerRakesList = new ArrayList<>();
        coachTypeList = new ArrayList<>();
        coachNumList = new ArrayList<>();


        createCoachButton.setOnClickListener(this);
        coachesAdapter = new CoachStatusAdapter(this,coachNumList);
        coachPerRakeRv.setLayoutManager(new LinearLayoutManager(this));
        coachPerRakeRv.setAdapter(coachesAdapter);

        coachesAdapter.setOnClickListener(new CoachStatusAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {

                editStatus(view,position);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        coachNumList.clear();
        coachesAdapter.notifyDataSetChanged();
        coachPerRakeCard.setVisibility(View.INVISIBLE);
        getCoaches(rakeNum);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_coach_button:
                Intent intent = new Intent(getApplicationContext(),AddCoachActivity.class);
                intent.putExtra(RAKE_NUM,rakeNum);
                startActivity(intent);
                break;
        }
    }


    private void editStatus(View view,int position) {
        Intent intent = new Intent(getApplicationContext(),EditStatusActivity.class);
        intent.putExtra(COACH_NUM,coachNumList.get(position));
        startActivity(intent);
    }

    private void getCoaches(String rakeNum) {

        Call<CoachPerRakeRegister> call = apiInterface.getRakeCoaches(rakeNum,token);
        call.enqueue(new Callback<CoachPerRakeRegister>() {
            @Override
            public void onResponse(Call<CoachPerRakeRegister> call, Response<CoachPerRakeRegister> response) {
                CoachPerRakeRegister coachPerRakeRegister = response.body();

                coachPerRakesList = coachPerRakeRegister.getCoaches();

                for(int i=0; i<coachPerRakesList.size() ; i++){
                    coachNumList.add(coachPerRakesList.get(i).getCoachNum());
                }

                coachesAdapter.notifyDataSetChanged();
                coachPerRakeCard.setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Call<CoachPerRakeRegister> call, Throwable t) {

            }
        });



    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        coachType =  adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

     /*private void createCoachDialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_coach_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.show();

        addDialogEt = dialogView.findViewById(R.id.add_coach_dialog_et);
        dialogAddButton = dialogView.findViewById(R.id.add_coach_dialog_button);
        dialogCancelButton = dialogView.findViewById(R.id.add_coach_dialog_cancel_button);
        addDialogSpinner = dialogView.findViewById(R.id.add_coach_dialog_spinner);

        coachTypeList.add("trailer");
        coachTypeList.add("driving");
        coachTypeList.add("motor");
        coachTypeList.add("handicapped");

        coachTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, coachTypeList);
        coachTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addDialogSpinner.setAdapter(coachTypeAdapter);
        addDialogSpinner.setOnItemSelectedListener(this);

        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                coachTypeList.clear();
            }
        });

        dialogAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createCoach();
            }
        });

    }

    private void createCoach(){
        coachNumber = addDialogEt.getText().toString().trim();
        if(coachNumber.isEmpty()){
            addDialogEt.setError("Enter a coach number");
            addDialogEt.requestFocus();
            return;
        }

        Call<PostResponse> call = apiInterface.createCoach(token,coachNumber,rakeNum,coachType);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                PostResponse postResponse = response.body();

                int status = postResponse.getStatus();
                if(status == 200){
                    Toast.makeText(getApplicationContext(),"Coach Created Successfully",Toast.LENGTH_SHORT).show();
                    coachNumList.add(coachNumber);
                    coachesAdapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(getApplicationContext(),"Error Creating Coach. Try Again",Toast.LENGTH_SHORT).show();
                }
                b.dismiss();
                coachTypeList.clear();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error Creating Coach. Try Again",Toast.LENGTH_SHORT).show();
                b.dismiss();
                coachTypeList.clear();
            }
        });
    }
*/
}
