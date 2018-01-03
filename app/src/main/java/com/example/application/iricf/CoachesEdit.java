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
import android.widget.ProgressBar;
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

    @BindView(R.id.edit_coach_progress)
    ProgressBar progressBar;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    CoachStatusAdapter coachesAdapter;
    List<CoachPerRake> coachPerRakesList;
    ArrayList<String> coachNumList,coachTypeList;
    String token,rakeNum;

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
        for (int i=0 ; i<rakeNum.length() ; i++){
            if(rakeNum.charAt(i) =='/'){
                rakeNum = rakeNum.substring(0,i)+'_'+rakeNum.substring(i+1);
            }

        }



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


                int status = response.code();
                if(status == 200){
                    CoachPerRakeRegister coachPerRakeRegister = response.body();
                    coachPerRakesList = coachPerRakeRegister.getCoaches();

                    for(int i=0; i<coachPerRakesList.size() ; i++){
                        coachNumList.add(coachPerRakesList.get(i).getCoachNum());
                    }

                    coachesAdapter.notifyDataSetChanged();
                    coachPerRakeCard.setVisibility(View.VISIBLE);
                }else {
                    Toast.makeText(getApplicationContext(),"Error fetching data. Try again.",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<CoachPerRakeRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error fetching data. Try again.",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });



    }
}
