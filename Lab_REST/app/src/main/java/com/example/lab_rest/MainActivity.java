package com.example.lab_rest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.lab_rest.model.User;
import com.example.lab_rest.sharedpref.SharedPrefManager;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    //    private TextView textView3;
    private GoogleMap gMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);
        // get references
//        textView3 = findViewById(R.id.textView3);
//
//        // greet the user
//        // if the user is not logged in we will directly them to LoginActivity
//        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
//        if (!spm.isLoggedIn()) { // no session record
//
//            // stop this MainActivity
//            finish();
//
//            // forward to Login Page
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }
//        else {
//
//            // Greet user
//            User user = spm.getUser();
//            textView3.setText("welcome " + user.getUsername() +" to");
//        }
//
    }
//
//    public void logoutClicked(View view) {
//
//        // clear the shared preferences
//        SharedPrefManager spm = new SharedPrefManager(getApplicationContext());
//        spm.logout();
//
//        // display message
//        Toast.makeText(getApplicationContext(),
//                "You have successfully logged out.",
//                Toast.LENGTH_LONG).show();
//
//        // terminate this MainActivity
//        finish();
//
//        // forward to Login Page
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//
//    }
//    public void bookingListClicked(View view) {
//        // forward user to BookListActivity
//        Intent intent = new Intent(getApplicationContext(), BookingListActivity.class);
//        startActivity(intent);
//    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng location = new LatLng(55.6761, 12.5683);
        googleMap.addMarker(new MarkerOptions().position(location).title("Copenhagen"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12));

    }
}