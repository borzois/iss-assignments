package org.example.teledon.domain;

import java.time.LocalDate;

public class Booking {
    private Long id;
    private BookingStatus status;
    private String comment;
    private Long userId;
    private LocalDate day;
    private int startHour;
    private int endHour;

    public Booking(Long id, BookingStatus status, String comment, Long userId, LocalDate day, int startHour, int endHour) {
        this.id = id;
        this.status = status;
        this.comment = comment;
        this.userId = userId;
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public Booking(Long id, BookingStatus status, Long userId, LocalDate day, int startHour, int endHour) {
        this.id = id;
        this.status = status;
        this.userId = userId;
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    public Booking() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Booking{");
        sb.append("id=").append(id);
        sb.append(", status=").append(status);
        sb.append(", comment='").append(comment).append('\'');
        sb.append(", userId=").append(userId);
        sb.append(", day=").append(day);
        sb.append(", startHour=").append(startHour);
        sb.append(", endHour=").append(endHour);
        sb.append('}');
        return sb.toString();
    }
}
