package com.example.application.iricf;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShellActivity extends AppCompatActivity {

    public static final String TOKEN = "token";

    @BindView(R.id.shell_received_rv)
    RecyclerView shellReceivedRv;

    @BindView(R.id.shell_received_card)
    CardView cardView;

    @BindView(R.id.no_of_shells_tv)
    TextView noOfShellsTv ;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token;
    List<Position> positionArrayList;
    CoachStatusAdapter shellAdapter;
    ArrayList<String> shellNamesList;
    Integer noOfShells=0;
    AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shell);
        ButterKnife.bind(this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        ((TextView) dialogView.findViewById(R.id.progressDialog_textView)).setText(R.string.loading);
        loadingDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");

        positionArrayList = new ArrayList<>();
        shellNamesList = new ArrayList<>();
        fetchPositionData();

        shellAdapter = new CoachStatusAdapter(this,shellNamesList);
        shellReceivedRv.setLayoutManager(new LinearLayoutManager(this));
        shellReceivedRv.setAdapter(shellAdapter);
        shellAdapter.setOnClickListener(new CoachStatusAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {

            }
        });
    }

    private void fetchPositionData() {

        loadingDialog.show();
        Call<PositionRegister> call = apiInterface.getAllPosition(token);
        call.enqueue(new Callback<PositionRegister>() {
            @Override
            public void onResponse(Call<PositionRegister> call, Response<PositionRegister> response) {

                int status = response.body().getStatus();
                if(status == 200){
                    PositionRegister positionRegister = response.body();
                    positionArrayList = positionRegister.getPositionList();
                    Log.e("SAN","total size : "+ positionArrayList.size());
                    for (int i=0 ; i<positionArrayList.size() ; i++){
                        if(positionArrayList.get(i).getLineName().equalsIgnoreCase("shell")
                                || positionArrayList.get(i).getLineName().equalsIgnoreCase("paint") ){
                            shellNamesList.add(positionArrayList.get(i).getCoachNum());
                            noOfShells++;
                        }
                    }
                    shellAdapter.notifyDataSetChanged();
                    noOfShellsTv.setText("No. of Shells : " + String.valueOf(noOfShells));
                    Log.e("SAN","Size : " + shellNamesList.size());
                    cardView.setVisibility(View.VISIBLE);

                }else{
                    Toast.makeText(getApplicationContext(),"Error getting positions. Try again later"
                            ,Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PositionRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Error getting positions. Try again later"
                        ,Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

    }
}
