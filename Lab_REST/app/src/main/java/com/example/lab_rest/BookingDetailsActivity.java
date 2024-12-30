package com.example.lab_rest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lab_rest.model.Booking;
import com.example.lab_rest.model.User;
import com.example.lab_rest.model.Users;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.BookingService;
import com.example.lab_rest.sharedpref.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingDetailsActivity extends AppCompatActivity {

    private BookingService bookingService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);



        // Retrieve booking details based on selected id
        Intent intent = getIntent();
        int bookingId = intent.getIntExtra("BookingId", -1);

        // Get user info from SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users users = spm.getUsers();
        String token = users.getUSER_TOKEN();

        // Initialize BookingService
        bookingService = ApiUtils.getBookingService();

        // Fetch booking details
        fetchBookingDetails(token, bookingId);
    }

    private void fetchBookingDetails(String token, int bookingId) {
        // Execute API query to get booking details
        bookingService.getBooking(token, bookingId).enqueue(new Callback<Booking>() {
            @Override
            public void onResponse(Call<Booking> call, Response<Booking> response) {
                if (response.isSuccessful()) {
                    // Booking details retrieved successfully
                    Booking booking = response.body();
                    if (booking != null) {
                        // Update UI with booking details
                        updateUI(booking);
                    } else {
                        Toast.makeText(BookingDetailsActivity.this, "Booking details not found", Toast.LENGTH_SHORT).show();
                    }
                } else if (response.code() == 401) {
                    // Unauthorized error. Invalid token, ask user to relogin
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                } else {
                    // Server returned other error
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    Log.e("BookingDetailsActivity", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Booking> call, Throwable t) {
                // Error fetching data
                Toast.makeText(getApplicationContext(), "Error fetching booking details", Toast.LENGTH_LONG).show();
                Log.e("BookingDetailsActivity", "Error fetching booking details: " + t.getMessage(), t);
            }
        });
    }

    private void updateUI(Booking booking) {
        // Update UI elements with booking details tvBookingId
        TextView tvBookingId = findViewById(R.id.tvBookingId);
        TextView tvUserId = findViewById(R.id.tvUserId);
        TextView tvCarId = findViewById(R.id.tvCarId);
        TextView tvBookingDate = findViewById(R.id.tvBookingDate);
        TextView tvStatus = findViewById(R.id.tvStatus);
        TextView tvRemarks = findViewById(R.id.tvRemarks);
        TextView tvAdminMsg = findViewById(R.id.tvAdminMsg);
        TextView tvCreatedAt = findViewById(R.id.tvCreatedAt);

        // Set values
        tvBookingId.setText("Booking ID: " + booking.getBookingId());
        tvUserId.setText("User ID: " + booking.getUserId());
        tvCarId.setText("Car ID: " + booking.getCarId());
        tvBookingDate.setText("Booking Date: " + booking.getBookingDate());
        tvStatus.setText("Status: " + booking.getStatus());
        tvRemarks.setText("Remarks: " + booking.getRemarks());
        tvAdminMsg.setText("Admin Message: " + booking.getAdminMsg());
        tvCreatedAt.setText("Created At: " + booking.getCreatedAt());

    }

    private void clearSessionAndRedirect() {
        // Clear the shared preferences and redirect to login
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}