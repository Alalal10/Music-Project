This is the link for presentation:
https://www.canva.com/design/DAGkignaNDc/BYJUtytu-04_hkCyxmWYIA/edit?utm_content=DAGkignaNDc&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton


# 🎵 Music Playlist Manager
# 👤 Student Name: Akylbekova Alina
# 📌 Project Description

Java CLI Playlist Manager is a console-based Java application designed to help users manage their music playlists. Users can register, log in, create and edit playlists, add or remove songs, and save all their data to JSON files for later use. The application supports different user roles (regular users and administrators), each with specific permissions and access levels.

# 🎯 Objectives

- The main objectives of this project are:

To build a fully functional command-line interface (CLI) for managing music playlists.

To implement user registration and login functionality with proper validation.

To support role-based access control (user/admin).

To allow users to create, update, and delete playlists and songs.

To ensure data persistence using JSON for saving and loading user and playlist data.

To demonstrate good coding practices using Java and object-oriented principles.

To make the app user-friendly, maintainable, and extendable for future features.

# ✅ Project Requirement List

Here are 10 key features and requirements that guide the project development:

User Registration — Users must be able to register with a unique username.

User Login — Authentication must be handled using username and password.

Role System — Support for at least two roles: "user" and "admin".

Create Playlists — Users can create one or more playlists.

Edit Playlists — Users can rename playlists and change their contents.

Add Songs — Users can add songs to any of their playlists.

Remove Songs — Users can remove songs by title from a playlist.

Rename Songs — Users can edit song titles within a playlist.

Delete Playlists — Users can delete playlists they no longer need.

Save and Load Data (JSON) — All users and playlist data must be saved to and loaded from JSON files for persistence.

# 🛠️ Technical Documentation

# 📚 Data Structures
- Class User

Fields: username, password, role, playlists

Description: Represents a user of the application, including their credentials and playlists.

Data Type: List of playlists (List<Playlist>)

- Class Playlist

Fields: title, songs

Description: Represents a music playlist, with functionality to add and remove songs.

Data Type: List of songs (List<Song>)

- Class Song

Fields: title, artist

Description: A simple model for a music track.

- JSON Files

Used for storing all data (users and their playlists).

