package org.example.teledon.service;

import javafx.scene.control.TextArea;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.teledon.domain.*;
import org.example.teledon.repository.BookingRepository;
import org.example.teledon.repository.UserRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MainService {
    UserRepository userRepository;
    BookingRepository bookingRepository;
    private static final Logger logger = LogManager.getLogger();


    public MainService(UserRepository userRepository, BookingRepository bookingRepository) {
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    public User register(String email, String password) {
        User user = userRepository.findOne(email);
        if (user != null) {
            return null;
        }
        userRepository.add(new User(email, password, Role.CLIENT));
        return userRepository.findOne(email, password);
    }

    public User login(String email, String password) {
        return userRepository.findOne(email, password);
    }

    public List<Booking> getBookings(User user) {
        List<Booking> bookings = new ArrayList<>();
        bookingRepository.find(user.getId()).forEach(bookings::add);
        return bookings;
    }

    public List<Timeslot> getTimeslots(LocalDate date) {
        Iterable<Booking> bookings = bookingRepository.find(date);

        List<Timeslot> day = generateDay(8, 22);
        if (bookings != null) {
            for (Booking booking : bookings) {
                if (booking.getStatus() == BookingStatus.ACTIVE) {
                    for (int h = booking.getStartHour(); h <= booking.getEndHour(); h++) {
                        logger.info("SETTING {}", h - 8);
                        day.set(h - 8, new Timeslot(h, true));
                    }
                }
            }
        }
        logger.info("DAY IS {} TO {}", day.get(0).getHour(), day.get(day.size()-1).getHour());
        return day;
    }

    private List<Timeslot> generateDay(int startHour, int endHour) {
        List<Timeslot> day = new ArrayList<>();
        for (int h = startHour; h < endHour; h++) {
            day.add(new Timeslot(h));
        }
        return day;
    }

    public void addBooking(Long userId, LocalDate day, int startHour, int endHour, String comment) {
        bookingRepository.add(
                new Booking(
                        0L,
                        BookingStatus.ACTIVE,
                        comment,
                        userId,
                        day,
                        startHour,
                        endHour
                        ));
    }

    public void cancelBooking(Booking booking) {
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.update(booking, booking.getId());
    }

    public Booking getBooking(LocalDate day, int startHour) {
        return bookingRepository.find(day, startHour);
    }

    public void modifyBooking(Booking booking) {
        bookingRepository.update(booking, booking.getId());
    }
}
