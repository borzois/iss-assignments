package org.example.teledon.domain;

import java.util.Calendar;

public class Timeslot {
    private int hour;
    private boolean isBooked;

    public Timeslot(int hour, boolean isBooked) {
        this.hour = hour;
        this.isBooked = isBooked;
    }

    public Timeslot(int hour) {
        this.hour = hour;
        isBooked = false;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}
