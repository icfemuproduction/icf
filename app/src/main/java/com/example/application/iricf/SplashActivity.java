package com.example.application.iricf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    public static final String LOGGED_IN = "logged_in";

    SharedPreferences preferences;
    boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isLoggedIn = preferences.getBoolean(LOGGED_IN,false);

        if(isLoggedIn){
            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            finish();
        }else {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            finish();
        }
    }
}
