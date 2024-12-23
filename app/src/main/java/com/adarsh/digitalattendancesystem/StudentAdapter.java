package com.adarsh.digitalattendancesystem;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private DatabaseReference databaseReference;

    public StudentAdapter(List<Student> studentList, DatabaseReference databaseReference) {
        this.studentList = studentList;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.rollNo.setText(student.getRollNo());
        holder.name.setText(student.getName());

        holder.btnPresent.setOnClickListener(v -> {
            student.setStatus("Present");
            student.setTimestamp(getCurrentTime()); // Set the formatted time string
            notifyItemChanged(position);
            saveAttendanceToFirebase(student);
        });

        holder.btnAbsent.setOnClickListener(v -> {
            student.setStatus("Absent");
            student.setTimestamp(getCurrentTime()); // Set the formatted time string
            notifyItemChanged(position);
            saveAttendanceToFirebase(student);
        });

        // Update button states
        if ("Present".equals(student.getStatus())) {
            holder.btnPresent.setEnabled(false);
            holder.btnAbsent.setEnabled(true);
        } else if ("Absent".equals(student.getStatus())) {
            holder.btnPresent.setEnabled(true);
            holder.btnAbsent.setEnabled(false);
        } else {
            holder.btnPresent.setEnabled(true);
            holder.btnAbsent.setEnabled(true);
        }
    }
    private String getCurrentTime() {
        // Format the current time in HH:mm:ss format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date()); // Return the formatted time
    }


    private void saveAttendanceToFirebase(Student student) {
        String rollNo = student.getRollNo();

        databaseReference.child(rollNo).setValue(student)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firebase", "Attendance saved successfully for " + student.getName());
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Error saving attendance", e);
                });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView rollNo, name;
        Button btnPresent, btnAbsent;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            rollNo = itemView.findViewById(R.id.studentRollNo);
            name = itemView.findViewById(R.id.studentName);
            btnPresent = itemView.findViewById(R.id.btnPresent);
            btnAbsent = itemView.findViewById(R.id.btnAbsent);
        }
    }
}
