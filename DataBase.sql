-- Create the database if it doesn't already exist
CREATE DATABASE IF NOT EXISTS event_management;
USE event_management;

-- Create a table for users
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_admin BOOLEAN NOT NULL DEFAULT FALSE
);

-- Create a table for events
CREATE TABLE events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    slots INT NOT NULL,
    registration_deadline DATE NOT NULL,
    location VARCHAR(255) NOT NULL
);

-- Create a table for notifications
-- Notifications are linked to events and contain a message
CREATE TABLE notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    event_id INT NOT NULL,
    message TEXT NOT NULL,
    date_sent DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE
);


CREATE TABLE registrations (
    event_id INT,
    user_id INT,
    registration_username VARCHAR(255),
    name VARCHAR(255),
    age INT,
    email VARCHAR(255),
    phone VARCHAR(255),
    PRIMARY KEY (event_id, user_id),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
