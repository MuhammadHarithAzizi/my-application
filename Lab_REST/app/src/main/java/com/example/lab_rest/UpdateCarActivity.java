package com.example.lab_rest;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.loader.content.CursorLoader;

import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.FileInfo;
import com.example.lab_rest.model.Maintenance;
import com.example.lab_rest.model.User;
import com.example.lab_rest.model.Users;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.CarService;
import com.example.lab_rest.remote.MaintenanceService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateCarActivity extends AppCompatActivity {

    private EditText txtModel;
    private EditText txtBrand;
    private EditText txtPlateNumber;
    private EditText txtAvailability;
    private static TextView tvCreated;
    private Spinner spCategory;

    private static Date createdAt;

    private Car car;

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            c.setTime(createdAt); // Use the current book created date as the default date in the picker
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
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            tvCreated.setText( sdf.format(createdAt) );
        }
    }

    /**
     * Called when pick date button is clicked. Display a date picker dialog
     * @param v
     */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new UpdateCarActivity.DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_car);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtModel = findViewById(R.id.txtModel);
        txtBrand = findViewById(R.id.txtBrand);
        txtPlateNumber = findViewById(R.id.txtPlateNumber);
        txtAvailability = findViewById(R.id.txtAStatus);
        tvCreated = findViewById(R.id.tvCreated);
        spCategory = findViewById(R.id.spCategory);

        Intent intent = getIntent();
        int id = intent.getIntExtra("car_id", -1);
        /*Log.d("UpdateCarActivity", "Car ID from intent: " + id);

        if (id == -1) {
            Toast.makeText(this, "Invalid Car ID", Toast.LENGTH_LONG).show();
            finish(); // close activity if Car ID is invalid
            return;
        }*/

        createdAt = new Date();
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users users = spm.getUsers();

        CarService carService = ApiUtils.getCarService();
        carService.getCar(users.getUSER_TOKEN(), id).enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                Log.d("UpdateCarActivity", "Update Form Populate Response: " + response.raw().toString());

                if (response.code() == 200) {
                    car = response.body();
                    txtModel.setText(car.getModel());
                    txtBrand.setText(car.getBrand());
                    txtPlateNumber.setText(car.getPlateNumber());
                    txtAvailability.setText(car.getAvailability());
                    tvCreated.setText(car.getCreatedAt());

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        createdAt = sdf.parse(car.getCreatedAt());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    fetchAllMainCategories();
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("UpdateCarActivity", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error connecting", Toast.LENGTH_LONG).show();
                Log.e("UpdateCarActivity", "Error: ", t);
            }
        });
    }


    public void clearSessionAndRedirect() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();
        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void updateCar(View view) {
        updateCarRecord();
    }

    public void updateCarRecord() {
        String model = txtModel.getText().toString();
        String brand = txtBrand.getText().toString();
        String plateNumber = txtPlateNumber.getText().toString();
        String availability = txtAvailability.getText().toString();
        Maintenance selectedCategory = (Maintenance) spCategory.getSelectedItem();
        int maintenance_ID = selectedCategory != null ? selectedCategory.getMaintenanceID() : -1;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String created_at = sdf.format(createdAt);

        car.setModel(model);
        car.setBrand(brand);
        car.setPlateNumber(plateNumber);
        car.setAvailability(availability);
        car.setCreatedAt(created_at);
        car.setMaintenance_id(maintenance_ID);

        Log.d("UpdateCarActivity", "Updated Car info: " + car.toString());

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users users = spm.getUsers();

        CarService carService = ApiUtils.getCarService();
        Call<Car> call = carService.updateCar(users.getUSER_TOKEN(), car.getCarID(), car.getMaintenance_id(), car.getModel(), car.getBrand(), car.getPlateNumber(), car.getAvailability(), car.getCreatedAt(), car.getImage());

        call.enqueue(new Callback<Car>() {
            @Override
            public void onResponse(Call<Car> call, Response<Car> response) {
                Log.d("UpdateCarActivity", "Update Request Response: " + response.raw().toString());

                if (response.code() == 200) {
                    Car updatedCar = response.body();
                    Toast.makeText(getApplicationContext(), updatedCar.getModel() + " updated successfully.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), CarListActivity.class);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("UpdateCarActivity", response.toString());
                }
            }

            @Override
            public void onFailure(Call<Car> call, Throwable t) {
                displayAlert("Error [" + (t != null ? t.getMessage() : "Unknown error") + "]");
                Log.d("UpdateCarActivity", "Error: " + (t != null ? t.getMessage() : "Unknown error"));
            }
        });
    }

    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void fetchAllMainCategories() {
        MaintenanceService maintenanceService = ApiUtils.getMaintenanceService();
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users users = spm.getUsers();
        Call<List<Maintenance>> call = maintenanceService.getAllMaintenances(users.getUSER_TOKEN());
        call.enqueue(new Callback<List<Maintenance>>() {
            @Override
            public void onResponse(Call<List<Maintenance>> call, Response<List<Maintenance>> response) {
                if (response.code() == 200) {
                    List<Maintenance> maintenance = response.body();
                    ArrayAdapter<Maintenance> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, maintenance);
                    spCategory.setAdapter(adapter);
                } else if (response.code() == 401) {
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("MyApp: ", response.toString());
                }
            }

            @Override
            public void onFailure(Call<List<Maintenance>> call, Throwable throwable) {
                Log.e("MyApp:", throwable.getMessage());
            }
        });
    }
}
