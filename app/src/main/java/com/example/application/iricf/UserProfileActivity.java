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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{

    public static final String TOKEN = "token";
    public static final String RAKE_NUM = "rakeNum";

    @BindView(R.id.create_rake_button)
    Button buttonCreateRake;

    @BindView(R.id.rakes_edit_rv)
    RecyclerView rakesEditRv;

    @BindView(R.id.rakes_edit_card)
    CardView rakesEditCard;

    @BindView(R.id.user_profile_progress)
    ProgressBar progressBar;

    SharedPreferences preferences;
    CoachStatusAdapter rakesAdapter;
    ArrayList<String> rakeNames,zonesArrayList;
    List<RakeName> rakeList;
    ApiInterface apiInterface;
    String token,railwayName,rakeNum;
    AlertDialog b;
    EditText addDialogEt;
    Spinner addDialogSpinner;
    Button dialogAddButton,dialogCancelButton;
    ArrayAdapter<String> zoneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        buttonCreateRake.setOnClickListener(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");

        rakeNames = new ArrayList<>();
        rakeList = new ArrayList<>();
        zonesArrayList = new ArrayList<>();
        getRakes();

        rakesAdapter = new CoachStatusAdapter(this,rakeNames);

        rakesEditRv.setLayoutManager(new LinearLayoutManager(this));
        rakesEditRv.setAdapter(rakesAdapter);

        rakesAdapter.setOnClickListener(new CoachStatusAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                openCoaches(view,position);
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_rake_button:
                createRakeDialog();
                break;
        }
    }

    private void createRakeDialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_rake_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.show();

        addDialogEt = dialogView.findViewById(R.id.add_rake_dialog_et);
        dialogAddButton = dialogView.findViewById(R.id.add_rake_dialog_button);
        dialogCancelButton = dialogView.findViewById(R.id.add_rake_dialog_cancel_button);
        addDialogSpinner = dialogView.findViewById(R.id.add_rake_dialog_spinner);


        zonesArrayList.add("NR");
        zonesArrayList.add("NER");
        zonesArrayList.add("NFR");
        zonesArrayList.add("ER");
        zonesArrayList.add("SER");
        zonesArrayList.add("SCR");
        zonesArrayList.add("SR");
        zonesArrayList.add("CR");
        zonesArrayList.add("WR");
        zonesArrayList.add("SWR");
        zonesArrayList.add("NWR");
        zonesArrayList.add("WCR");
        zonesArrayList.add("NCR");
        zonesArrayList.add("SECR");
        zonesArrayList.add("ECoR");
        zonesArrayList.add("ECR");
        zonesArrayList.add("MTP");
        zonesArrayList.add("KR");


        zoneAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, zonesArrayList);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addDialogSpinner.setAdapter(zoneAdapter);
        addDialogSpinner.setOnItemSelectedListener(this);

        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                zonesArrayList.clear();
            }
        });

        dialogAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRake();
            }
        });


    }

    private void createRake() {

        rakeNum = addDialogEt.getText().toString().trim();
        if(rakeNum.isEmpty()){
            addDialogEt.setError("Enter a rake number");
            addDialogEt.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        Call<PostResponse> call = apiInterface.createRake(token,railwayName,rakeNum);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                int status = response.code();
                if(status == 200){
                    Toast.makeText(getApplicationContext(),"Rake Created Successfully",Toast.LENGTH_SHORT).show();
                    rakeNames.add(railwayName + rakeNum);
                    rakeList.add(new RakeName(railwayName,rakeNum));
                    rakesAdapter.notifyDataSetChanged();
                    b.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(),"Error Creating Rake. Try Again",Toast.LENGTH_SHORT).show();
                }

                zonesArrayList.clear();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error Creating Rake. Try Again",Toast.LENGTH_SHORT).show();
                zonesArrayList.clear();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void openCoaches(View view,int position) {

        Intent coachesIntent = new Intent(getApplicationContext(),CoachesEdit.class);
        coachesIntent.putExtra(RAKE_NUM,rakeList.get(position).getRakeNum());
        startActivity(coachesIntent);

    }


    private void getRakes() {

        Call<RakeRegister> call = apiInterface.getRakes(token);

        call.enqueue(new Callback<RakeRegister>() {
            @Override
            public void onResponse(Call<RakeRegister> call, Response<RakeRegister> response) {
                Integer statusCode = response.code();
                if(statusCode == 200){
                    RakeRegister rakeRegister = response.body();
                    rakeList = rakeRegister.getRakes();
                    for(int i=0 ; i<rakeList.size() ; i++){
                        StringBuilder sb = new StringBuilder();
                        sb.append(rakeList.get(i).getRailway());
                        sb.append(rakeList.get(i).getRakeNum());
                        String rakeName = sb.toString();
                        rakeNames.add(rakeName);
                    }
                    rakesAdapter.notifyDataSetChanged();
                    rakesEditCard.setVisibility(View.VISIBLE);

                }else {
                    Toast.makeText(getApplicationContext(),"Error fetching data. Try again.",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<RakeRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error fetching data. Try again.",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        railwayName = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
