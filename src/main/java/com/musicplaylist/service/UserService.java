package com.musicplaylist.service;

import com.musicplaylist.model.User;
import com.musicplaylist.exception.DatabaseException;
import com.musicplaylist.exception.InvalidInputException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class UserService {
    private final LogService logService = new LogService();

    public User registerUser(String username, String password, String email, String adminPassword)
            throws InvalidInputException, DatabaseException {

        if (username == null || password == null) {
            throw new InvalidInputException("Username and password cannot be null");
        }

        validateUsername(username);
        validatePassword(password);
        if (email != null && !email.isEmpty()) {
            validateEmail(email);
        }

        String role = "user";
        if (adminPassword != null && !adminPassword.isEmpty()) {
            if (!"admin123".equals(adminPassword)) {
                throw new InvalidInputException("Invalid admin password");
            }
            role = "admin";
        }

        if (usernameExists(username)) {
            throw new DatabaseException("Username already exists");
        }

        if (email != null && !email.isEmpty() && emailExists(email)) {
            throw new DatabaseException("Email already exists");
        }

        String sql = "INSERT INTO users(username, password, email, role) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);
            pstmt.setString(4, role);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("User creation failed");
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    logService.logAction(id, "REGISTER");
                    return new User(id, username, password, email, role, true);
                } else {
                    throw new DatabaseException("User creation failed - no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error creating user: " + e.getMessage());
        }
    }

    private boolean usernameExists(String username) throws DatabaseException {
        String sql = "SELECT 1 FROM users WHERE username = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking username existence: " + e.getMessage());
        }
    }

    private boolean emailExists(String email) throws DatabaseException {
        if (email == null || email.isEmpty()) {
            return false;
        }

        String sql = "SELECT 1 FROM users WHERE email = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking email existence: " + e.getMessage());
        }
    }

    public User login(String username, String password) throws DatabaseException, InvalidInputException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND is_active = TRUE";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("email"),
                            rs.getString("role"),
                            rs.getBoolean("is_active"));

                    logService.logAction(user.getId(), "LOGIN");
                    return user;
                } else {
                    throw new InvalidInputException("Invalid username or password");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error during login: " + e.getMessage());
        }
    }

    public void deleteUser(int userId, int currentUserId) throws DatabaseException {
        if (userId == currentUserId) {
            throw new DatabaseException("You cannot delete your own account");
        }

        String sql = "UPDATE users SET is_active = FALSE WHERE id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("User not found or already deleted");
            }

            logService.logAction(currentUserId, "DELETE_USER " + userId);
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting user: " + e.getMessage());
        }
    }

    public List<User> getAllUsers() throws DatabaseException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE is_active = TRUE";

        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                users.add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        rs.getString("role"),
                        rs.getBoolean("is_active")));
            }

            return users;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving users: " + e.getMessage());
        }
    }

    private void validateUsername(String username) throws InvalidInputException {
        if (username == null || username.length() < 4) {
            throw new InvalidInputException("Username must be at least 4 characters long");
        }

        if (!Pattern.matches("^[a-zA-Z0-9_]+$", username)) {
            throw new InvalidInputException("Username can only contain letters, numbers and underscores");
        }
    }

    private void validatePassword(String password) throws InvalidInputException {
        if (password == null || password.length() < 8) {
            throw new InvalidInputException("Password must be at least 8 characters long");
        }

        if (!Pattern.matches(".*[A-Z].*", password)) {
            throw new InvalidInputException("Password must contain at least one uppercase letter");
        }

        if (!Pattern.matches(".*[a-z].*", password)) {
            throw new InvalidInputException("Password must contain at least one lowercase letter");
        }

        if (!Pattern.matches(".*\\d.*", password)) {
            throw new InvalidInputException("Password must contain at least one digit");
        }
    }

    private void validateEmail(String email) throws InvalidInputException {
        if (!Pattern.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
            throw new InvalidInputException("Invalid email format");
        }
    }
}