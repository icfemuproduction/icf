package com.example.application.iricf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinePositionActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TOKEN = "token";

    @BindView(R.id.production_line_button)
    Button productionLineButton;

    @BindView(R.id.despatch_shed_button)
    Button dispatchShedButton;

    @BindView(R.id.outshed_button)
    Button outShedButton;

    @BindView(R.id.paint_shop_button)
    Button paintShopButton;

    @BindView(R.id.commission_shed_button)
    Button commissionShedButton;

    @BindView(R.id.shell_recieved_button)
    Button shellReceivedButton;


    SharedPreferences preferences;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN,"");
        fetchPositionData();

        productionLineButton.setOnClickListener(this);
        dispatchShedButton.setOnClickListener(this);
        outShedButton.setOnClickListener(this);
        paintShopButton.setOnClickListener(this);
        commissionShedButton.setOnClickListener(this);
        shellReceivedButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.production_line_button:
                startActivity(new Intent(LinePositionActivity.this,ProductionLineActivity.class));
                break;
            case R.id.despatch_shed_button:
                startActivity(new Intent(LinePositionActivity.this,DispatchActivity.class));
                break;
            case R.id.paint_shop_button:

                break;
            case R.id.outshed_button:

                break;
            case R.id.shell_recieved_button:

                break;
            case R.id.commission_shed_button:

                break;
        }
    }

    private void fetchPositionData() {
    }
}
