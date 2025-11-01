package com.stocktracker.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.stocktracker.model.User;
import com.stocktracker.util.DBConnection;

public class UserDAO {

    // SQL statements
    private static final String INSERT_USER_SQL =
        "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
    private static final String SELECT_BY_EMAIL_SQL =
        "SELECT id, username, email, password FROM users WHERE email = ?";
    private static final String CHECK_EMAIL_SQL =
        "SELECT id FROM users WHERE email = ?";

    // Register user - returns true if inserted
    public boolean registerUser(User user) {
        // check email exists
        if (isEmailTaken(user.getEmail())) {
            return false;
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_USER_SQL)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword()); // consider hashing
            int rows = ps.executeUpdate();
            return rows == 1;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Login - returns User if credentials match, otherwise null
    public User login(String email, String password) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BY_EMAIL_SQL)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String dbPassword = rs.getString("password");
                    // For production: verify hashed password instead
                    if (dbPassword != null && dbPassword.equals(password)) {
                        return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            dbPassword
                        );
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper - check if email already exists
    private boolean isEmailTaken(String email) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(CHECK_EMAIL_SQL)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return true; // treat error as taken to avoid duplicate entries
        }
    }
}
