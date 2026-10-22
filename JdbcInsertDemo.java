import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcInsertDemo {

    // Database connection parameters (Adjust according to your DB setup)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/student_db";
    private static final String USER = "root";
    private static final String PASSWORD = "your_password";

    public static void main(String[] args) {
        // SQL query to create a table if it does not already exist
        String createTableSQL = "CREATE TABLE IF NOT EXISTS students ("
                + "id INT PRIMARY KEY AUTO_INCREMENT, "
                + "name VARCHAR(100) NOT NULL, "
                + "email VARCHAR(100) UNIQUE NOT NULL, "
                + "department VARCHAR(50)"
                + ")";

        // SQL parameterized query for insertion (prevents SQL injection)
        String insertSQL = "INSERT INTO students (name, email, department) VALUES (?, ?, ?)";

        // 1. (Optional for JDBC 4.0+) Explicitly load the MySQL Driver class
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Please add the connector JAR to your classpath.");
            e.printStackTrace();
            return;
        }

        // 2. Establish connection and execute statements using try-with-resources
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = connection.createStatement();
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

            System.out.println("Connected to the database successfully!");

            // Create table if not present
            stmt.executeUpdate(createTableSQL);

            // 3. Set parameter values for the PreparedStatement
            pstmt.setString(1, "Alice Johnson");
            pstmt.setString(2, "alice.johnson@example.com");
            pstmt.setString(3, "Computer Science");

            // 4. Execute the insert statement
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Record inserted successfully! Rows affected: " + rowsAffected);
            }

            // Inserting a second record
            pstmt.setString(1, "Bob Smith");
            pstmt.setString(2, "bob.smith@example.com");
            pstmt.setString(3, "Information Technology");

            rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Second record inserted successfully! Rows affected: " + rowsAffected);
            }

        } catch (SQLException e) {
            System.err.println("Database operation failed!");
            e.printStackTrace();
        }
    }
}
