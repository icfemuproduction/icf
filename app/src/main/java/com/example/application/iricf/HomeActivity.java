package com.example.application.iricf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    public static final String ROLE = "role";
    public static final String TOKEN = "token";

    @BindView(R.id.coach_status_button)
    Button buttonCoachStatus;

    @BindView(R.id.line_position_button)
    Button buttonLinePosition;

    @BindView(R.id.despatch_rakes_button)
    Button buttonDespatchRakes;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String role = null, token;
    AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        ((TextView) dialogView.findViewById(R.id.progressDialog_textView)).setText(R.string.loading);
        loadingDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        role = preferences.getString(ROLE, null);
        token = preferences.getString(TOKEN, "");
        if (role == null) {
            getRole();
        }


        buttonLinePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, LinePositionActivity.class));
            }
        });


        buttonCoachStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CoachStatusActivity.class));
            }
        });

        buttonDespatchRakes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, DespatchedRakesActivity.class));
            }
        });


    }

    private void getRole() {

        Call<SingleProfileRegister> call = apiInterface.getUserProfile(token);
        call.enqueue(new Callback<SingleProfileRegister>() {
            @Override
            public void onResponse(Call<SingleProfileRegister> call, Response<SingleProfileRegister> response) {

                int statusCode = response.body().getStatus();

                if (statusCode == 200) {
                    SingleProfileRegister profileRegister = response.body();
                    role = profileRegister.getProfile().getRole();
                    editor.putString(ROLE, role)
                            .apply();
                }

            }

            @Override
            public void onFailure(Call<SingleProfileRegister> call, Throwable t) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_in_menu:
                if (role != null) {
                    editActivity();
                } else {
                    getRole();
                }
                break;
            case R.id.create_user_menu:
                if (role != null) {
                    createUserActivity();
                } else {
                    getRole();
                }
                break;
            case R.id.get_all_users_menu:
                if (role != null) {
                    getAllUsers();
                } else {
                    getRole();
                }
                break;
            case R.id.log_out:
                logUserOut();

        }

        return super.onOptionsItemSelected(item);
    }

    private void getAllUsers() {
        if (role.equals("admin")) {
            startActivity(new Intent(getApplicationContext(), AllUsersActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), "You don't have privileges to view users", Toast.LENGTH_SHORT).show();
        }
    }

    private void logUserOut() {
        loadingDialog.show();
        if(role != null && !role.equalsIgnoreCase("read")){
            Call<PostResponse> call = apiInterface.logOut(token);
            call.enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    Integer status = response.body().getStatus();

                    if (status == 200) {
                        preferences.edit().clear().apply();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error Connecting. Try Again.", Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                }

                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Error Connecting. Try Again.", Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            });
        } else {
            preferences.edit().clear().apply();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }


    }

    private void createUserActivity() {
        if (role.equals("admin")) {
            startActivity(new Intent(getApplicationContext(), CreateUserActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), "You don't have privileges to create user", Toast.LENGTH_SHORT).show();
        }
    }

    private void editActivity() {
        if (role.equals("admin") || role.equals("write")) {
            startActivity(new Intent(getApplicationContext(), RakesEditActivity.class));
        } else {
            Toast.makeText(getApplicationContext(), "You don't have editing privileges", Toast.LENGTH_SHORT).show();
        }
    }
}
