package org.example.teledon.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.teledon.domain.Role;
import org.example.teledon.domain.User;
import org.example.teledon.utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserDBRepository extends UserRepository {
    JdbcUtils jdbcUtils;
    private static final Logger logger = LogManager.getLogger();

    public UserDBRepository(Properties properties) {
        this.jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public void add(User elem) {
        logger.traceEntry("saving entity {}", elem);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement =
                    con.prepareStatement("INSERT INTO users(email, password, role) VALUES (?, ?, ?)")){
            preparedStatement.setString(1, elem.getEmail());
            preparedStatement.setString(2, elem.getPassword());
            preparedStatement.setInt(3, elem.getRole().ordinal());
            int result = preparedStatement.executeUpdate();
            logger.trace("saved {}", result);
        }
        catch (SQLException e) {
            logger.error(e);
        }
        logger.traceExit();
    }

    @Override
    public void delete(User elem) {

    }

    @Override
    public void update(User elem, Long aLong) {

    }

    @Override
    public User findOne(Long aLong) {
        logger.traceEntry("searching for entity with id {}", aLong);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT * FROM users WHERE user_id=?")) {
            preparedStatement.setLong(1, aLong);
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    Long id = result.getLong("user_id");
                    String email = result.getString("email");
                    String password = result.getString("password");
                    Role role = Role.values()[result.getInt("role")];

                    logger.trace("loaded {}", result);
                    return new User(id, email, password, role);
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
    public User findOne(String email, String password) {
        logger.traceEntry("searching for entity with email {} and password {}", email, password);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT * FROM users WHERE email=? AND password=?")) {
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    Long id = result.getLong("user_id");
                    String newEmail = result.getString("email");
                    String newPassword = result.getString("password");
                    Role role = Role.values()[result.getInt("role")];

                    logger.trace("loaded {}", result);
                    return new User(id, newEmail, newPassword, role);
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
    public User findOne(String email) {
        logger.traceEntry("searching for entity with email {}", email);
        Connection con = jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT * FROM users WHERE email=?")) {
            preparedStatement.setString(1, email);
            try (ResultSet result = preparedStatement.executeQuery()) {
                if (result.next()) {
                    Long id = result.getLong("user_id");
                    String newEmail = result.getString("email");
                    String newPassword = result.getString("password");
                    Role role = Role.values()[result.getInt("role")];

                    logger.trace("loaded {}", result);
                    return new User(id, newEmail, newPassword, role);
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
    public Iterable<User> findAll() {
        logger.traceEntry("getting entities");
        Connection con = jdbcUtils.getConnection();
        List<User> users = new ArrayList<>();
        try(PreparedStatement preparedStatement =
                    con.prepareStatement("SELECT * FROM users")) {
            try (ResultSet result = preparedStatement.executeQuery()) {
                while (result.next()) {
                    Long id = result.getLong("user_id");
                    String newEmail = result.getString("email");
                    String newPassword = result.getString("password");
                    Role role = Role.values()[result.getInt("role")];

                    users.add(new User(id, newEmail, newPassword, role));
                    logger.trace("loaded {}", result);
                }
            }
        }
        catch (SQLException e) {
            logger.error(e);
            System.out.println("error " + e);
        }
        logger.traceExit();
        return users;
    }
}
