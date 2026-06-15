# Student Management System

Simple Spring Boot application with MySQL.

## Tech Stack
- Java 17
- Spring Boot
- MySQL
- Thymeleaf

## Features
- Home page with two options:
  - Add Student
  - Show Total Student

## Setup
1. Create database in MySQL:
   ```sql
   CREATE DATABASE student_db;
   ```
2. Set database credentials as environment variables (PowerShell):
   ```powershell
   $env:DB_USERNAME="root"
   $env:DB_PASSWORD="your_actual_mysql_password"
   ```
3. Run the project:
   ```bash
   mvn spring-boot:run
   ```
4. Open:
   http://localhost:8080
