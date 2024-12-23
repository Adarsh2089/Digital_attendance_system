package com.adarsh.digitalattendancesystem;

public class Student {
    private String name;
    private String rollNo;
    private String status;
    private String timestamp; // Timestamp


    public Student() {
        // No-argument constructor for Firebase
    }

    public Student(String name, String rollNo, String status) {
        this.name = name;
        this.rollNo = rollNo;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


