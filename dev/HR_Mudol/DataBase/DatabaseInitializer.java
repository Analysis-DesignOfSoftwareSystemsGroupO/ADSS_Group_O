package HR_Mudol.DataBase;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Scanner;

public class DatabaseInitializer {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Sansa1234";

    private static final String SCHEMA_ONLY_FILE = "schema_only.sql";           // רק טבלאות
    private static final String SCHEMA_WITH_DATA_FILE = "schema_with_data.sql"; // טבלאות + דאטה

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Database Initialization ===");
        System.out.println("1. Create schema and load sample data");
        System.out.println("2. Create schema only (empty tables)");
        System.out.print("Choose option [1/2]: ");
        String choice = scanner.nextLine().trim();

        String sqlFile;
        if (choice.equals("1")) {
            sqlFile = SCHEMA_WITH_DATA_FILE;
        } else if (choice.equals("2")) {
            sqlFile = SCHEMA_ONLY_FILE;
        } else {
            System.out.println("❌ Invalid choice. Exiting.");
            return;
        }

        try {
            // Read the entire SQL file
            String sql = new String(Files.readAllBytes(Paths.get(sqlFile)));

            // ✅ Load PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                for (String command : sql.split(";")) {
                    command = command.trim();
                    if (!command.isEmpty()) {
                        try {
                            stmt.execute(command + ";");
                        } catch (Exception e) {
                            System.err.println("⚠ Skipping command:\n" + command + "\nCause: " + e.getMessage());
                        }
                    }
                }

                System.out.println("✅ Database initialized successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Failed to initialize database.");
        }
    }
}
