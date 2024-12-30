package com.example.lab_rest;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab_rest.model.Booking;
import com.example.lab_rest.model.User;
import com.example.lab_rest.model.Users;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.BookingService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateBookingActivity extends AppCompatActivity {

    private TextView tvCar;
    private TextView tvBookingDate;
    private TextView tvRemarks;
    private Spinner spinnerStatus;
    private TextView tvCreatedAt;
    private EditText txtAdminMessage;

    private Booking booking;
    private static Date createdAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_booking);

        Intent intent = getIntent();
        int id = intent.getIntExtra("BookingID", -1);
        Log.d("UpdateBookingActivity", "Booking ID from intent: " + id);

        createdAt = new Date();

        initializeViews();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.status_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(adapter);

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users users = spm.getUsers();

        fetchBookingData(users, id, adapter);
    }

    private void initializeViews() {
        tvCar = findViewById(R.id.tvCar);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvRemarks = findViewById(R.id.tvRemarks);
        spinnerStatus = findViewById(R.id.spinnerStatus);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        txtAdminMessage = findViewById(R.id.txtAdminMessage);
    }

    private void fetchBookingData(Users users, int id, ArrayAdapter<CharSequence> adapter) {
        BookingService bookingService = ApiUtils.getBookingService();
        bookingService.getBooking(users.getUSER_TOKEN(), id).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                Log.d("UpdateBookingActivity", "Update Form Populate Response: " + response.raw().toString());

                if (response.code() == 200) {
                    populateBookingData(response.body(), adapter);
                } else if (response.code() == 401) {
                    handleInvalidSession();
                } else {
                    showError(response.message());
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                showConnectionError(t);
            }
        });
    }

    private void populateBookingData(Booking bookingData, ArrayAdapter<CharSequence> adapter) {
        booking = bookingData;
        tvCar.setText(String.valueOf(booking.getCarId()));
        tvBookingDate.setText(booking.getBookingDate());
        tvRemarks.setText(booking.getRemarks());
        spinnerStatus.setSelection(adapter.getPosition(booking.getStatus()));
        tvCreatedAt.setText(booking.getCreatedAt());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        try {
            createdAt = sdf.parse(booking.getCreatedAt());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void handleInvalidSession() {
        Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
        clearSessionAndRedirect();
    }

    private void showError(String message) {
        Toast.makeText(getApplicationContext(), "Error: " + message, Toast.LENGTH_LONG).show();
        Log.e("UpdateBookingActivity", message);
    }

    private void showConnectionError(Throwable t) {
        Toast.makeText(getApplicationContext(), "Error connecting", Toast.LENGTH_LONG).show();
        Log.e("UpdateBookingActivity", "Error: ", t);
    }

    public void clearSessionAndRedirect() {
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();
        finishAffinity();
        startActivity(new Intent(UpdateBookingActivity.this, LoginActivity.class));
    }

    public void updateBooking(View view) {
        String status = spinnerStatus.getSelectedItem().toString();
        String adminMessage = txtAdminMessage.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        String formattedCreatedAt = sdf.format(createdAt);

        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users users = spm.getUsers();
        BookingService bookingService = ApiUtils.getBookingService();
        bookingService.updateBooking(
                users.getUSER_TOKEN(),
                booking.getBookingId(),
                booking.getUserId(),
                booking.getCarId(),
                booking.getBookingDate(),
                status,
                booking.getRemarks(),
                adminMessage,
                formattedCreatedAt
        ).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                Log.d("UpdateBookingActivity", "Update Request Response: " + response.raw());

                if (response.code() == 200) {
                    handleSuccessfulUpdate();
                } else if (response.code() == 401) {
                    handleInvalidSession();
                } else {
                    showDetailedError(response);
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                showFailureDialog(t);
            }
        });
    }

    private void handleSuccessfulUpdate() {
        Booking updatedBooking = booking;
        Toast.makeText(getApplicationContext(), "Booking updated successfully.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), BookingListActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDetailedError(Response<Booking> response) {
        try {
            String errorBody = response.errorBody().string();
            Toast.makeText(getApplicationContext(), "Error: " + errorBody, Toast.LENGTH_LONG).show();
            Log.e("UpdateBookingActivity", "Error body: " + errorBody);
        } catch (IOException e) {
            Log.e("UpdateBookingActivity", "Error reading error body", e);
        }
        Log.e("UpdateBookingActivity", response.toString());
    }

    private void showFailureDialog(Throwable t) {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateBookingActivity.this);
        builder.setTitle("Error")
                .setMessage("Cannot connect to server")
                .setPositiveButton(android.R.string.ok, (dialog, id) -> {
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        Log.e("UpdateBookingActivity", "Error: ", t);
    }
}
