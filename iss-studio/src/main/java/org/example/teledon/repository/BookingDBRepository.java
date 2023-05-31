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
    public Iterable<Booking> find(Long userId) {
        logger.traceEntry("getting entities");
        Connection con = jdbcUtils.getConnection();
        List<Booking> bookings = new ArrayList<>();
        try(PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT * FROM bookings WHERE user_id=?")) {
            preparedStatement.setLong(1, userId);

            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("booking_id");
                    BookingStatus status = BookingStatus.values()[result.getInt("status")];
                    String comment = result.getString("comment");
                    LocalDate date = LocalDate.ofEpochDay(result.getLong("day"));
                    int startHour = result.getInt("start_hour");
                    int endHour = result.getInt("end_hour");

                    bookings.add(new Booking(id, status, comment, userId, date, startHour, endHour));
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
    public Booking find(LocalDate date, int startHour) {
        long day = date.toEpochDay();
        logger.traceEntry("getting entity");
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT * FROM bookings WHERE day=? AND start_hour=?")) {
            preparedStatement.setLong(1, day);
            preparedStatement.setInt(2, startHour);

            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    Long id = result.getLong("booking_id");
                    BookingStatus status = BookingStatus.values()[result.getInt("status")];
                    String comment = result.getString("comment");
                    Long userId = result.getLong("user_id");
                    int endHour = result.getInt("end_hour");

                    return new Booking(id, status, comment, userId, LocalDate.ofEpochDay(day), startHour, endHour);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("error " + e);
        }
        logger.traceExit();
        return null;
    }

    @Override
    public void add(Booking elem) {
        logger.traceEntry("saving entity {}", elem);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement =
                    con.prepareStatement("INSERT INTO bookings(status, comment, user_id, day, start_hour, end_hour) VALUES (?, ?, ?, ?, ?, ?)")){
            preparedStatement.setInt(1, BookingStatus.ACTIVE.ordinal());
            preparedStatement.setString(2, elem.getComment());
            preparedStatement.setLong(3, elem.getUserId());
            preparedStatement.setLong(4, elem.getDay().toEpochDay());
            preparedStatement.setInt(5, elem.getStartHour());
            preparedStatement.setInt(6, elem.getEndHour());
            int result = preparedStatement.executeUpdate();
            logger.trace("saved {}", result);
        }
        catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Booking elem) {

    }

    @Override
    public void update(Booking elem, Long aLong) {
        logger.traceEntry("updating entity {}", elem);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement =
                    con.prepareStatement(
                        "UPDATE bookings " +
                            "SET status=?, comment=?, user_id=?, day=?, start_hour=?, end_hour=? " +
                            "WHERE booking_id=?")){
            preparedStatement.setInt(1, elem.getStatus().ordinal());
            preparedStatement.setString(2, elem.getComment());
            preparedStatement.setLong(3, elem.getUserId());
            preparedStatement.setLong(4, elem.getDay().toEpochDay());
            preparedStatement.setInt(5, elem.getStartHour());
            preparedStatement.setInt(6, elem.getEndHour());
            preparedStatement.setLong(7, aLong);

            int result = preparedStatement.executeUpdate();
            logger.trace("updated {}", result);
        }
        catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit();
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