Read and write operations are handled using Gson (Google's library for JSON processing).

# 🧠 Algorithms and Logic

- Authentication

Linear search through the list of users loaded from the JSON file.

Comparison of the entered password with the stored one.

- Registration

Check if the username is unique.

Validate password length and role correctness.

Add the new user to the list and save it back to JSON.

- Playlist Management

Search for a playlist by its title.

Add and remove songs from the playlist (List<Song>) with duplicate checks.

- Search and Edit

Use for-each loops and conditionals to search for songs or playlists by title.

Modify fields of objects directly when a match is found.

- Save/Load

All data is serialized and deserialized using Gson.

Files: users.json, playlists.json (if separated) or one combined data.json.

# 🧩 Modules and Classes

Class/Module	Purpose

App.java:	                  Entry point, CLI menu, command handling

UserService.java:         	User registration, authentication, role management

PlaylistService.java:      	Playlist and song management (add, remove, edit)

JsonStorage.java:         	Load and save data to/from JSON

User.java:	                User model

Playlist.java:            	Playlist model

Song.java:                 	Song model

# 🚧 Challenges and Issues

Working with JSON

Challenges with serializing nested structures (User → Playlist → Song).

Needed to configure Gson to properly handle lists of objects.

Role Management

Implementing access control for administrators and regular users was challenging (for example, only admins can delete other users).

Editing Entities

Initially, organizing the logic for searching and editing by titles was tricky — had to add helper methods in services to make it more efficient.


# 💻 Sample User Inputs and Expected Outputs

# 1. User Registration
   
   This is the registration for regular user. The programme asks for the requirements for the user to register:

    === Welcome to Music Playlist App ===
    [1] Login
    [2] Register
    [0] Exit
    Select option: 2

    --- Register ---

    Username requirements:
       - At least 4 characters
       - Only letters, numbers and underscores

    Username: NewUser
    Password requirements:
       - At least 8 characters
       - At least one uppercase letter
       - At least one lowercase letter
       - At least one digit
    Password: New12345
    Email: newuser@gmail.com
    Admin password (leave blank if not admin): 
    Registration successful! Logged in as NewUser

    --- User Menu ---
    [1] Manage playlists
    [0] Logout
    Choose an option: 
   
  This is the registration of a user with admin rights (the programme asks for a password):
  

    === Welcome to Music Playlist App ===
    [1] Login
    [2] Register
    [0] Exit
    Select option: 2

    --- Register ---

    Username requirements:
       - At least 4 characters
       - Only letters, numbers and underscores

    Username: NewAdmin
    Password requirements:
       - At least 8 characters
       - At least one uppercase letter
       - At least one lowercase letter
       - At least one digit
    Password: NewAdmin123
    Email: newadmin@gmail.com
    Admin password (leave blank if not admin): admin123
    Registration successful! Logged in as NewAdmin

    --- Admin Menu ---
    [1] View all users
    [2] Delete user
    [3] Manage playlists
    [0] Logout
    Choose an option: 
        
If the user gives the programme an incorrect input that does not meet the requirements, the programme gives him an error:
For example, this is error if input in username is wrong:
 
        --- Register ---

    Username requirements:
       - At least 4 characters
       - Only letters, numbers and underscores

    Username: us
    Password requirements:
       - At least 8 characters
       - At least one uppercase letter
       - At least one lowercase letter
       - At least one digit
    Password: user00000
    Email: user1234@gmail.com
    Admin password (leave blank if not admin): 
    Error: Username must be at least 4 characters long
    
This is error if input in password wrong:

    --- Register ---

    Username requirements:
       - At least 4 characters
       - Only letters, numbers and underscores

    Username: User0
    Password requirements:
       - At least 8 characters
       - At least one uppercase letter
       - At least one lowercase letter
       - At least one digit
    Password: user
    Email: user111@gmail.com
    Admin password (leave blank if not admin): 
    Error: Password must be at least 8 characters long     
        
This is error if input in email wrong:
        
       --- Register ---

    Username requirements:
       - At least 4 characters
       - Only letters, numbers and underscores

    Username: user
    Password requirements:
       - At least 8 characters
       - At least one uppercase letter
       - At least one lowercase letter
       - At least one digit
    Password: User00000
    Email: us
    Admin password (leave blank if not admin): 
    Error: Invalid email format


# 2. User Login

After registration or if the user already has an account, they can log in. The programme welcomes user and shows us the different possibilities for a regular user and for an admin.
For Admin:

    === Welcome to Music Playlist App ===
    [1] Login
    [2] Register
    [0] Exit
    Select option: 1

    --- Login ---
    Username: NewAdmin
    Password: NewAdmin123
    Welcome back, NewAdmin!

    --- Admin Menu ---
    [1] View all users
    [2] Delete user
    [3] Manage playlists
    [0] Logout
    Choose an option: 
    
For regular user:

    === Welcome to Music Playlist App ===
    [1] Login
    [2] Register
    [0] Exit
    Select option: 1

    --- Login ---
    Username: NewUser
    Password: New12345
    Welcome back, NewUser!

    --- User Menu ---
    [1] Manage playlists
    [0] Logout
    Choose an option: 

# 3. Manage Playlists

Users can manage their playlists. When you select the "Manage playlists" function, the menu opens.

    --- User Menu ---
    [1] Manage playlists
    [0] Logout
    Choose an option: 1

    --- Playlist Management ---
    [1] View my playlists
    [2] Create playlist
    [3] Rename playlist
    [4] Delete playlist
    [5] Add song
    [6] Edit song
    [7] Remove song
    [8] Add to favorites
    [9] Remove from favorites
    [0] Back
    Choose an option: 

For example, let's create a new playlist and add new song into it:

    -- Playlist Management ---
    [1] View my playlists
    [2] Create playlist
    [3] Rename playlist
    [4] Delete playlist
    [5] Add song
    [6] Edit song
    [7] Remove song
    [8] Add to favorites
    [9] Remove from favorites
    [0] Back
    Choose an option: 2
    Enter playlist name: NewPlaylist
    Playlist created.

    --- Playlist Management ---
    [1] View my playlists
    [2] Create playlist
    [3] Rename playlist
    [4] Delete playlist
    [5] Add song
    [6] Edit song
    [7] Remove song
    [8] Add to favorites
    [9] Remove from favorites
    [0] Back
    Choose an option: 5
    Playlist ID: 4
    Song title: NewSong
    Artist: Artist
    Duration (sec): 212
    Song added.

    --- Playlist Management ---
    [1] View my playlists
    [2] Create playlist
    [3] Rename playlist
    [4] Delete playlist
    [5] Add song
    [6] Edit song
    [7] Remove song
    [8] Add to favorites
    [9] Remove from favorites
    [0] Back
    Choose an option: 1
    Playlist ID: 4 | Name: NewPlaylist | Favorite: No
    Songs:
    - ID: 3 | NewSnog by Artist (212 sec)


Each playlist and song has its own ID to make it easier to manage them. Users can exercise all of the above features, and also have the ability to add playlists to the Favourites folder.

# 4. Admin options

The administrator has the same capabilities as regular users, with the addition of the ability to allow users to see from the full list.

    --- Admin Menu ---
    [1] View all users
    [2] Delete user
    [3] Manage playlists
    [0] Logout
    Choose an option: 1
    
    --- Active Users ---
    ID: 2 | Username: Admin | Email: admin@gmail.com | Role: admin
    ID: 3 | Username: User1 | Email: user@gmail.com | Role: user
    ID: 6 | Username: NewUser | Email: newuser@gmail.com | Role: user
    ID: 7 | Username: NewAdmin | Email: newadmin@gmail.com | Role: admin
    
Each user has their own ID

    --- Admin Menu ---
    [1] View all users
    [2] Delete user
    [3] Manage playlists
    [0] Logout
    Choose an option: 2
    Enter user ID to delete: 3
    User deleted successfully.

# Thank you for attention! 😊❤️
