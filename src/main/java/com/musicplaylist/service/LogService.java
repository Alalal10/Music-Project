package com.musicplaylist.service;

import java.sql.*;

public class LogService {
    public void logAction(int userId, String action) {
        String sql = "INSERT INTO activity_logs(user_id, action) VALUES(?, ?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, action);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Failed to log action: " + e.getMessage());
        }
    }
}
