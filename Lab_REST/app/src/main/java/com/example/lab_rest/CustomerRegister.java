package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab_rest.model.Customer;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.CustomerService;
import com.example.lab_rest.sharedpref.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerRegister extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtContact;
    private EditText edtEmail;
    private EditText edtAddress;
    private TextView textViewLogin;
    private Button submitSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_register);

        // Apply window insets for Edge-to-Edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize form elements
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtContact = findViewById(R.id.edtContact);
        edtAddress = findViewById(R.id.edtAddress);
        submitSave = findViewById(R.id.submitSave);
        textViewLogin = findViewById(R.id.textViewLogin);

        // Set click listener for the login text view
        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerRegister.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Set click listener for the submit button
        submitSave.setOnClickListener(this::registerCustomer);
    }

    private void registerCustomer(View view) {
        // Get user input
        String custName = edtUsername.getText().toString().trim();
        String custPassword = edtPassword.getText().toString().trim();
        String custEmail = edtEmail.getText().toString().trim();
        String custContact = edtContact.getText().toString().trim();
        String custAddress = edtAddress.getText().toString().trim();

        // Validate user input
        if (validateRegistrationForm(custName, custPassword, custEmail, custContact, custAddress)) {
            // Register customer using REST API
            doRegister(custName, custPassword, custEmail, custContact, custAddress);
        }
    }

    private boolean validateRegistrationForm(String custName, String custPassword, String custEmail, String custContact, String custAddress) {
        if (custName.isEmpty()) {
            displayToast("Customer Name is required");
            return false;
        }
        if (custPassword.isEmpty()) {
            displayToast("Customer Password is required");
            return false;
        }
        if (custEmail.isEmpty()) {
            displayToast("Customer Email is required");
            return false;
        }
        if (custContact.isEmpty()) {
            displayToast("Customer Contact is required");
            return false;
        }
        if (custAddress.isEmpty()) {
            displayToast("Customer Address is required");
            return false;
        }
        return true;
    }

    private void doRegister(String custName, String custPassword, String custEmail, String custContact, String custAddress) {
        CustomerService customerService = ApiUtils.getCustomerService();
        Call<Customer> call = customerService.register(custName, custPassword, custEmail, custContact, custAddress);

        call.enqueue(new Callback<Customer>() {
            @Override
            public void onResponse(Call<Customer> call, Response<Customer> response) {
                // for debug purpose
                Log.d("MyApp:", "Response: " + response.raw().toString());
            }

            @Override
            public void onFailure(Call<Customer> call, Throwable t) {
                displayToast("Error connecting to server");
                Log.e("CustomerRegister", t.toString());
            }
        });
    }

    public void addCustomer(View view) {
        // forward user to BookListActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    private void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
