package com.hkhasib;

import java.time.LocalTime;

public class Routine {
    private String day;
    private LocalTime from, to;
    private int doctorID;

    public Routine(int doctorID, LocalTime from, LocalTime to,String day) {
        this.day = day;
        this.from = from;
        this.to = to;
        this.doctorID = doctorID;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalTime getFrom() {
        return from;
    }

    public void setFrom(LocalTime from) {
        this.from = from;
    }

    public LocalTime getTo() {
        return to;
    }

    public void setTo(LocalTime to) {
        this.to = to;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }
}
