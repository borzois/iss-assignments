package org.example.teledon.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.teledon.domain.Booking;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.time.LocalDate;
import java.util.List;


public class BookingORMRepository extends BookingRepository {
    SessionFactory sessionFactory;
    private static final Logger logger = LogManager.getLogger();

    public BookingORMRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Iterable<Booking> find(LocalDate date) {
        logger.traceEntry("FIND ENTITY");
        try (Session session = sessionFactory.openSession()) {
            logger.info("OBTAINED SESSION");
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                List<Booking> bookings = session
                        .createQuery("from Booking as c where c.day = :day", Booking.class)
                        .setParameter("day", date)
                        .list();
                logger.info(bookings.size() + " booking(s) found:");

                tx.commit();
                return bookings;
            } catch (RuntimeException e) {
                logger.error(e);
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        logger.traceExit();
        return null;
    }

    @Override
    public Iterable<Booking> find(Long userId) {
        return null;
    }

    @Override
    public Booking find(LocalDate day, int startHour) {
        return null;
    }

    @Override
    public void add(Booking elem) {

    }

    @Override
    public void delete(Booking elem) {

    }

    @Override
    public void update(Booking elem, Long aLong) {

    }

    @Override
    public Booking findOne(Long aLong) {
        return null;
    }

    @Override
    public Iterable<Booking> findAll() {
        return null;
    }
}
