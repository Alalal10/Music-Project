package com.musicplaylist.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
    private boolean isActive;

    public User(int id, String username, String password, String email, String role, boolean isActive) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public boolean isActive() { return isActive; }
}