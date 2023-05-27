package org.example.teledon.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.teledon.domain.Booking;
import org.example.teledon.domain.BookingStatus;
import org.example.teledon.domain.Role;
import org.example.teledon.domain.User;
import org.example.teledon.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BookingDBRepository extends BookingRepository {
    JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public BookingDBRepository(Properties properties) {
        this.jdbcUtils = new JdbcUtils(properties);
    }
    @Override
    public Iterable<Booking> find(LocalDate date) {
        long day = date.toEpochDay();
        logger.traceEntry("getting entities");
        Connection con = jdbcUtils.getConnection();
        List<Booking> bookings = new ArrayList<>();
        try(PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT * FROM bookings WHERE day=?")) {
            preparedStatement.setLong(1, day);

            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("booking_id");
                    BookingStatus status = BookingStatus.values()[result.getInt("status")];
                    String comment = result.getString("comment");
                    Long userId = result.getLong("user_id");
                    int startHour = result.getInt("start_hour");
                    int endHour = result.getInt("end_hour");

                    bookings.add(new Booking(id, status, comment, userId, LocalDate.ofEpochDay(day), startHour, endHour));
                    logger.trace("loaded {}", result);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("error " + e);
        }
        logger.traceExit();
        return bookings;
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
