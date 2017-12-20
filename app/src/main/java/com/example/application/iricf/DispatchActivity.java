package com.example.application.iricf;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DispatchActivity extends AppCompatActivity {

    @BindView(R.id.dispatch_line_rv)
    RecyclerView dispatchLineRv;

    LineAdapters lineAdapter;
    ArrayList<String> coaches;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatch);
        ButterKnife.bind(this);

        coaches = new ArrayList<>();
        coaches.add("abcd123");
        coaches.add("efgh456");
        coaches.add("wxyz987");
        lineAdapter = new LineAdapters(this,coaches);
        dispatchLineRv.setLayoutManager(new LinearLayoutManager(this));
        dispatchLineRv.setAdapter(lineAdapter);

    }
}
