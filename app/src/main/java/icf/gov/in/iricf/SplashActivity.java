package icf.gov.in.iricf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    public static final String LOGGED_IN = "logged_in";
    public static final String TOKEN = "token";

    @BindView(R.id.splash_progress)
    ProgressBar progressBar;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    boolean isLoggedIn;
    String token = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        isLoggedIn = preferences.getBoolean(LOGGED_IN, false);
        token = preferences.getString(TOKEN, null);

        if (token != null) {
            validateToken();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isLoggedIn) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                }
            }, 500);
        }

    }

    private void validateToken() {

        retrofit2.Call<SingleProfileRegister> call = apiInterface.getUserProfile(token);
        call.enqueue(new Callback<SingleProfileRegister>() {
            @Override
            public void onResponse(retrofit2.Call<SingleProfileRegister> call, Response<SingleProfileRegister> response) {

                SingleProfileRegister profileRegister = response.body();

                int statusCode = response.body().getStatus();
                Log.d("SAN", "code: " + statusCode);

                if (statusCode == 200) {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                } else if (statusCode == 401) {
                    preferences.edit().clear().apply();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } else {
                    if (isLoggedIn) {
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<SingleProfileRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error : Check Connection. Try Again.", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }


}
