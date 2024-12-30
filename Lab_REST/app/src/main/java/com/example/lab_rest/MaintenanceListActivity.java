package com.example.lab_rest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.adapter.MaintenanceAdapter;
import com.example.lab_rest.adapter.MaintenanceAdapter;
import com.example.lab_rest.model.Maintenance;
import com.example.lab_rest.model.DeleteResponse;
import com.example.lab_rest.model.User;
import com.example.lab_rest.model.Users;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.MaintenanceService;
import com.example.lab_rest.remote.MaintenanceService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaintenanceListActivity extends AppCompatActivity {

    private MaintenanceService maintenanceService;
    private RecyclerView rvMaintenanceList;
    private MaintenanceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_maintenance_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // get reference to the RecyclerView bookList
        rvMaintenanceList = findViewById(R.id.rvMaintenanceList);

        //register for context menu
        registerForContextMenu(rvMaintenanceList);

        // fetch and update book list
        updateListView();

        // get user info from SharedPreferences to get token value
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users users = spm.getUsers();
        String token = users.getUSER_TOKEN();

        // get book service instance
        maintenanceService = ApiUtils.getMaintenanceService();

        // execute the call. send the user token when sending the query
        maintenanceService.getAllMaintenances(token).enqueue(new Callback<List<Maintenance>>() {
            @Override
            public void onResponse(Call<List<Maintenance>> call, Response<List<Maintenance>> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                if (response.code() == 200) {
                    // Get list of book object from response
                    List<Maintenance> maintenances = response.body();

                    // initialize adapter
                    adapter = new MaintenanceAdapter(getApplicationContext(), maintenances);

                    // set adapter to the RecyclerView
                    rvMaintenanceList.setAdapter(adapter);

                    // set layout to recycler view
                    rvMaintenanceList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    // add separator between item in the list
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvMaintenanceList.getContext(),
                            DividerItemDecoration.VERTICAL);
                    rvMaintenanceList.addItemDecoration(dividerItemDecoration);
                }
                else if (response.code() == 401) {
                    // invalid token, ask user to relogin
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    // server return other error
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Maintenance>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_LONG).show();
                Log.e("MyApp:", t.toString());
            }
        });
    }

    private void updateListView() {
        // get user info from SharedPreferences to get token value
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users users = spm.getUsers();
        String token = users.getUSER_TOKEN();

        // get book service instance
        maintenanceService = ApiUtils.getMaintenanceService();

        // execute the call. send the user token when sending the query
        maintenanceService.getAllMaintenances(token).enqueue(new Callback<List<Maintenance>>() {
            @Override
            public void onResponse(Call<List<Maintenance>> call, Response<List<Maintenance>> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());

                if (response.code() == 200) {
                    // Get list of book object from response
                    List<Maintenance> maintenances = response.body();

                    // initialize adapter
                    adapter = new MaintenanceAdapter(getApplicationContext(), maintenances);

                    // set adapter to the RecyclerView
                    rvMaintenanceList.setAdapter(adapter);

                    // set layout to recycler view
                    rvMaintenanceList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    // add separator between item in the list
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvMaintenanceList.getContext(),
                            DividerItemDecoration.VERTICAL);
                    rvMaintenanceList.addItemDecoration(dividerItemDecoration);
                }
                else if (response.code() == 401) {
                    // invalid token, ask user to relogin
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    // server return other error
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Maintenance>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting to the server", Toast.LENGTH_LONG).show();
                Log.e("MyApp:", t.toString());
            }
        });
    }
    public void clearSessionAndRedirect() {
        // clear the shared preferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        // terminate this MainActivity
        finish();

        // forward to Login Page
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);

    }

    /**
     * Fetch data for ListView
     */


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.maintenance_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Maintenance selectedMaintenance = adapter.getSelectedItem();
        Log.d("MyApp", "selected "+selectedMaintenance.toString());    // debug purpose

        if (item.getItemId() == R.id.menu_details) {    // user clicked details contextual menu
            doViewDetails(selectedMaintenance);
        }
        else if (item.getItemId() == R.id.menu_delete) {
            // user clicked the delete contextual menu
            doDeleteMaintenance(selectedMaintenance);
        }
        else if (item.getItemId() == R.id.menu_update) {
            // user clicked the update contextual menu
            doUpdateMaintenance(selectedMaintenance);
        }

        return super.onContextItemSelected(item);
    }

    private void doUpdateMaintenance(Maintenance selectedMaintenance) {
        Log.d("MyApp:", "updating maintenance: " + selectedMaintenance.toString());
        // forward user to UpdateMaintenanceActivity, passing the selected book id
        Intent intent = new Intent(getApplicationContext(), UpdateMaintenanceActivity.class);
        intent.putExtra("maintenance_id", selectedMaintenance.getMaintenanceID());
        startActivity(intent);
    }

    private void doViewDetails(Maintenance selectedMaintenance) {
        Log.d("MyApp:", "viewing details: " + selectedMaintenance.toString());
        // forward user to MaintenanceDetailsActivity, passing the selected book id
        Intent intent = new Intent(getApplicationContext(), MaintenanceDetailsActivity.class);
        intent.putExtra("maintenance_id", selectedMaintenance.getMaintenanceID());
        startActivity(intent);
    }

    /**
     * Delete book record. Called by contextual menu "Delete"
     * @param selectedMaintenance - book selected by user
     */
    private void doDeleteMaintenance(Maintenance selectedMaintenance) {
        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users users = spm.getUsers();

        // prepare REST API call
        MaintenanceService maintenanceService = ApiUtils.getMaintenanceService();
        Call<DeleteResponse> call = maintenanceService.deleteMaintenance(users.getUSER_TOKEN(), selectedMaintenance.getMaintenanceID());

        // execute the call
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.code() == 200) {
                    // 200 means OK
                    displayAlert("Maintenance successfully deleted");
                    // update data in list view
                    updateListView();
                } else {
                    displayAlert("Maintenance failed to delete");
                    Log.e("MyApp:", response.raw().toString());
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                displayAlert("Error [" + t.getMessage() + "]");
                Log.e("MyApp:", t.getMessage());
            }
        });
    }

    /**
     * Displaying an alert dialog with a single button
     * @param message - message to be displayed
     */
    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void floatingAddMaintenanceClicked (View view){
        Intent intent = new Intent(getApplicationContext(), NewMaintenanceActivity.class);
        startActivity(intent);
    }
}