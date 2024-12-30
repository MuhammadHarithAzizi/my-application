package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminRegister extends AppCompatActivity {

    private EditText edtUsername;
    private EditText edtPassword;
    private EditText edtContact;
    private EditText edtEmail;
    private EditText edtAddress;
    private TextView textViewLogin;


    String Colector = "";
    Button SubmitSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            return insets;
        });

        // get references to form elements
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        edtEmail = findViewById(R.id.edtEmail);
        edtContact = findViewById(R.id.edtContact);
        edtAddress = findViewById(R.id.edtAddress);
        SubmitSave = findViewById(R.id.btnSubmit);
        //textViewLogin = findViewById(R.id.textViewLogin);

        textViewLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View view){
                Intent intent = new Intent(AdminRegister.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }




    /**
     * Login button action handler
     */
    public void loginClicked(View view) {

        // get username and password entered by user
        String CustName = edtUsername.getText().toString();
        String CustPassword = edtPassword.getText().toString();
        String CustEmail = edtEmail.getText().toString();
        String CustContact = edtContact.getText().toString();
        String CustAddress = edtAddress.getText().toString();

        if (CustName.isEmpty()) {
            Toast.makeText(AdminRegister.this, "Pleas fill the name field", Toast.LENGTH_SHORT).show();
        }  else if (CustPassword.isEmpty()) {
            Toast.makeText(AdminRegister.this, "Pleas fill the password field", Toast.LENGTH_SHORT).show();
        } else if (CustEmail.isEmpty()) {
            Toast.makeText(AdminRegister.this, "Pleas fill the email field", Toast.LENGTH_SHORT).show();
        } else if (CustContact.isEmpty()) {
            Toast.makeText(AdminRegister.this, "Pleas fill the Contact field", Toast.LENGTH_SHORT).show();
        } else if (CustAddress.isEmpty()) {
            Toast.makeText(AdminRegister.this, "Pleas fill the Address field", Toast.LENGTH_SHORT).show();
        } else {

            Colector += edtUsername + "\n";
            Colector += edtPassword + "\n";
            Colector += edtEmail + "\n";
            Colector += edtContact + "\n";
            Colector += edtAddress + "\n";
            Toast.makeText(AdminRegister.this, "User Register Info \n:" + Colector, Toast.LENGTH_SHORT).show();
        }

    }

    /*private void doRegister(String CustEmail, String CustName, String CustContact, String CustAddress) {
        RegisterService registerService = ApiUtils.getRegisterService();
        Call<User> call = registerService.register(CustEmail, CustName, CustContact, CustAddress);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null && user.getToken() != null) {
                        displayToast("Register successful");
                        displayToast("Token: " + user.getToken());

                        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
                        spm.storeUser(user);

                        Intent intent;
                        if ("admin".equalsIgnoreCase(user.getRole())) {
                            intent = new Intent(getApplicationContext(), AdminActivity.class);
                        } else {
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                        }

                        finish();
                        startActivity(intent);
                    } else {
                        displayToast("Register error");
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
   /* private boolean validateRegister(String username, String password, String contact, String address) {
        if (username == null || username.trim().isEmpty()) {
            displayToast("Username is required");
            return false;
        }
        if (password == null || password.trim().isEmpty()) {
            displayToast("Password is required");
            return false;
        }
        if (contact == null || contact.trim().isEmpty()) {
            displayToast("Contact is required");
            return false;
        }
        if (address == null || address.trim().isEmpty()) {
            displayToast("Address is required");
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
}

