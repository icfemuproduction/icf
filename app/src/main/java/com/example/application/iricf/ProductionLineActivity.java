package com.example.application.iricf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductionLineActivity extends AppCompatActivity {

    @BindView(R.id.production_one_rv)
    RecyclerView productionOneRv;

    ProdLineAdapter prodOneAdapter;
    ArrayList<String> tempArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_production_line);

        ButterKnife.bind(this);
        tempArray.add("lksm64");
        tempArray.add("lsdm64");
        tempArray.add("sdfm64");
        tempArray.add("lsm64");
        tempArray.add("assm64");
        tempArray.add("assm64");
        tempArray.add("lkds64");
        tempArray.add("lktr64");
        tempArray.add("ewqm64");
        tempArray.add("lker54");

        prodOneAdapter = new ProdLineAdapter(this,tempArray);
        productionOneRv.setLayoutManager(new LinearLayoutManager(this));
        productionOneRv.setAdapter(prodOneAdapter);
    }
}
