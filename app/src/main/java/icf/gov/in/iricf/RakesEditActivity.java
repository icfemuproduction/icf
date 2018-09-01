package icf.gov.in.iricf;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static icf.gov.in.iricf.Utils.parseDateSend;

public class RakesEditActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public static final String TOKEN = "token";
    public static final String RAKE_NUM = "rakeNum";

    @BindView(R.id.create_rake_button)
    Button buttonCreateRake;

    @BindView(R.id.rakes_edit_rv)
    RecyclerView rakesEditRv;

    @BindView(R.id.rakes_edit_card)
    CardView rakesEditCard;

    SharedPreferences preferences;
    CoachStatusAdapter rakesAdapter;
    ArrayList<String> rakeNames, zonesArrayList;
    List<RakeName> rakeList;
    ApiInterface apiInterface;
    String token, railwayName, rakeNum, newRailway, despatchDate, fieldName;
    AlertDialog b, rakesDeleteDialog, editRakesDialog, editRakesValueDialog;
    EditText addDialogEt, rakesDeleteEt, oldRakeNumEt, rakeEditValueEt;
    Spinner addDialogSpinner;
    Button dialogAddButton, dialogCancelButton, rakesDeleteDialogButton,
            rakesDeleteDialogCancelButton, rakesEditDialogCancelButton, rakesEditDialogButton;
    ImageView railwayNameEdit, rakeNumberEdit, despatchDateEdit;
    TextView rakeEditValueTv;
    ArrayAdapter<String> zoneAdapter;
    AlertDialog loadingDialog;
    boolean newRakeChanged = false , newRailwayChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rakes_edit);
        ButterKnife.bind(this);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.progress_dialog, null);
        ((TextView) dialogView.findViewById(R.id.progressDialog_textView)).setText(R.string.loading);
        loadingDialog = new android.support.v7.app.AlertDialog.Builder(this)
                .setView(dialogView)
                .setCancelable(false)
                .create();
        loadingDialog.show();

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        buttonCreateRake.setOnClickListener(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        token = preferences.getString(TOKEN, "");

        rakeNames = new ArrayList<>();
        rakeList = new ArrayList<>();
        zonesArrayList = new ArrayList<>();
        zonesArrayList.add("NR");
        zonesArrayList.add("NER");
        zonesArrayList.add("NFR");
        zonesArrayList.add("ER");
        zonesArrayList.add("SER");
        zonesArrayList.add("SCR");
        zonesArrayList.add("SR");
        zonesArrayList.add("CR");
        zonesArrayList.add("WR");
        zonesArrayList.add("SWR");
        zonesArrayList.add("NWR");
        zonesArrayList.add("WCR");
        zonesArrayList.add("NCR");
        zonesArrayList.add("SECR");
        zonesArrayList.add("ECoR");
        zonesArrayList.add("ECR");
        zonesArrayList.add("MTP");
        zonesArrayList.add("KR");
        getRakes();

        rakesAdapter = new CoachStatusAdapter(this, rakeNames);

        rakesEditRv.setLayoutManager(new LinearLayoutManager(this));
        rakesEditRv.setAdapter(rakesAdapter);

        rakesAdapter.setOnClickListener(new CoachStatusAdapter.OnClickListener() {
            @Override
            public void itemClicked(View view, int position) {
                openCoaches(position);
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_rake_button:
                createRakeDialog();
                break;
        }
    }

    private void createRakeDialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.add_rake_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        b = dialogBuilder.create();
        b.show();

        addDialogEt = dialogView.findViewById(R.id.add_rake_dialog_et);
        dialogAddButton = dialogView.findViewById(R.id.add_rake_dialog_button);
        dialogCancelButton = dialogView.findViewById(R.id.add_rake_dialog_cancel_button);
        addDialogSpinner = dialogView.findViewById(R.id.add_rake_dialog_spinner);


        zoneAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, zonesArrayList);
        zoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addDialogSpinner.setAdapter(zoneAdapter);
        addDialogSpinner.setOnItemSelectedListener(this);

        dialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });

        dialogAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createRake();
            }
        });


    }

    private void createRake() {

        despatchDate = null;
        rakeNum = addDialogEt.getText().toString().trim().toUpperCase();
        if (rakeNum.isEmpty()) {
            addDialogEt.setError("Enter a rake number");
            addDialogEt.requestFocus();
            return;
        }

        loadingDialog.show();
        Call<PostResponse> call = apiInterface.createRake(token, railwayName, rakeNum, despatchDate);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                int status = response.body().getStatus();
                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "Rake Created Successfully", Toast.LENGTH_SHORT).show();
                    rakeNames.add(railwayName + rakeNum);
                    rakeList.add(new RakeName(railwayName, rakeNum, null));
                    rakesAdapter.notifyDataSetChanged();
                    b.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Error Creating Rake. Try Again", Toast.LENGTH_SHORT).show();
                }

                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error Creating Rake. Try Again", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void openCoaches(int position) {

        Intent coachesIntent = new Intent(getApplicationContext(), CoachesEdit.class);
        coachesIntent.putExtra(RAKE_NUM, rakeList.get(position).getRakeNum());
        startActivity(coachesIntent);

    }

    private void getRakes() {

        Call<RakeRegister> call = apiInterface.getRakes(token);

        call.enqueue(new Callback<RakeRegister>() {
            @Override
            public void onResponse(Call<RakeRegister> call, Response<RakeRegister> response) {
                Integer statusCode = response.body().getStatus();
                if (statusCode == 200) {
                    rakeList.clear();
                    RakeRegister rakeRegister = response.body();
                    rakeList = rakeRegister.getRakes();
                    for (int i = 0; i < rakeList.size(); i++) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(rakeList.get(i).getRailway());
                        sb.append(rakeList.get(i).getRakeNum());
                        String rakeName = sb.toString();
                        rakeNames.add(rakeName);
                    }
                    rakesAdapter.notifyDataSetChanged();
                    rakesEditCard.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(getApplicationContext(), "Error fetching data. Try again.", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();

            }

            @Override
            public void onFailure(Call<RakeRegister> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error fetching data. Try again.", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

    }

    private void deleteRakesDialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.rakes_delete_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        rakesDeleteDialog = dialogBuilder.create();
        rakesDeleteDialog.show();

        rakesDeleteEt = dialogView.findViewById(R.id.rake_delete_dialog_et);
        rakesDeleteDialogButton = dialogView.findViewById(R.id.rake_delete_dialog_delete_button);
        rakesDeleteDialogCancelButton = dialogView.findViewById(R.id.rake_delete_dialog_cancel_button);

        rakesDeleteDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rakesDeleteDialog.dismiss();
            }
        });

        rakesDeleteDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRake();
            }
        });

    }

    private void deleteRake() {

        String rakeNum = rakesDeleteEt.getText().toString().toUpperCase().trim();

        if (rakeNum.isEmpty()) {
            rakesDeleteEt.setError("Enter a rake number");
            rakesDeleteEt.requestFocus();
            return;
        }

        final String rakenum = rakeNum;
        for (int i = 0; i < rakeNum.length(); i++) {
            if (rakeNum.charAt(i) == '/') {
                rakeNum = rakeNum.substring(0, i) + '_' + rakeNum.substring(i + 1);
            }
        }

        loadingDialog.show();
        Call<PostResponse> call = apiInterface.deleteRake(rakeNum, token);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                int status = response.body().getStatus();

                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "Successfully Deleted.", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < rakeNames.size(); i++) {
                        if (rakeList.get(i).getRakeNum().equalsIgnoreCase(rakenum)) {
                            rakeNames.remove(i);
                            rakeList.remove(i);

                            rakesAdapter.notifyDataSetChanged();
                        }
                    }
                    rakesDeleteDialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(), "Error Deleting Rake. Try Again", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error Deleting Rake. Try Again", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });
    }

    private void editRakesDialog() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(true);
        editRakesDialog = dialogBuilder.create();
        editRakesDialog.show();

        TextView textView = dialogView.findViewById(R.id.edit_1_text);
        textView.setText("Railway Name");

        textView = dialogView.findViewById(R.id.edit_2_text);
        textView.setText("Rake Number");

        textView = dialogView.findViewById(R.id.edit_3_text);
        textView.setText("Despatch Date");

        railwayNameEdit = dialogView.findViewById(R.id.edit_1);
        railwayNameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editRakesValueDialog("Railway Name");
            }
        });
        rakeNumberEdit = dialogView.findViewById(R.id.edit_2);
        rakeNumberEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editRakesValueDialog("New Rake Number");
            }
        });
        despatchDateEdit = dialogView.findViewById(R.id.edit_3);
        despatchDateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  editRakesValueDialog("Despatch Date");
            }
        });


    }

    private void editRakesValueDialog(final String name){

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_value_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        editRakesValueDialog = dialogBuilder.create();
        editRakesValueDialog.show();
        editRakesDialog.dismiss();

        TextView oldName = dialogView.findViewById(R.id.edit_old_name_textview);
        oldName.setText("Old Rake Number");
        rakeEditValueTv = dialogView.findViewById(R.id.edit_new_value_textview);
        oldRakeNumEt = dialogView.findViewById(R.id.edit_old_name_et);
        rakeEditValueEt = dialogView.findViewById(R.id.edit_new_value_et);
        rakesEditDialogButton = dialogView.findViewById(R.id.edit_value_dialog_edit_button);
        rakesEditDialogCancelButton = dialogView.findViewById(R.id.edit_value_dialog_cancel_button);

        rakeEditValueTv.setText(name);

        rakesEditDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editRakes(name);
            }
        });

        rakesEditDialogCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editRakesValueDialog.cancel();
            }
        });


    }

    private void editRakes(String name) {

        final String oldRakeNum = oldRakeNumEt.getText().toString().trim().toUpperCase();
        String rakeEditValue = rakeEditValueEt.getText().toString().trim().toUpperCase();
        final String tempEditValue = rakeEditValue;

        if (oldRakeNum.isEmpty()) {
            oldRakeNumEt.setError("Enter old rake number");
            oldRakeNumEt.requestFocus();
            return;
        }

        if (rakeEditValue.isEmpty()) {
            rakeEditValueEt.setError("Enter a value");
            rakeEditValueEt.requestFocus();
            return;
        }

        Map<String, String> editRakeMap = new HashMap<>();
        if(name.equalsIgnoreCase("Railway Name")){
            newRailwayChanged = true;
            fieldName = "railway";
            editRakeMap.put("railway",rakeEditValue);
        }else if(name.equalsIgnoreCase("New Rake Number")){
            newRakeChanged = true;
            fieldName = "rake_num";
            editRakeMap.put("rake_num",rakeEditValue);
        }else if(name.equalsIgnoreCase("Despatch Date")){
            rakeEditValue = parseDateSend(rakeEditValue);
            fieldName = "despatch";
            editRakeMap.put("despatch",rakeEditValue);
        }


        loadingDialog.show();
        Call<PostResponse> call = apiInterface.editRake(fieldName, token, oldRakeNum, editRakeMap);
        call.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                int status = response.body().getStatus();

                if (status == 200) {
                    editRakesValueDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Edited Successfully.", Toast.LENGTH_SHORT).show();
                    for (int i = 0; i < rakeNames.size(); i++) {
                        if (rakeList.get(i).getRakeNum().equalsIgnoreCase(oldRakeNum)) {

                            if (newRakeChanged) {
                                rakeNames.set(i, rakeList.get(i).getRailway() + tempEditValue);
                                rakeList.set(i, new RakeName(rakeList.get(i).getRailway(), tempEditValue, null));
                            } else if(newRailwayChanged){
                                rakeNames.set(i, tempEditValue  + rakeList.get(i).getRakeNum());
                                rakeList.set(i, new RakeName(tempEditValue, rakeList.get(i).getRakeNum(), null));
                            }
                            rakesAdapter.notifyDataSetChanged();
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Error editing rake. Try again later.", Toast.LENGTH_SHORT).show();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error editing rake. Try again later.", Toast.LENGTH_SHORT).show();
                loadingDialog.dismiss();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rake_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.del_rake_menu:
                deleteRakesDialog();
                break;
            case R.id.edit_rake_menu:
                editRakesDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        railwayName = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
