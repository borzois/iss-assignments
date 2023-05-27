package org.example.teledon.repository;

import org.example.teledon.domain.Booking;

import java.time.LocalDate;

public abstract class BookingRepository implements Repository<Booking, Long> {
    public abstract Iterable<Booking> find(LocalDate day);
}
