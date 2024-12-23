package com.adarsh.digitalattendancesystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Teacher1 extends AppCompatActivity {

    private DatabaseReference databaseReference;
    TextView TodayDate;
    TextView TodayTime;
    private Handler handler;
    private Runnable runnable;
    Button SaveAttendance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher1);

        getWindow().setStatusBarColor(ContextCompat.getColor(Teacher1.this,R.color.green));

        //Taking Today's Date
        String currentDateTime = getCurrentDateTime();

        TodayDate=findViewById(R.id.TodayDate);
        TodayTime=findViewById(R.id.TodayTime);
        SaveAttendance=findViewById(R.id.SaveAttendance);

        // After saving attendence it will logout
        SaveAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "saved Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Teacher1.this,login.class);
                startActivity(intent);
            }
        });

        // Set the current date and time to the TextView
        TodayDate.setText(currentDateTime);
      //  To_Date.setText(currentDateTime);

        //getting system time
        // Initialize the Handler
        handler = new Handler();

        // Define the Runnable to update the time
        runnable = new Runnable() {
            @Override
            public void run() {
                // Get the current time
                String currentTime = getCurrentTime();
                // Update the TextView
                TodayTime.setText(currentTime);

                // Re-run the Runnable after 1000ms (1 second)
                handler.postDelayed(this, 1000);
            }
        };

        // Start the Runnable
        handler.post(runnable);



        // Initialize Firebase Database
        //    FirebaseDatabase database = FirebaseDatabase.getInstance("https://digital-attendance-syste-c9e49-default-rtdb.asia-southeast1.firebasedatabase.app/");
        //  databaseReference = database.getReference("Class1/Attendance");
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://digital-attendance-syste-c9e49-default-rtdb.asia-southeast1.firebasedatabase.app");
        DatabaseReference databaseReference = database.getReference("DataStructure").child(currentDateTime);


        // RecyclerView setup
        RecyclerView recyclerView = findViewById(R.id.studentRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create sample data
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student("Adarsh Kumar", "1", ""));
        studentList.add(new Student("Avnish Satyam", "2", ""));
        studentList.add(new Student("Nishant Kumar", "3", ""));
        studentList.add(new Student("Sushant Kumar", "4", ""));
        studentList.add(new Student("Atul Singh", "5", ""));
        studentList.add(new Student("Rishav Anand", "6", ""));
        studentList.add(new Student("Abhishek Yadav", "7", ""));
        studentList.add(new Student("Vinit Dubey", "8", ""));
        studentList.add(new Student("Yash Raj", "9", ""));
        studentList.add(new Student("Ravi ", "10", ""));


        // Set adapter
        StudentAdapter adapter = new StudentAdapter(studentList, databaseReference);

        recyclerView.setAdapter(adapter);
    }
    private String getCurrentDateTime() {
        // Define the date and time format
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        // Get the current date and time
        return sdf.format(new Date());
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

}