package com.hkhasib;

import java.time.LocalDate;
import java.time.LocalTime;

public class Appointments {
    private int doctorID, PatientID;
    private String note, emergency;
    private LocalDate date;
    private LocalTime time;

    public Appointments(int doctorID, int patientID, String note, String emergency, LocalDate date, LocalTime time) {
        this.doctorID = doctorID;
        PatientID = patientID;
        this.note = note;
        this.emergency = emergency;
        this.date = date;
        this.time = time;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public int getPatientID() {
        return PatientID;
    }

    public void setPatientID(int patientID) {
        PatientID = patientID;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}