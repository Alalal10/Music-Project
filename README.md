This is the link for presentation:
https://www.canva.com/design/DAGkignaNDc/BYJUtytu-04_hkCyxmWYIA/edit?utm_content=DAGkignaNDc&utm_campaign=designshare&utm_medium=link2&utm_source=sharebutton


# Music Playlist Manager
# Student Name: Akylbekova Alina
# Project Description

Music Playlist Manager is a console-based Java application (CLI) that allows users to manage music playlists. Users can register, log in, create and edit playlists, and add or remove songs. The app features a role-based access system (regular users and administrators), action logging, and stores all data in a SQLite database using JDBC.

# Objectives

Build a functional CLI application for managing music playlists

Implement user registration and login with validation

Support role-based access control (user / admin)

Allow CRUD operations on playlists and songs

Store data in a SQLite database via JDBC

Log user actions like registration, login, and deletions

Follow clean code and object-oriented principles

# Project Requirement List

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

Data persistence via SQLite database

# Technical Documentation
# Data Storage

The app uses a SQLite database to store all data persistently.

JDBC (Java Database Connectivity) is used to connect and execute queries.

All user, playlist, and song data is stored in corresponding tables in the database.

A LogService is used to track and store user actions like registration, login, and deletion.

# Data Structures (Java Classes)

- User

Fields: id, username, password, email, role, isActive

Purpose: Represents a user of the application, with credentials and role (user or admin)

Stored in: users table in SQLite

- Playlist

Fields: id, userId, title

Purpose: Represents a music playlist that belongs to a user

Stored in: playlists table in SQLite

- Song

Fields: id, playlistId, title, artist, isFavorite

Purpose: Represents a song inside a playlist

Stored in: songs table in SQLite


# Logic and Algorithms

- Authentication

Search in users table by username and password.

Check that the account is marked as is_active = true.

- Registration

Validate fields (username, password, email).

Check for unique username and email in the database.

Insert a new row into the users table with appropriate role.

- Role Management
  
Users with role "admin" can:

View all users

Delete (deactivate) users

See action logs

Regular users can:

Only manage their own playlists and songs

- Playlist Management
  
Users can:

Create new playlists

Rename and delete their own playlists

Add, rename, and remove songs inside their playlists

Uses user_id and playlist_id to establish relations between tables

- Logging
  
Every action (registration, login, delete, etc.) is logged in a logs table.

The LogService class handles inserting log entries into the database.


# Modules and Classes

Class	Purpose

App.java:	Entry point, CLI menu, user interaction

UserService.java:	Handles registration, login, deletion, validation, role checking

PlaylistService.java:	Manages playlists and songs (CRUD)

DatabaseService.java:	Provides SQLite connection and database setup

LogService.java:	Logs user actions into database

User.java:	User model class

Playlist.java:	Playlist model class

Song.java:	Song model class


# Sample User Inputs and Expected Outputs

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

