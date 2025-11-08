-- GoPark Database Schema
-- Version: 1.0
-- Created: 2025
-- This is required! - Earl

DROP DATABASE IF EXISTS gopark_db;
CREATE DATABASE gopark_db;
USE gopark_db;

-- Users table for system authentication
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    full_name VARCHAR(100),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Parking slots table
CREATE TABLE parking_slots (
    id INT PRIMARY KEY AUTO_INCREMENT,
    slot_code VARCHAR(10) UNIQUE NOT NULL,
    type ENUM('Car', 'Motorcycle') NOT NULL,
    status ENUM('AVAILABLE', 'HOLD', 'OCCUPIED') DEFAULT 'AVAILABLE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Vehicle entries table
CREATE TABLE vehicle_entries (
    id INT PRIMARY KEY AUTO_INCREMENT,
    slot_code VARCHAR(10) NOT NULL,
    plate_number VARCHAR(20) NOT NULL,
    vehicle_type ENUM('Car', 'Motorcycle') NOT NULL,
    entry_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    exit_time DATETIME NULL,
    total_fee DECIMAL(10,2) DEFAULT 0.00,
    FOREIGN KEY (slot_code) REFERENCES parking_slots(slot_code) ON DELETE CASCADE,
    INDEX idx_slot_code (slot_code),
    INDEX idx_entry_time (entry_time),
    INDEX idx_exit_time (exit_time)
);

-- Transactions table
CREATE TABLE transactions (
    id INT PRIMARY KEY AUTO_INCREMENT,
    plate_number VARCHAR(20),
    vehicle_type VARCHAR(50),
    slot_code VARCHAR(10),
    entry_time TIMESTAMP NULL,
    exit_time TIMESTAMP NULL,
    duration VARCHAR(50),
    fee DOUBLE DEFAULT 0,
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_plate_number (plate_number),
    INDEX idx_entry_time (entry_time),
    INDEX idx_status (status)
);

-- Parking rates configuration table
CREATE TABLE parking_rates (
    vehicle_type VARCHAR(20) PRIMARY KEY,
    first_3_hours DECIMAL(10,2) NOT NULL,
    succeeding_hour DECIMAL(10,2) NOT NULL,
    overnight_rate DECIMAL(10,2) NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert default parking rates
INSERT INTO parking_rates (vehicle_type, first_3_hours, succeeding_hour, overnight_rate) VALUES
('Motorcycle', 30.00, 10.00, 350.00),
('Car', 40.00, 15.00, 500.00);

-- Insert default admin user (password: admin123)
INSERT INTO users (full_name, username, password) VALUES
('Administrator', 'admin', 'admin123');

-- Insert parking slots matching your actual data (18 Car slots, 17 Motorcycle slots)
INSERT INTO parking_slots (slot_code, type, status) VALUES
-- Car slots (C1 to C18)
('C1', 'Car', 'AVAILABLE'),
('C2', 'Car', 'AVAILABLE'),
('C3', 'Car', 'AVAILABLE'),
('C4', 'Car', 'AVAILABLE'),
('C5', 'Car', 'AVAILABLE'),
('C6', 'Car', 'AVAILABLE'),
('C7', 'Car', 'AVAILABLE'),
('C8', 'Car', 'AVAILABLE'),
('C9', 'Car', 'AVAILABLE'),
('C10', 'Car', 'AVAILABLE'),
('C11', 'Car', 'AVAILABLE'),
('C12', 'Car', 'AVAILABLE'),
('C13', 'Car', 'AVAILABLE'),
('C14', 'Car', 'AVAILABLE'),
('C15', 'Car', 'AVAILABLE'),
('C16', 'Car', 'AVAILABLE'),
('C17', 'Car', 'AVAILABLE'),
('C18', 'Car', 'AVAILABLE'),

-- Motorcycle slots (M1 to M17)
('M1', 'Motorcycle', 'AVAILABLE'),
('M2', 'Motorcycle', 'AVAILABLE'),
('M3', 'Motorcycle', 'AVAILABLE'),
('M4', 'Motorcycle', 'AVAILABLE'),
('M5', 'Motorcycle', 'AVAILABLE'),
('M6', 'Motorcycle', 'AVAILABLE'),
('M7', 'Motorcycle', 'AVAILABLE'),
('M8', 'Motorcycle', 'AVAILABLE'),
('M9', 'Motorcycle', 'AVAILABLE'),
('M10', 'Motorcycle', 'AVAILABLE'),
('M11', 'Motorcycle', 'AVAILABLE'),
('M12', 'Motorcycle', 'AVAILABLE'),
('M13', 'Motorcycle', 'AVAILABLE'),
('M14', 'Motorcycle', 'AVAILABLE'),
('M15', 'Motorcycle', 'AVAILABLE'),
('M16', 'Motorcycle', 'AVAILABLE'),
('M17', 'Motorcycle', 'AVAILABLE');

-- Create views for reporting
CREATE VIEW revenue_today AS
SELECT COALESCE(SUM(fee), 0) AS today_revenue
FROM transactions
WHERE DATE(exit_time) = CURDATE() AND status = 'Paid';

CREATE VIEW revenue_this_month AS
SELECT COALESCE(SUM(fee), 0) AS month_revenue
FROM transactions
WHERE MONTH(exit_time) = MONTH(CURDATE())
  AND YEAR(exit_time) = YEAR(CURDATE())
  AND status = 'Paid';

CREATE VIEW monthly_revenue_report AS
SELECT
    DATE_FORMAT(exit_time, '%b') AS month,
    SUM(fee) AS total_revenue
FROM transactions
WHERE status = 'Paid'
GROUP BY YEAR(exit_time), MONTH(exit_time), DATE_FORMAT(exit_time, '%b')
ORDER BY YEAR(exit_time), MONTH(exit_time);

-- Show table structure verification
SHOW TABLES;

DESCRIBE users;
DESCRIBE parking_slots;
DESCRIBE vehicle_entries;
DESCRIBE transactions;
DESCRIBE parking_rates;