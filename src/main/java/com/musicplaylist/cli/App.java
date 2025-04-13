package com.musicplaylist.cli;

import com.musicplaylist.exception.DatabaseException;
import com.musicplaylist.exception.InvalidInputException;
import com.musicplaylist.model.Playlist;
import com.musicplaylist.model.User;
import com.musicplaylist.service.PlaylistService;
import com.musicplaylist.service.UserService;

import java.util.List;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    private static final UserService userService = new UserService();
    private static final PlaylistService playlistService = new PlaylistService();
    private static User currentUser = null;

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            if (currentUser == null) {
                System.out.println("\n=== Welcome to Music Playlist App ===");
                System.out.println("[1] Login");
                System.out.println("[2] Register");
                System.out.println("[0] Exit");
                System.out.print("Select option: ");
                String choice = scanner.nextLine();

                switch (choice) {
                    case "1" -> login();
                    case "2" -> register();
                    case "0" -> exit = true;
                    default -> System.out.println("Invalid option.");
                }
            } else {
                if ("admin".equalsIgnoreCase(currentUser.getRole())) {
                    showAdminMenu();
                } else {
                    showUserMenu();
                }
            }
        }

        System.out.println("Goodbye!");
    }

    private static void register() {
        try {
            System.out.println("\n--- Register ---" +
            "\n");
            System.out.println("Username requirements:\n" +
                    "       - At least 4 characters\n" +
                    "       - Only letters, numbers and underscores\n");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.println("Password requirements:\n" +
                    "       - At least 8 characters\n" +
                    "       - At least one uppercase letter\n" +
                    "       - At least one lowercase letter\n" +
                    "       - At least one digit");
            System.out.print("Password: ");
            String password = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Admin password (leave blank if not admin): ");
            String adminPassword = scanner.nextLine();

            currentUser = userService.registerUser(username, password, email, adminPassword);
            System.out.println("Registration successful! Logged in as " + currentUser.getUsername());
        } catch (InvalidInputException | DatabaseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void login() {
        try {
            System.out.println("\n--- Login ---");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            currentUser = userService.login(username, password);
            System.out.println("Welcome back, " + currentUser.getUsername() + "!");
        } catch (InvalidInputException | DatabaseException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void logout() {
        currentUser = null;
        System.out.println("Logged out successfully.");
    }

    private static void showAdminMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("[1] View all users");
        System.out.println("[2] Delete user");
        System.out.println("[3] Manage playlists");
        System.out.println("[0] Logout");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> viewAllUsers();
            case "2" -> deleteUser();
            case "3" -> managePlaylists();
            case "0" -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private static void showUserMenu() {
        System.out.println("\n--- User Menu ---");
        System.out.println("[1] Manage playlists");
        System.out.println("[0] Logout");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        switch (choice) {
            case "1" -> managePlaylists();
            case "0" -> logout();
            default -> System.out.println("Invalid option.");
        }
    }

    private static void viewAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            System.out.println("\n--- Active Users ---");
            for (User u : users) {
                System.out.printf("ID: %d | Username: %s | Email: %s | Role: %s\n",
                        u.getId(), u.getUsername(), u.getEmail(), u.getRole());
            }
        } catch (DatabaseException e) {
            System.out.println("Error fetching users: " + e.getMessage());
        }
    }

    private static void deleteUser() {
        try {
            System.out.print("Enter user ID to delete: ");
            int userId = Integer.parseInt(scanner.nextLine());

            userService.deleteUser(userId, currentUser.getId());
            System.out.println("User deleted successfully.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
        } catch (DatabaseException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }

    private static void managePlaylists() {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Playlist Management ---");
            System.out.println("[1] View my playlists");
            System.out.println("[2] Create playlist");
            System.out.println("[3] Rename playlist");
            System.out.println("[4] Delete playlist");
            System.out.println("[5] Add song");
            System.out.println("[6] Edit song");
            System.out.println("[7] Remove song");
            System.out.println("[8] Add to favorites");
            System.out.println("[9] Remove from favorites");
            System.out.println("[0] Back");

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> {
                        List<Playlist> playlists = playlistService.getUserPlaylists(currentUser.getId());
                        for (Playlist pl : playlists) {
                            System.out.println(pl);
                        }
                    }
                    case "2" -> {
                        System.out.print("Enter playlist name: ");
                        String name = scanner.nextLine();
                        playlistService.createPlaylist(name, currentUser.getId(), false);
                        System.out.println("Playlist created.");
                    }
                    case "3" -> {
                        System.out.print("Playlist ID to rename: ");
                        int pid = Integer.parseInt(scanner.nextLine());
                        System.out.print("New name: ");
                        String name = scanner.nextLine();
                        playlistService.updatePlaylist(pid, name, currentUser.getId());
                        System.out.println("Playlist renamed.");
                    }
                    case "4" -> {
                        System.out.print("Playlist ID to delete: ");
                        int pid = Integer.parseInt(scanner.nextLine());
                        playlistService.deletePlaylist(pid, currentUser.getId());
                        System.out.println("Playlist deleted.");
                    }
                    case "5" -> {
                        System.out.print("Playlist ID: ");
                        int pid = Integer.parseInt(scanner.nextLine());
                        System.out.print("Song title: ");
                        String title = scanner.nextLine();
                        System.out.print("Artist: ");
                        String artist = scanner.nextLine();
                        System.out.print("Duration (sec): ");
                        int duration = Integer.parseInt(scanner.nextLine());
                        playlistService.addSongToPlaylist(pid, title, artist, duration, currentUser.getId());
                        System.out.println("Song added.");
                    }
                    case "6" -> {
                        System.out.print("Playlist ID: ");
                        int pid = Integer.parseInt(scanner.nextLine());
                        System.out.print("Song ID: ");
                        int sid = Integer.parseInt(scanner.nextLine());
                        System.out.print("New title: ");
                        String title = scanner.nextLine();
                        System.out.print("New artist: ");
                        String artist = scanner.nextLine();
                        System.out.print("New duration (sec): ");
                        int duration = Integer.parseInt(scanner.nextLine());
                        playlistService.updateSongInPlaylist(sid, pid, title, artist, duration, currentUser.getId());
                        System.out.println("Song updated.");
                    }
                    case "7" -> {
                        System.out.print("Playlist ID: ");
                        int pid = Integer.parseInt(scanner.nextLine());
                        System.out.print("Song ID: ");
                        int sid = Integer.parseInt(scanner.nextLine());
                        playlistService.removeSongFromPlaylist(sid, pid, currentUser.getId());
                        System.out.println("Song removed.");
                    }
                    case "8" -> {
                        System.out.print("Playlist ID to add to favorites: ");
                        int pid = Integer.parseInt(scanner.nextLine());
                        playlistService.addToFavorites(pid, currentUser.getId());
                        System.out.println("Added to favorites.");
                    }
                    case "9" -> {
                        System.out.print("Playlist ID to remove from favorites: ");
                        int pid = Integer.parseInt(scanner.nextLine());
                        playlistService.removeFromFavorites(pid, currentUser.getId());
                        System.out.println("Removed from favorites.");
                    }
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
