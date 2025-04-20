package com.musicplaylist.service;

import com.musicplaylist.model.Playlist;
import com.musicplaylist.model.Song;
import com.musicplaylist.exception.DatabaseException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaylistService {
    private final LogService logService = new LogService();

    public Playlist createPlaylist(String name, int userId, boolean isFavorite) throws DatabaseException {
        String sql = "INSERT INTO playlists(name, user_id, is_favorite) VALUES(?, ?, ?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.setInt(2, userId);
            pstmt.setBoolean(3, isFavorite);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Playlist creation failed");
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    logService.logAction(userId, "CREATE_PLAYLIST " + id);
                    return new Playlist(id, name, userId, isFavorite);
                } else {
                    throw new DatabaseException("Playlist creation failed - no ID obtained");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error creating playlist: " + e.getMessage());
        }
    }

    public void updatePlaylist(int playlistId, String newName, int userId) throws DatabaseException {
        String sql = "UPDATE playlists SET name = ? WHERE id = ? AND user_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newName);
            pstmt.setInt(2, playlistId);
            pstmt.setInt(3, userId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Playlist not found or you don't have permission");
            }

            logService.logAction(userId, "UPDATE_PLAYLIST " + playlistId);
        } catch (SQLException e) {
            throw new DatabaseException("Error updating playlist: " + e.getMessage());
        }
    }

    public void deletePlaylist(int playlistId, int userId) throws DatabaseException {

        String deleteSongsSql = "DELETE FROM songs WHERE playlist_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSongsSql)) {

            pstmt.setInt(1, playlistId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting songs from playlist: " + e.getMessage());
        }


        String deletePlaylistSql = "DELETE FROM playlists WHERE id = ? AND user_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deletePlaylistSql)) {

            pstmt.setInt(1, playlistId);
            pstmt.setInt(2, userId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Playlist not found or you don't have permission");
            }

            logService.logAction(userId, "DELETE_PLAYLIST " + playlistId);
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting playlist: " + e.getMessage());
        }
    }

    public List<Playlist> getUserPlaylists(int userId) throws DatabaseException {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT * FROM playlists WHERE user_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Playlist playlist = new Playlist(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getInt("user_id"),
                            rs.getBoolean("is_favorite"));

                    playlist.getSongs().addAll(getSongsForPlaylist(playlist.getId(), conn));
                    playlists.add(playlist);
                }
            }

            return playlists;
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving playlists: " + e.getMessage());
        }
    }


    public void addSongToPlaylist(int playlistId, String title, String artist, int duration, int userId)
            throws DatabaseException {

        String checkSql = "SELECT 1 FROM songs WHERE playlist_id = ? AND LOWER(title) = LOWER(?)";
        String insertSql = "INSERT INTO songs(title, artist, duration, playlist_id) VALUES(?, ?, ?, ?)";

        try (Connection conn = DatabaseService.getConnection()) {
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setInt(1, playlistId);
                checkStmt.setString(2, title);

                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        throw new DatabaseException("This song already exists in the playlist");
                    }
                }
            }


            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, title);
                insertStmt.setString(2, artist);
                insertStmt.setInt(3, duration);
                insertStmt.setInt(4, playlistId);

                int affectedRows = insertStmt.executeUpdate();

                if (affectedRows == 0) {
                    throw new DatabaseException("Failed to add song");
                }

                logService.logAction(userId, "ADD_SONG " + playlistId);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error adding song to playlist: " + e.getMessage());
        }
    }

    public void updateSongInPlaylist(int songId, int playlistId, String newTitle, String newArtist, int newDuration, int userId)
            throws DatabaseException {
        String sql = "UPDATE songs SET title = ?, artist = ?, duration = ? WHERE id = ? AND playlist_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newTitle);
            pstmt.setString(2, newArtist);
            pstmt.setInt(3, newDuration);
            pstmt.setInt(4, songId);
            pstmt.setInt(5, playlistId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Song not found in this playlist");
            }

            logService.logAction(userId, "UPDATE_SONG " + songId + " IN " + playlistId);
        } catch (SQLException e) {
            throw new DatabaseException("Error updating song: " + e.getMessage());
        }
    }

    private List<Song> getSongsForPlaylist(int playlistId, Connection conn) throws SQLException {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM songs WHERE playlist_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, playlistId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    songs.add(new Song(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("artist"),
                            rs.getInt("duration"),
                            rs.getInt("playlist_id")));
                }
            }
        }

        return songs;
    }


    public void removeSongFromPlaylist(int songId, int playlistId, int userId) throws DatabaseException {
        String sql = "DELETE FROM songs WHERE id = ? AND playlist_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, songId);
            pstmt.setInt(2, playlistId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Song not found in this playlist");
            }

            logService.logAction(userId, "REMOVE_SONG " + songId + " FROM " + playlistId);
        } catch (SQLException e) {
            throw new DatabaseException("Error removing song from playlist: " + e.getMessage());
        }
    }

    public void addToFavorites(int playlistId, int userId) throws DatabaseException {
        String sql = "UPDATE playlists SET is_favorite = TRUE WHERE id = ? AND user_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playlistId);
            pstmt.setInt(2, userId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Playlist not found or you don't have permission");
            }

            logService.logAction(userId, "ADD_TO_FAVORITES " + playlistId);
        } catch (SQLException e) {
            throw new DatabaseException("Error adding to favorites: " + e.getMessage());
        }
    }

    public void removeFromFavorites(int playlistId, int userId) throws DatabaseException {
        String sql = "UPDATE playlists SET is_favorite = FALSE WHERE id = ? AND user_id = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, playlistId);
            pstmt.setInt(2, userId);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new DatabaseException("Playlist not found or you don't have permission");
            }

            logService.logAction(userId, "REMOVE_FROM_FAVORITES " + playlistId);
        } catch (SQLException e) {
            throw new DatabaseException("Error removing from favorites: " + e.getMessage());
        }
    }
}