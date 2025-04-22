package com.musicplaylist.service;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DatabaseService {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            Properties prop = new Properties();
            try (InputStream input = DatabaseService.class.getClassLoader()
                    .getResourceAsStream("config.properties")) {
                if (input != null) {
                    prop.load(input);
                }
            } catch (IOException ignored) {
            }

            String url = prop.getProperty("db.url", "jdbc:sqlite:musicp_laylist.db");
            connection = DriverManager.getConnection(url);
            initializeDatabase(); 
        }
        return connection;
    }

    private static void initializeDatabase() throws SQLException {

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("PRAGMA foreign_keys = ON");

            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE NOT NULL," +
                    "password TEXT NOT NULL," +
                    "email TEXT UNIQUE," +
                    "role TEXT NOT NULL DEFAULT 'user'," +
                    "is_active BOOLEAN NOT NULL DEFAULT TRUE)");

            stmt.execute("CREATE TABLE IF NOT EXISTS playlists (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "user_id INTEGER NOT NULL," +
                    "is_favorite BOOLEAN NOT NULL DEFAULT FALSE," +
                    "FOREIGN KEY(user_id) REFERENCES users(id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS songs (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT NOT NULL," +
                    "artist TEXT NOT NULL," +
                    "duration INTEGER NOT NULL," +
                    "playlist_id INTEGER NOT NULL," +
                    "FOREIGN KEY(playlist_id) REFERENCES playlists(id))");

            stmt.execute("CREATE TABLE IF NOT EXISTS activity_logs (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_id INTEGER NOT NULL," +
                    "action TEXT NOT NULL," +
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "FOREIGN KEY(user_id) REFERENCES users(id))");
        }
    }
}
