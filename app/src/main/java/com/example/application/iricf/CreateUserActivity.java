package com.example.application.iricf;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateUserActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String TOKEN = "token";

    @BindView(R.id.create_name_et)
    EditText nameEt;

    @BindView(R.id.create_username_et)
    EditText userNameEt;

    @BindView(R.id.create_password_et)
    EditText passwordEt;

    @BindView(R.id.create_spinner_role)
    Spinner roleSpinner;

    @BindView(R.id.create_user_button)
    Button createUserButton;

    ApiInterface apiInterface;
    SharedPreferences preferences;
    ArrayAdapter<String> roleAdapter;
    ArrayList<String> rolesArrayList = new ArrayList<>();
    String role, token;
    AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        ButterKnife.bind(this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        ((TextView) dialogView.findViewById(R.id.progressDialog_textView)).setText(R.string.loading);
        loadingDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN, "");
        createUserButton.setOnClickListener(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        rolesArrayList.add("write");
        rolesArrayList.add("admin");

        roleAdapter = new ArrayAdapter<>(this, R.layout.single_spinner_item, rolesArrayList);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        roleSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_user_button:
                createUser();
                break;

        }
    }

    private void createUser() {

        String name = nameEt.getText().toString().trim();
        String userName = userNameEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();


        if (name.isEmpty() && !password.isEmpty() && !userName.isEmpty()) {
            nameEt.setError("Name is required");
            nameEt.requestFocus();
            return;
        }

        if (!name.isEmpty() && password.isEmpty() && !userName.isEmpty()) {
            passwordEt.setError("Password is required");
            passwordEt.requestFocus();
            return;
        }

        if (!name.isEmpty() && !password.isEmpty() && userName.isEmpty()) {
            userNameEt.setError("Username is required");
            userNameEt.requestFocus();
            return;
        }

        if (name.isEmpty() && !password.isEmpty() && userName.isEmpty()) {
            userNameEt.setError("Username is required");
            nameEt.setError("Name is required");
            nameEt.requestFocus();
            return;
        }

        if (name.isEmpty() && password.isEmpty() && !userName.isEmpty()) {
            passwordEt.setError("Password is required");
            nameEt.setError("Name is required");
            nameEt.requestFocus();
            return;
        }

        if (!name.isEmpty() && password.isEmpty() && userName.isEmpty()) {
            userNameEt.setError("Username is required");
            passwordEt.setError("Password is required");
            userNameEt.requestFocus();
            return;
        }

        if (name.isEmpty() && password.isEmpty() && userName.isEmpty()) {
            nameEt.setError("Name is required");
            userNameEt.setError("Username is required");
            passwordEt.setError("Password is required");
            nameEt.requestFocus();
            return;
        }

        loadingDialog.show();
        Call<PostResponse> call = apiInterface.createUser(name, userName, password, role, token);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {

                int status = response.code();

                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "User Created Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error Creating User. Try Again Later.", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error Creating User. Try Again Later.", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        role = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
