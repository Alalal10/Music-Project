package com.musicplaylist.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int id;
    private String name;
    private int userId;
    private boolean isFavorite;
    private List<Song> songs;

    public Playlist(int id, String name, int userId, boolean isFavorite) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.isFavorite = isFavorite;
        this.songs = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getUserId() { return userId; }
    public boolean isFavorite() { return isFavorite; }
    public List<Song> getSongs() { return songs; }

    public void setName(String name) { this.name = name; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Playlist ID: ").append(id)
                .append(" | Name: ").append(name)
                .append(" | Favorite: ").append(isFavorite ? "Yes" : "No")
                .append("\nSongs:\n");

        if (songs.isEmpty()) {
            sb.append("  (No songs in this playlist)\n");
        } else {
            for (Song song : songs) {
                sb.append("  - ").append(song).append("\n");
            }
        }

        return sb.toString();
    }
}
