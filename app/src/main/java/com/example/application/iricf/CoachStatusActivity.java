package com.example.application.iricf;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoachStatusActivity extends AppCompatActivity {

    @BindView(R.id.rake_search_bar)
    AutoCompleteTextView rakeSearchBar;

    @BindView(R.id.rake_search_button)
    ImageView rakeSearchButton;

    @BindView(R.id.coach_status_coaches_card)
    CardView coachesCard;

    @BindView(R.id.coach_status_rv)
    RecyclerView coachesRv;

    ArrayList<String> rakeNumbersArrayList,coachNamesArrayList;
    ArrayAdapter<String> rakeNumberAdapter;
    CoachStatusAdapter coachStatusAdapter;

    ArrayList<Property> propertyArrayList;
    StatusPropertyAdapter statusPropertyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_status);
        ButterKnife.bind(this);

        propertyArrayList = new ArrayList<>();
        rakeNumbersArrayList = new ArrayList<>();
        coachNamesArrayList = new ArrayList<>();

        rakeNumbersArrayList.add("RM5C4");
        rakeNumberAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,rakeNumbersArrayList);
        rakeSearchBar.setAdapter(rakeNumberAdapter);

        coachStatusAdapter = new CoachStatusAdapter(this,coachNamesArrayList);
        coachesRv.setLayoutManager(new LinearLayoutManager(this));
        coachesRv.setAdapter(coachStatusAdapter);

        coachStatusAdapter.setOnClickListener(new CoachStatusAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {

                displaystatus(coachNamesArrayList.get(position));
            }
        });

        rakeSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                coachesCard.setVisibility(View.VISIBLE);

                coachNamesArrayList.add("D1 K");
                coachNamesArrayList.add("M1 M");
                coachNamesArrayList.add("C3 H");
                coachNamesArrayList.add("C4 K");
                coachNamesArrayList.add("M2 H");
                coachNamesArrayList.add("HC3 M");
                coachNamesArrayList.add("C5 H");
                coachNamesArrayList.add("M2 H");
                coachNamesArrayList.add("HC4 M");
                coachNamesArrayList.add("C6 K");
                coachNamesArrayList.add("M2 M");
                coachNamesArrayList.add("D2 K");
                coachStatusAdapter.notifyDataSetChanged();

            }
        });
    }

    private void displaystatus(String coachName) {

        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));
        propertyArrayList.add(new Property("abc","cde"));

        statusPropertyAdapter = new StatusPropertyAdapter(this,propertyArrayList);

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.coach_status_dialog, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog b = dialogBuilder.create();

        TextView coachNameTv = dialogView.findViewById(R.id.coach_status_dialog_name_tv);
        coachNameTv.setText(coachName);

        ImageView closeDialogButton = dialogView.findViewById(R.id.dialog_close_button);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        RecyclerView statusPropertyRv = dialogView.findViewById(R.id.coach_status_dialog_rv);
        statusPropertyRv.setLayoutManager(new LinearLayoutManager(this));
        statusPropertyRv.setAdapter(statusPropertyAdapter);


        b.show();
    }
}
