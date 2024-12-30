package com.example.lab_rest;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;


import com.example.lab_rest.model.Maintenance;
import com.example.lab_rest.model.User;
import com.example.lab_rest.model.Users;
import com.example.lab_rest.remote.ApiUtils;

import com.example.lab_rest.remote.MaintenanceService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateMaintenanceActivity extends AppCompatActivity {

    private EditText txtType;
    private EditText  txtCost;

    private static TextView tvMaintenanceDate;

    private static Date createdAt;

    private Maintenance maintenance;  // current maintenance to be updated
    private Maintenance updatedMaintenance;

    /**
     * Date picker fragment class
     * Reference: https://developer.android.com/guide/topics/ui/controls/pickers
     */
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            // create a date object from selected year, month and day
            createdAt = new GregorianCalendar(year, month, day).getTime();

            // display in the label beside the button with specific date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            tvMaintenanceDate.setText(sdf.format(createdAt));
        }
    }

    /**
     * Called when pick date button is clicked. Display a date picker dialog
     * @param v
     */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new UpdateMaintenanceActivity.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_maintenance);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // retrieve book id from intent
        // get book id sent by BookListActivity, -1 if not found
        Intent intent = getIntent();
        int maintenance_id = intent.getIntExtra("maintenance_id", -1);

        // initialize createdAt to today's date
        createdAt = new Date();

        // get references to the form fields in layout
        txtType = findViewById(R.id.txtType);
        txtCost = findViewById(R.id.txtCost);
        tvMaintenanceDate = findViewById(R.id.tvMaintenanceDate);


        // retrieve book info from database using the book id
        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users users = spm.getUsers();

        // get book service instance
        MaintenanceService maintenanceService = ApiUtils.getMaintenanceService();

        // execute the API query. send the token and book id
        maintenanceService.getMaintenance(users.getUSER_TOKEN(), maintenance_id).enqueue(new Callback<Maintenance>() {

            @Override
            public void onResponse(Call<Maintenance> call, Response<Maintenance> response) {
                // for debug purpose
                Log.d("MyApp:", "Update Form Populate Response: " + response.raw().toString());

                if (response.code() == 200) {
                    // server return success
                    // get book object from response
                    maintenance = response.body();

                    // set values into forms
                    txtType.setText(maintenance.getType());

                    double cost = maintenance.getCost();
                    txtCost.setText(String.format("%.2f", cost));

                    tvMaintenanceDate.setText(maintenance.getMaintenanceDate());

                    // parse created_at date to date object
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        // parse created date string to date object
                        createdAt = sdf.parse(maintenance.getMaintenanceDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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

    /**
     * Update book info in database when the user click Update Book button
     * @param view
     */
    public void updateMaintenance(View view) {
        // get values in form
        String type = txtType.getText().toString();
        String costS = txtCost.getText().toString();
        double cost = Double.parseDouble(costS);

        // convert createdAt date to format required by Database - yyyy-MM-dd HH:mm:ss
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String m_Date = sdf.format(createdAt);

        // set updated_at to current date and time
        String updated_at = sdf.format(new Date());

        Log.d("MyApp:", "Old Maintenance info: " + maintenance.toString());

        // update the book object retrieved in when populating the form with the new data.
        // update all fields excluding the id
        maintenance.setType(type);
        maintenance.setCost(cost);
        maintenance.setMaintenanceDate(m_Date);
        maintenance.setUpdateMaintenanceDate(updated_at);


        Log.d("MyApp:", "New Maintenance info: " + maintenance.toString());

        // get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users users = spm.getUsers();

        // send request to update the book record to the REST API
        MaintenanceService maintenanceService= ApiUtils.getMaintenanceService();
        Call<Maintenance> call = maintenanceService.updateMaintenance(users.getUSER_TOKEN(), maintenance.getMaintenanceID(),
                maintenance.getType(), maintenance.getCost(), maintenance.getMaintenanceDate(),
                maintenance.getUpdateMaintenanceDate());

        // execute
        call.enqueue(new Callback<Maintenance>() {
            @Override
            public void onResponse(Call<Maintenance> call, Response<Maintenance> response) {

                // for debug purpose
                Log.d("MyApp:", "Update Request Response: " + response.raw().toString());

                if (response.code() == 200) {
                    // server return success code for update request
                    // get updated book object from response
                    updatedMaintenance = response.body();
                    // display message
                    showAlertDialog();
                   /* Toast.makeText(getApplicationContext(),
                            updatedBook.getName() + " updated successfully.",
                            Toast.LENGTH_LONG).show();*/

                    // end this activity and forward user to BookListActivity
                    /*Intent intent = new Intent(getApplicationContext(), BookListActivity.class);
                    startActivity(intent);
                    finish();*/
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
                displayAlert("Error [" + t.getMessage() + "]");
                // for debug purpose
                Log.d("MyApp:", "Error: " + t.getCause().getMessage());
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
     * Displaying an alert dialog with a single button
     //* @param message - message to be displayed
     */

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(updatedMaintenance.getType() + " update successfully");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //do things
                Intent intent = new Intent(getApplicationContext(), MaintenanceListActivity.class);
                startActivity(intent);
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
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
}