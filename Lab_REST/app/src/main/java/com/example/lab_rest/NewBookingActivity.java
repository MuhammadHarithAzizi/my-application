package com.example.lab_rest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.lab_rest.model.Booking;
import com.example.lab_rest.model.Car;
import com.example.lab_rest.model.Users;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.BookingService;
import com.example.lab_rest.remote.CarService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewBookingActivity extends AppCompatActivity {

    private Spinner spCar;
    private TextView tvBookingDate;
    private EditText txtRemarks;
    private EditText txtStatus;
    private TextView tvCreatedAt;
    private Button btnAddBooking;

    private static Date createdAt;
    private Users users;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_booking);

        spCar = findViewById(R.id.spCar);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        txtRemarks = findViewById(R.id.txtRemarks);
        txtStatus = findViewById(R.id.txtStatus);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        btnAddBooking = findViewById(R.id.btnAddBooking);

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        users = spm.getUsers(); // Retrieve user instance from shared preferences
        if (users != null) {
            userId = users.getUSER_ID(); // Ensure this method returns the correct user ID
            Log.d("NewBookingActivity", "User retrieved: " + users + ", UserId: " + userId);
        } else {
            Log.e("NewBookingActivity", "User not found in SharedPrefManager");
        }

        createdAt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
        tvCreatedAt.setText(sdf.format(createdAt));

        fetchAllCarModel();

        tvBookingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tvBookingDate);
            }
        });

        tvCreatedAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(tvCreatedAt);
            }
        });

        /*btnAddBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewBooking(v);
            }
        });*/
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment(tvBookingDate);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addNewBooking(View view) {
        int CarId = ((Car) spCar.getSelectedItem()).getCarID();
        String Remarks = txtRemarks.getText().toString();
        String Status = txtStatus.getText().toString();

        Date bookingDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String BookingDate = sdf.format(bookingDate);
        String CreatedAt = sdf.format(createdAt);

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users users = spm.getUsers();

        BookingService bookingService = ApiUtils.getBookingService();
        Call<Booking> call = bookingService.addBooking(users.getUSER_TOKEN(), users.getUSER_ID(), CarId, BookingDate, Status, Remarks, CreatedAt);

        call.enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                Log.d("MyApp:", "Response: " + response.raw().toString());
                if (response.isSuccessful() && response.body() != null) {
                    Booking addedBooking = response.body();
                    Toast.makeText(getApplicationContext(), "Booking added successfully.", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), BookingListActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("MyApp: ", "Response code: " + response.code() + ", message: " + response.message() + ", body: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error [" + t.getMessage() + "]", Toast.LENGTH_LONG).show();
                Log.d("MyApp:", "Error: " + t.getCause());
            }
        });
    }

    private void fetchAllCarModel() {
        CarService carService = ApiUtils.getCarService();
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users user = spm.getUsers();
        Call<List<Car>> call = carService.getAllCars(users.getUSER_TOKEN());
        call.enqueue(new Callback<List<Car>>() {
            @Override
            public void onResponse(Call<List<Car>> call, Response<List<Car>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Car> carList = response.body();

                    // Filter out cars that have maintenance ID
                    List<Car> availableCars = new ArrayList<>();
                    for (Car car : carList) {
                        if (!car.hasMaintenance()) {
                            availableCars.add(car);
                        }
                    }

                    ArrayAdapter<Car> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, availableCars);
                    spCar.setAdapter(adapter);
                } else {
                    Toast.makeText(NewBookingActivity.this, "Error fetching car models: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("NewBookingActivity", "Error fetching car models: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Car>> call, Throwable t) {
                Toast.makeText(NewBookingActivity.this, "Error fetching car models: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("NewBookingActivity", "Error: " + t.getMessage());
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

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        private TextView dateTextView;

        public DatePickerFragment(TextView dateTextView) {
            this.dateTextView = dateTextView;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            Calendar calendar = new GregorianCalendar(year, month, day);
            createdAt = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            dateTextView.setText(sdf.format(createdAt));
        }
    }
}
