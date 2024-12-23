package com.adarsh.digitalattendancesystem;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Analytics extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private EditText startDateInput, endDateInput;
    private String startDate="invalid";
    private String  endDate="invalid";
    private static final String DATE_FORMAT = "dd-MM-yyyy"; // Date format



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_analytics);

        EditText rollNumberInput = findViewById(R.id.rollNumberInput);
        Button fetchButton = findViewById(R.id.fetchButton);
        PieChart pieChartDataStructure = findViewById(R.id.pieChartDataStructure);
        PieChart pieChartCProgramming = findViewById(R.id.pieChartCProgramming);
        TextView summaryTextDataStructure = findViewById(R.id.summaryTextDataStructure);
        TextView summaryTextCProgramming = findViewById(R.id.summaryTextCProgramming);
        startDateInput = findViewById(R.id.startDateInput);
        endDateInput = findViewById(R.id.endDateInput);


        // Set DatePicker for Start Date
        startDateInput.setOnClickListener(v -> showDatePickerDialog(startDateInput));

        // Set DatePicker for End Date
        endDateInput.setOnClickListener(v -> showDatePickerDialog(endDateInput));





        fetchButton.setOnClickListener(v -> {
            String rollNumber = rollNumberInput.getText().toString().trim();
           if (startDate.equals("invalid") || endDate.equals("invalid")) { // checking user is selected date or not
               Toast.makeText(this, "Please select date first", Toast.LENGTH_SHORT).show();
               startDateInput.setError("Please select date first");
               endDateInput.setError("Please select date first");
           }else if (!rollNumber.isEmpty()) {
                String[] subjects = {"DataStructure", "C Programming"};  // Define the subjects
                fetchAttendanceForRollNumber(rollNumber, startDate, endDate, subjects, pieChartDataStructure, pieChartCProgramming, summaryTextDataStructure, summaryTextCProgramming);
            } else {
                rollNumberInput.setError("Please enter a valid roll number");
            }
        });
    }

    private void fetchAttendanceForRollNumber(String rollNumber, String startDate, String endDate, String[] subjects, PieChart pieChartDataStructure, PieChart pieChartCProgramming, TextView summaryTextDataStructure, TextView summaryTextCProgramming) {
        ArrayList<String> dateRange = getDateRange(startDate, endDate);

        // Loop over subjects
        for (String subject : subjects) {
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://digital-attendance-syste-c9e49-default-rtdb.asia-southeast1.firebasedatabase.app");
            databaseReference = database.getReference(subject); // Correct reference
           // DatabaseReference subjectRef = databaseReference.child(subject);

            // Using an array to hold totalClasses and presentCount (mutable reference)
            final int[] totalClasses = {0}; // Using array to make the value mutable
            final int[] presentCount = {0}; // Using array to make the value mutable

            for (String date : dateRange) {
                DatabaseReference dateRef = databaseReference.child(date);

                dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                                String rollNo = studentSnapshot.child("rollNo").getValue(String.class);
                                String status = studentSnapshot.child("status").getValue(String.class);

                                if (rollNo != null && rollNo.equals(rollNumber)) {
                                    totalClasses[0]++; // Increment totalClasses using the array
                                    if (status != null && status.equals("Present")) {
                                        presentCount[0]++; // Increment presentCount using the array
                                    }
                                }
                            }
                        }
                        updateSummaryAndChart(subject,totalClasses[0], presentCount[0], pieChartDataStructure, pieChartCProgramming, summaryTextDataStructure, summaryTextCProgramming,rollNumber);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Firebase", "Error: " + databaseError.getMessage());
                    }
                });
            }
        }
    }

    private void updateSummaryAndChart(String subject, int totalClasses, int presentCount, PieChart pieChartDataStructure, PieChart pieChartCProgramming, TextView summaryTextDataStructure, TextView summaryTextCProgramming ,String rollNumber) {
        int absentCount = totalClasses - presentCount;

        // Calculate attendance percentage
        float overallAttendancePercentage = 0;
        if (totalClasses > 0) {
            overallAttendancePercentage = ((float) presentCount / totalClasses) * 100;
        }

        // Update PieChart for the subject
        if (subject.equals("DataStructure")) {
            setupPieChart(pieChartDataStructure, overallAttendancePercentage);
            String summary = "Subject: "+ subject + "\n"+
                    "Roll Number: " + rollNumber + "\n"+
                    "Total Classes: " + totalClasses + "\n" +
                    "Present: " + presentCount + "\n" +
                    "Absent: " + absentCount + "\n" +
                    "Attendance Percentage: " + String.format(Locale.getDefault(), "%.2f", overallAttendancePercentage) + "%";
            summaryTextDataStructure.setText(summary);
        } else if (subject.equals("C Programming")) {
            setupPieChart(pieChartCProgramming, overallAttendancePercentage);
            String summary = "Subject: "+ subject + "\n"+
                    "Roll Number: " + rollNumber + "\n"+
                    "Total Classes: " + totalClasses + "\n" +
                    "Present: " + presentCount + "\n" +
                    "Absent: " + absentCount + "\n" +
                    "Attendance Percentage: " + String.format(Locale.getDefault(), "%.2f", overallAttendancePercentage) + "%";
            summaryTextCProgramming.setText(summary);
        }
    }

    private ArrayList<String> getDateRange(String startDate, String endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        ArrayList<String> dateRange = new ArrayList<>();
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            if (start != null && end != null) {
                long time = start.getTime();
                while (time <= end.getTime()) {
                    dateRange.add(dateFormat.format(new Date(time)));
                    time += 86400000; // Add one day in milliseconds
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateRange;
    }
    private void setupPieChart(PieChart pieChart, float attendancePercentage) {
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        pieEntries.add(new PieEntry(attendancePercentage, "Present"));
        pieEntries.add(new PieEntry(100 - attendancePercentage, "Absent"));

        PieDataSet dataSet = new PieDataSet(pieEntries, "Attendance Distribution");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate(); // Refresh the chart
    }
    // Method to show DatePickerDialog
    private void showDatePickerDialog(final EditText dateInput) {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            // Format the date and set it to EditText
            String formattedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, month1 + 1, year1);
            dateInput.setText(formattedDate);

            // Store the date
            if (dateInput == startDateInput) {
                startDate = formattedDate; // Save start date
            } else if (dateInput == endDateInput) {
                endDate = formattedDate; // Save end date
            }
        }, year, month, day);

        datePickerDialog.show();
    }


}
