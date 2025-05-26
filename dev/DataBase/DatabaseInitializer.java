package DataBase;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    private static final String SCHEMA_SQL_FILE = "projectData.sql";// path to your .sql file

    public static void main(String[] args) {
        try {
            // Read the entire SQL file as a string
            String sql = new String(Files.readAllBytes(Paths.get(SCHEMA_SQL_FILE)));

            // ✅ Load PostgreSQL driver (required in non-Maven setups)
            Class.forName("org.postgresql.Driver");

            // Connect to PostgreSQL
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                // Split SQL into statements using semicolon
                for (String command : sql.split(";")) {
                    command = command.trim();
                    if (!command.isEmpty()) {
                        try {
                            stmt.execute(command + ";");
                        } catch (Exception e) {
                            System.err.println("Skipping command:\n" + command + "\nCause: " + e.getMessage());
                        }
                    }
                }

                System.out.println("✅ Schema and tables applied successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Failed to initialize database.");
        }
    }
}
