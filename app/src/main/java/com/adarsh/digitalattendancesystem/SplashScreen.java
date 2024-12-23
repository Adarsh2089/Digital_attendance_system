package com.adarsh.digitalattendancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(() -> {
            // Start the main activity
            Intent mainIntent = new Intent(SplashScreen.this,login.class);
            startActivity(mainIntent);
            finish(); // Finish splash activity so user can't go back to it
        },SPLASH_DISPLAY_LENGTH);

    }
}