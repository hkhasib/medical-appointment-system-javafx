package com.hkhasib;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class BasicAppointmentModel {
   private LocalDate date;
   private LocalTime time;
   private String name;

    public BasicAppointmentModel(LocalDate date, LocalTime time, String name) {
        this.date = date;
        this.time = time;
        this.name = name;
    }

    public String getDate() {
        return DateTimeFormatter.ofPattern("dd MMM YYYY", Locale.US).format(date);
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return DateTimeFormatter.ofPattern("hh:mm a").format(time);
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
