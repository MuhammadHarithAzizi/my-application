package com.example.lab_rest;

import android.content.Intent;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab_rest.model.Book;
import com.example.lab_rest.model.Maintenance;
import com.example.lab_rest.model.User;
import com.example.lab_rest.model.Users;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.MaintenanceService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaintenanceDetailsActivity extends AppCompatActivity {

    private MaintenanceService maintenanceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_maintenance_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // retrieve Maintenance details based on selected id

        // get Maintenance id sent by MaintenanceListActivity, -1 if not found
        Intent intent = getIntent();
        int maintenanceId = intent.getIntExtra("maintenance_id", -1);

        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users users = spm.getUsers();
        String token = users.getUSER_TOKEN();

        // get Maintenance service instance
        maintenanceService = ApiUtils.getMaintenanceService();

        // execute the API query. send the token and Maintenance id
        maintenanceService.getMaintenance(token, maintenanceId).enqueue(new Callback<Maintenance>() {

            public void onResponse(Call<Maintenance> call, Response<Maintenance> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                if (response.code() == 200) {
                    // server return success

                    // get book object from response
                    Maintenance maintenance = response.body();

                    // get references to the view elements
                    TextView tvType = findViewById(R.id.tvType);
                    TextView tvCost = findViewById(R.id.tvCost);
                    TextView tvMaintenanceDate = findViewById(R.id.tvMaintenanceDate);
                    TextView tvUpdatedMain = findViewById(R.id.tvUpdatedMain);

                    //Double cost = 0.0;
                    // set values
                    tvType.setText(maintenance.getType());

                    double cost = maintenance.getCost();
                    tvCost.setText(String.format("%.2f", cost));

                    tvMaintenanceDate.setText(maintenance.getMaintenanceDate());
                    tvUpdatedMain.setText(maintenance.getUpdateMaintenanceDate());

                }
                else if (response.code() == 401) {
                    // unauthorized error. invalid token, ask user to relogin
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                }
                else {
                    // server return other error
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Maintenance> call, Throwable t) {
                Toast.makeText(null, "Error connecting", Toast.LENGTH_LONG).show();
            }
        });
    }


    public void clearSessionAndRedirect () {
        // clear the shared preferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        // terminate this activity
        finish();

        // forward to Login Page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }
}