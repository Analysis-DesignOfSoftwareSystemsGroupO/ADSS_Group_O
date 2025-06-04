package HR_Mudol.DataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresConnection {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Sansa1234";

    private static Connection connection; // ← מחזיק את החיבור היחיד

    static {
        try {
            Class.forName("org.postgresql.Driver");
            System.out.println("✅ PostgreSQL JDBC Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ PostgreSQL JDBC Driver not found in classpath.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                System.out.println("✅ Connected to PostgreSQL.");
            } catch (SQLException e) {
                System.err.println("❌ Failed to connect to database: " + DB_URL);
                throw e;
            }
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✅ PostgreSQL connection closed.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to close PostgreSQL connection.");
            e.printStackTrace();
        }
    }
}
