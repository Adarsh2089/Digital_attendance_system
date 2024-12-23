package com.adarsh.digitalattendancesystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Class1 extends AppCompatActivity {

    Button takeattendece1;
    Button takeattendece2;
    Button takeattendece3;
    Button takeattendece4;
    Button takeattendece5;
    Button takeattendece6;
    private TextView Date_Time;
    private TextView liveTimeTextView;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class1);

      takeattendece1 = findViewById(R.id.take1);
      takeattendece2 = findViewById(R.id.take2);
      takeattendece3 = findViewById(R.id.take3);
      takeattendece4 = findViewById(R.id.take4);
      takeattendece5 = findViewById(R.id.take5);
      takeattendece6 = findViewById(R.id.take6);

      Date_Time=findViewById(R.id.date);
        // Get the current date and time
        String currentDateTime = getCurrentDateTime();

        // Set the current date and time to the TextView
        Date_Time.setText(currentDateTime);


        // Find the TextView by its ID
        liveTimeTextView = findViewById(R.id.Systime);
        // Initialize the Handler
        handler = new Handler();

        // Define the Runnable to update the time
        runnable = new Runnable() {
            @Override
            public void run() {
                // Get the current time
                String currentTime = getCurrentTime();
                // Update the TextView
                liveTimeTextView.setText(currentTime);

                // Re-run the Runnable after 1000ms (1 second)
                handler.postDelayed(this, 1000);
            }
        };

        // Start the Runnable
        handler.post(runnable);

        //Buttons Starts from here
        takeattendece1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Class1.this, Teacher1.class);
                startActivity(intent);

            }
        });
        takeattendece2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Class1.this,Teacher2.class);
                startActivity(intent);

            }
        });
        takeattendece3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Class1.this, Class3.class);
                startActivity(intent);

            }
        });
        takeattendece4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Class1.this, Class3.class);
                startActivity(intent);


            }
        });
        takeattendece5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Class1.this, Class3.class);
                startActivity(intent);

            }
        });
        takeattendece6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Class1.this, Class3.class);
                startActivity(intent);

            }
        });

    }

    private String getCurrentTime() {
        // Define the time format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        // Get the current time
        return sdf.format(new Date());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the Runnable when the Activity is destroyed
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
    private String getCurrentDateTime() {
        // Define the date and time format
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        // Get the current date and time
        return sdf.format(new Date());
    }
}