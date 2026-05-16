package util;

import java.sql.*;

public class DatabaseHandler {
    private static final String URL = "jdbc:sqlite:hardwarehustle.db";

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        createTables(conn); 
        return conn;
    }

    private static void createTables(Connection conn) {
        String usersTable = "CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT);";
        String questionTable = "CREATE TABLE IF NOT EXISTS questions (id INTEGER PRIMARY KEY AUTOINCREMENT, question TEXT, optionA TEXT, optionB TEXT, optionC TEXT, optionD TEXT, correctAnswer TEXT);";
        String leaderboardTable = "CREATE TABLE IF NOT EXISTS leaderboard (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, score INTEGER);"; 
        
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(usersTable);
            stmt.execute(questionTable);
            stmt.execute(leaderboardTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void seedQuestions() {
        String sql = "INSERT INTO questions (question, optionA, optionB, optionC, optionD, correctAnswer) VALUES (?, ?, ?, ?, ?, ?)";
        
        // The 15 Hardware Questions
        String[][] data = {
            {"What does CPU stand for?", "Central Process Unit", "Central Processing Unit", "Computer Personal Unit", "Control Process Unit", "B"},
            {"Which component is the 'brain' of the computer?", "RAM", "GPU", "CPU", "Hard Drive", "C"},
            {"What type of memory is volatile?", "SSD", "ROM", "HDD", "RAM", "D"},
            {"Which part is responsible for rendering graphics?", "Sound Card", "GPU", "NIC", "PSU", "B"},
            {"What does RAM stand for?", "Read Access Memory", "Real Access Memory", "Random Access Memory", "Rapid Access Memory", "C"},
            {"Which of these is a permanent storage device?", "RAM", "SSD", "CPU", "Cache", "B"},
            {"What is the main circuit board called?", "Fatherboard", "Dashboard", "Motherboard", "Systemboard", "C"},
            {"Which component supplies power?", "CPU", "PSU", "UPS", "Battery", "B"},
            {"What does 'BIOS' stand for?", "Binary Input Output System", "Basic Input Output System", "Basic Integrated OS", "Board Input Output System", "B"},
            {"Which port is used for mouse/keyboard?", "HDMI", "VGA", "USB", "Ethernet", "C"},
            {"What is the physical part of the computer called?", "Software", "Firmware", "Hardware", "Malware", "C"},
            {"1,024 Megabytes is equal to what?", "1 Terabyte", "1 Gigabyte", "1 Petabyte", "1 Kilobyte", "B"},
            {"What handles the cooling of the CPU?", "Power Supply", "Case Fan", "Heat Sink/Fan", "CMOS", "C"},
            {"Which device is used to enter text?", "Monitor", "Mouse", "Keyboard", "Printer", "C"},
            {"What does SSD stand for?", "Super Speed Drive", "Solid State Drive", "System Storage Device", "Static State Drive", "B"}
        };

        try (Connection conn = getConnection()) {
            // Only seed if the table is empty
            Statement checkStmt = conn.createStatement();
            ResultSet rs = checkStmt.executeQuery("SELECT COUNT(*) FROM questions");
            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    for (String[] q : data) {
                        pstmt.setString(1, q[0]);
                        pstmt.setString(2, q[1]);
                        pstmt.setString(3, q[2]);
                        pstmt.setString(4, q[3]);
                        pstmt.setString(5, q[4]);
                        pstmt.setString(6, q[5]);
                        pstmt.executeUpdate();
                    }
                    System.out.println("Hardware Hustle: 15 Questions loaded!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}