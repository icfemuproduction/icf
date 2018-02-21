package com.example.application.iricf;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllUsersActivity extends AppCompatActivity {

    public static final String TOKEN = "token";

    @BindView(R.id.all_users_card)
    CardView allUsersCard;

    @BindView(R.id.all_users_rv)
    RecyclerView allUsersRv;

    @BindView(R.id.all_users_progress)
    ProgressBar progressBar;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    String token;
    AllUsersAdapter allUsersAdapter;
    List<Profile> profiles;
    ArrayList<Profile> profileArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");

        profiles = new ArrayList<>();
        profileArrayList = new ArrayList<>();


        allUsersAdapter = new AllUsersAdapter(this,profileArrayList);
        allUsersRv.setLayoutManager(new LinearLayoutManager(this));
        allUsersRv.setAdapter(allUsersAdapter);
        fetchUsers();

    }

    private void fetchUsers() {

        Log.e("SAN","FEtching");
        Call<AllProfileRegister> call = apiInterface.getAllUsers(token);
        call.enqueue(new Callback<AllProfileRegister>() {
            @Override
            public void onResponse(Call<AllProfileRegister> call, Response<AllProfileRegister> response) {
                int status = response.body().getStatus();
                if(status == 200){

                    AllProfileRegister allProfileRegister = response.body();
                    profiles = allProfileRegister.getData();
                    //profileArrayList= profiles

                    Iterator<Profile> itrTemp = profiles.iterator();
                    while(itrTemp.hasNext()){
                        Profile temp = itrTemp.next();
                        profileArrayList.add(temp);
                    }

                    allUsersAdapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(getApplicationContext(),"Cannot fetch data. Try Again later.",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<AllProfileRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Cannot fetch data. Try Again later.",Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
}
