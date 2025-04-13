package com.musicplaylist.model;

public class Song {
    private int id;
    private String title;
    private String artist;
    private int duration;
    private int playlistId;

    public Song(int id, String title, String artist, int duration, int playlistId) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.playlistId = playlistId;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public int getDuration() { return duration; }
    public int getPlaylistId() { return playlistId; }

    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setDuration(int duration) { this.duration = duration; }

    @Override
    public String toString() {
        return String.format("ID: %d | %s by %s (%d sec)", id, title, artist, duration);
    }
}
