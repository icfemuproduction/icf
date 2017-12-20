package com.example.application.iricf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinePositionActivity extends AppCompatActivity {

    @BindView(R.id.production_line_button)
    Button productionLineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);

        ButterKnife.bind(this);

        productionLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LinePositionActivity.this,ProductionLineActivity.class));
            }
        });

    }
}
