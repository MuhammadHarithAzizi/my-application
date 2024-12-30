package com.example.lab_rest;

        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Handler;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;

        import androidx.activity.EdgeToEdge;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.core.graphics.Insets;
        import androidx.core.view.ViewCompat;
        import androidx.core.view.WindowInsetsCompat;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 3000; // Duration of splash screen (3 seconds)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Apply fade-in animation to the logo
        ImageView splashLogo = findViewById(R.id.carsplash);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        splashLogo.startAnimation(fadeIn);
       // Apply fade-in animation to the text
        TextView splashText = findViewById(R.id.splash_text);
        splashText.startAnimation(fadeIn);

        // Apply fade-in animation to the progress bar
        ProgressBar splashProgress = findViewById(R.id.splash_progress);
        splashProgress.startAnimation(fadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start main activity
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                // Close splash activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}