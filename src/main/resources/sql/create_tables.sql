CREATE DATABASE IF NOT EXISTS flightbookingsystemdb;
USE flightbookingsystemdb;

CREATE TABLE IF NOT EXISTS employees(
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS flights(
    id INT AUTO_INCREMENT PRIMARY KEY,
    departure_airport VARCHAR(255) NOT NULL,
    arrival_airport VARCHAR(255) NOT NULL,
    departure_time DATETIME NOT NULL,
    arrival_time DATETIME NOT NULL,
    available_seats INT NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings(
    id INT AUTO_INCREMENT PRIMARY KEY,
    flight_id INT NOT NULL,
    number_of_seats INT NOT NULL,
    tourist_names VARCHAR(1000) NOT NULL,
    FOREIGN KEY (flight_id) REFERENCES flights(id)
);