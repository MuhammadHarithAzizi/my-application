package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UserRegistrationActivity extends AppCompatActivity {

    private EditText emailField, fullNameField, usernameField, passwordField;
    private Button signUpButton;
    private TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        // Initialize UI components
        emailField = findViewById(R.id.emailField);
        fullNameField = findViewById(R.id.fullNameField);
        usernameField = findViewById(R.id.usernameField);
        passwordField = findViewById(R.id.passwordField);
        signUpButton = findViewById(R.id.signUpButton);
        loginText = findViewById(R.id.loginText);

        // Handle sign-up button click
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String email = emailField.getText().toString().trim();
                String fullName = fullNameField.getText().toString().trim();
                String username = usernameField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();

                // Basic Input Validation
                if (TextUtils.isEmpty(email)) {
                    emailField.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(fullName)) {
                    fullNameField.setError("Full Name is required");
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    usernameField.setError("Username is required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    passwordField.setError("Password is required");
                    return;
                }

                // **(Replace with your actual registration logic)**
                // 1. Network request to your server to register the user
                // 2. Handle server response (success, errors, etc.)

                // Example: Simulate successful registration (replace with actual logic)
                Toast.makeText(UserRegistrationActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();

                // Navigate to the next activity (e.g., HomeActivity)
//                Intent intent = new Intent(UserRegistrationActivity.this, HomeActivity.class); // Replace with your actual HomeActivity class
//                startActivity(intent);
            }
        });

        // Handle the Log In link click
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Login Activity
                Intent intent = new Intent(UserRegistrationActivity.this, LoginActivity.class); // Replace with your actual LoginActivity class
                startActivity(intent);
            }
        });
    }
}