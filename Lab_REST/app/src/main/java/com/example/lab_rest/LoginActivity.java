package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab_rest.model.FailLogin;
import com.example.lab_rest.model.User;
import com.example.lab_rest.model.Users;
import com.example.lab_rest.remote.ApiUtils;
import com.example.lab_rest.remote.UserService;
import com.example.lab_rest.sharedpref.SharedPrefManager;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // get references to form elements
        edtUsername = findViewById(R.id.etUsername);
        edtPassword = findViewById(R.id.etPassword);

        // Check if the user is already logged in
//        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
//            // Redirect to the appropriate activity
//            Intent intent;
//            String userRole = SharedPrefManager.getInstance(this).getUser().getRole();
//            if ("admin".equalsIgnoreCase(userRole)) {
//                intent = new Intent(getApplicationContext(), AdminActivity.class);
//            } else {
//                intent = new Intent(getApplicationContext(), MainActivity.class);
//            }
//            startActivity(intent);
//            finish();
//        }
    }

    /**
     * Login button action handler
     */
    public void loginClicked(View view) {

        // get username and password entered by user
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        // validate form, make sure it is not empty
        if (validateLogin(username, password)) {
            // if not empty, login using REST API
            doLogin(username, password);
        }

    }

    /**
     * Call REST API to login
     *
     * @param username username
     * @param password password
     */
    private void doLogin(String username, String password) {
        UserService userService = ApiUtils.getUserService();
        Call<Users> call = userService.login(username, password);

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (response.isSuccessful()) {
                    Users users = response.body();
                    if (users != null && users.getUSER_TOKEN() != null) {
                        displayToast("Login successful");
                        displayToast("Token: " + users.getUSER_TOKEN());

                        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
                        spm.storeUser(users);

                        Intent intent;
                        if ("admin".equalsIgnoreCase(users.getUSER_ROLE())) {
                            intent = new Intent(getApplicationContext(), AdminActivity.class);
                        } else {
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                        }

                        finish();
                        startActivity(intent);
                    } else {
                        displayToast("Login error");
                    }
                } else {
                    try {
                        String errorResp = response.errorBody().string();
                        FailLogin e = new Gson().fromJson(errorResp, FailLogin.class);
                        displayToast(e.getError().getMessage());
                    } catch (Exception e) {
                        Log.e("MyApp:", e.toString());
                        displayToast("Error");
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                displayToast("Error connecting to server.");
                displayToast(t.getMessage());
                Log.e("MyApp:", t.toString()); // print error details to error log
            }
        });
    }

    /**
     * Validate value of username and password entered. Client side validation.
     * @param username
     * @param password
     * @return
     */
    private boolean validateLogin(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            displayToast("Username is required");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            displayToast("Password is required");
            return false;
        }
        return true;
    }

    /**
     * Display a Toast message
     * @param message message to be displayed inside toast
     */
    public void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void onSignupClicked(View view) {
        Intent intent = new Intent(getApplicationContext(), UserRegistrationActivity.class);
        startActivity(intent);
    }
}