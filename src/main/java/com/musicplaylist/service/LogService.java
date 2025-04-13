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

    public String getActivityLogs(int adminUserId) throws SQLException {
        StringBuilder logs = new StringBuilder();
        String sql = "SELECT * FROM activity_logs ORDER BY timestamp DESC LIMIT 50";

        try (Connection conn = DatabaseService.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            logs.append("=== Recent Activity Logs ===\n");
            while (rs.next()) {
                logs.append(String.format("[%s] User %d: %s\n",
                        rs.getTimestamp("timestamp"),
                        rs.getInt("user_id"),
                        rs.getString("action")));
            }
        }

        return logs.toString();
    }
}