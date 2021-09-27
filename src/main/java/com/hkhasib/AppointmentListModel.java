package com.hkhasib;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentListModel {
    private String doctorName, patientName, phone, department, emergency;
    private LocalTime time;
    private LocalDate date;
    private int doctorID, patientID;


    public AppointmentListModel(String doctorName, String patientName, String phone, String department, String emergency, LocalTime time, LocalDate date, int doctorID, int patientID) {
        this.doctorName = doctorName;
        this.patientName = patientName;
        this.phone = phone;
        this.department = department;
        this.emergency = emergency;
        this.time = time;
        this.date = date;
        this.doctorID = doctorID;
        this.patientID = patientID;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }
}
