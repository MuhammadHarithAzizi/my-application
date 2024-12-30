package com.example.lab_rest;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab_rest.adapter.BookingAdapter;
import com.example.lab_rest.model.Booking;
import com.example.lab_rest.model.DeleteResponse;
import com.example.lab_rest.model.User;
import com.example.lab_rest.model.Users;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.BookingService;
import com.example.lab_rest.sharedpref.SharedPrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingListActivity extends AppCompatActivity {

    private BookingService bookingService;
    private RecyclerView rvBookingList;
    private BookingAdapter adapter;
    private String userRole; // Add a variable to store the user role
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_list);

        fab = findViewById(R.id.fab);

        // Retrieve the user role from the Intent extras
        String userRole = getIntent().getStringExtra("userRole");

        // Check if the user is not an admin
        if (!"admin".equals(userRole)) {
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }


        // Initialize RecyclerView
        rvBookingList = findViewById(R.id.rvBookingList);
        registerForContextMenu(rvBookingList);

        // Fetch and update booking list
        updateBookingList();
    }

    private void updateBookingList() {
        // Get user info from SharedPreferences to retrieve token value
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users users = spm.getUsers();
        String token = users.getUSER_TOKEN();
        userRole = users.getUSER_ROLE(); // Store the user role in the variable
        int userId = users.getUSER_ID(); // Get user ID as int

        // Get booking service instance
        bookingService = ApiUtils.getBookingService();

        Call<List<Booking>> call;
        if ("admin".equalsIgnoreCase(userRole)) {
            // Admin can see all bookings
            call = bookingService.getAllBookings(token);
        } else {
            // Regular user can see only their bookings
            call = bookingService.getAllBookings(token);
        }

        // Execute API call to fetch bookings
        // Execute API call to fetch bookings
        call.enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if (response.isSuccessful()) {
                    List<Booking> bookings = response.body();

                    if (!"admin".equalsIgnoreCase(userRole)) {
                        // Filter bookings to only include the user's bookings
                        List<Booking> userBookings = new ArrayList<>();
                        for (Booking booking : bookings) {
                            if (booking.getUserId() == userId) {
                                userBookings.add(booking);
                            }
                        }
                        bookings = userBookings;
                    }

                    // Initialize adapter with retrieved bookings
                    adapter = new BookingAdapter(getApplicationContext(), bookings);

                    // Set adapter to RecyclerView
                    rvBookingList.setAdapter(adapter);

                    // Set layout manager to RecyclerView
                    rvBookingList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    // Add divider between items in the list
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvBookingList.getContext(),
                            DividerItemDecoration.VERTICAL);
                    rvBookingList.addItemDecoration(dividerItemDecoration);
                } else {
                    handleErrorResponse(response);
                }
            }


            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                showErrorToast("Error connecting to the server");
                Log.e("BookingListActivity", "Error: " + t.getMessage(), t);
            }
        });
    }

    private void handleErrorResponse(Response<List<Booking>> response) {
        if (response.code() == 401) {
            showErrorToast("Invalid session. Please login again");
            clearSessionAndRedirect();
        } else {
            showErrorToast("Error: " + response.message());
            Log.e("BookingListActivity", "Error: " + response.message());
        }
    }

    private void clearSessionAndRedirect() {
        // Clear SharedPreferences
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        spm.logout();

        // Finish current activity
        finish();

        // Redirect to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.booking_context_menu, menu);
        // Check user role and set visibility of the delete and update menu items
        MenuItem deleteMenuItem = menu.findItem(R.id.menuDelete);
        MenuItem updateMenuItem = menu.findItem(R.id.menuUpdate);

        if ("admin".equalsIgnoreCase(userRole)) {
            deleteMenuItem.setVisible(false);
        } else {
            updateMenuItem.setVisible(false);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Booking selectedBooking = adapter.getSelectedItem();
        Log.d("BookingListActivity", "Selected booking: " + selectedBooking.toString());

        if (item.getItemId() == R.id.menuDetails) {    // user clicked details contextual menu
            doViewDetails(selectedBooking);
            return true;
        } else if (item.getItemId() == R.id.menuDelete) {
            // user clicked the delete contextual menu
            if ("new".equalsIgnoreCase(selectedBooking.getStatus())) {
                doDeleteBooking(selectedBooking);
            } else {
                showErrorToast("You don't have permission to delete this booking");
            }
            return true;
        } else if (item.getItemId() == R.id.menuUpdate) {
            doUpdateBooking(selectedBooking);
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void doViewDetails(Booking selectedBooking) {
        Log.d("MyApp:", "viewing details: " + selectedBooking.toString());
        // Forward user to BookingDetailsActivity, passing the selected booking id
        Intent intent = new Intent(this, BookingDetailsActivity.class);
        intent.putExtra("BookingId", selectedBooking.getBookingId());
        startActivity(intent);
    }

    private void doUpdateBooking(Booking selectedBooking) {
        Log.d("BookingListActivity", "Updating booking: " + selectedBooking.toString());
        // Forward user to UpdateBookingActivity, passing the selected booking id
        Intent intent = new Intent(getApplicationContext(), UpdateBookingActivity.class);
        intent.putExtra("BookingID", selectedBooking.getBookingId());
        startActivity(intent);
    }

    private void doDeleteBooking(Booking selectedBooking) {
        // Get user info from SharedPrefManager
        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
        Users user = spm.getUsers();

        // Prepare REST API call
        BookingService bookingService = ApiUtils.getBookingService();
        Call<DeleteResponse> call = bookingService.deleteBooking(user.getUSER_TOKEN(), selectedBooking.getBookingId());

        // Execute the call
        call.enqueue(new Callback<DeleteResponse>() {
            @Override
            public void onResponse(Call<DeleteResponse> call, Response<DeleteResponse> response) {
                if (response.code() == 200) {
                    // 200 means OK
                    displayAlert("Booking successfully deleted");
                    // Update data in list view
                    updateBookingList();
                } else if (response.code() == 401) {
                    // Invalid token, ask user to re-login
                    Toast.makeText(getApplicationContext(), "Invalid session. Please login again", Toast.LENGTH_LONG).show();
                    clearSessionAndRedirect();
                } else {
                    Toast.makeText(getApplicationContext(), "Error: " + response.message(), Toast.LENGTH_LONG).show();
                    // Server returned other error
                    Log.e("BookingListActivity", "Error: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<DeleteResponse> call, Throwable t) {
                displayAlert("Error: " + t.getMessage());
                Log.e("BookingListActivity", "Error: " + t.getMessage());
            }
        });
    }

    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, id) -> dialog.cancel());
        AlertDialog alert = builder.create();
        alert.show();
    }


    public void floatingAddBookingClicked(View view) {
        // forward user to NewBookActivity
        Intent intent = new Intent(this, NewBookingActivity.class);
        startActivity(intent);
    }


    private void showErrorToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}