package com.example.application.iricf;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String LOGGED_IN = "logged_in";
    public static final String TOKEN = "token";
    public static final String USERNAME = "username";
    public static final String ROLE = "role";

    @BindView(R.id.sign_in_username)
    EditText signInUsernameEt;

    @BindView(R.id.sign_in_password)
    EditText signInPasswordEt;

    @BindView(R.id.sign_in_button)
    Button signInButton;

    @BindView(R.id.login_progress_bar)
    ProgressBar progressBar;

    ApiInterface apiInterface;
    String token;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);


        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = preferences.edit();
        signInButton.setOnClickListener(this);

         /*myCalendar = Calendar.getInstance();


         date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        signInUsernameEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(LoginActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/
    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        signInUsernameEt.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_in_button:
                signInUser();
                break;
        }
    }

    private void signInUser() {

        final String username = signInUsernameEt.getText().toString().trim();
        String password = signInPasswordEt.getText().toString().trim();

        if(username.isEmpty() && !password.isEmpty()){
            signInUsernameEt.setError("Username is required");
            signInUsernameEt.requestFocus();
            return;
        }

        if(!username.isEmpty() && password.isEmpty()){
            signInPasswordEt.setError("Password is required");
            signInPasswordEt.requestFocus();
            return;
        }

        if(username.isEmpty() && password.isEmpty()){
            signInPasswordEt.setError("Password is required");
            signInUsernameEt.setError("Username is required");
            signInUsernameEt.requestFocus();
            return;
        }

        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(signInPasswordEt.getWindowToken(), 0);

        progressBar.setVisibility(View.VISIBLE);
        Call<LoginRegister> call = apiInterface.login(username,password);
        call.enqueue(new Callback<LoginRegister>() {
            @Override
            public void onResponse(Call<LoginRegister> call, Response<LoginRegister> response) {
                LoginRegister loginRegister = response.body();

                int status = response.code();
                if(status == 200){
                     token = loginRegister.getDatum().getToken();



                    editor.putBoolean(LOGGED_IN,true)
                            .putString(USERNAME,username)
                            .putString(TOKEN,token)
                            .apply();

                    validSignIn();
                    progressBar.setVisibility(View.INVISIBLE);

                } else {
                    Toast.makeText(getApplicationContext(),"Error SignIn",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onFailure(Call<LoginRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Failed : " + t.toString(),Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }


    private void validSignIn() {

            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

    }
}
