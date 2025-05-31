package HR_Mudol.DataBase;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Sansa1234";

    // טוענת ומריצה את הקובץ.
    private static void executeSQLFile(String filePath) {
        try {
            String sql = new String(Files.readAllBytes(Paths.get(filePath)));

            Class.forName("org.postgresql.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                for (String command : sql.split(";")) {
                    command = command.trim();
                    if (!command.isEmpty()) {
                        try {
                            stmt.execute(command + ";");
                        } catch (Exception e) {
                            System.err.println("⚠️ Skipping command:\n" + command + "\nCause: " + e.getMessage());
                        }
                    }
                }

                System.out.println("✅ SQL script executed successfully: " + filePath);
            }

        } catch (Exception e) {
            System.err.println("❌ Failed to execute SQL script: " + filePath);
            e.printStackTrace();
        }
    }

    // בוחרת את הקובץ לפי הפרמטר.
    public static void initialize(boolean withData) {
        String sqlFile = withData ? "schema_with_data.sql" : "schema_only.sql";
        executeSQLFile(sqlFile);
    }

    //רק בקריאה למחלקה
    public static void main(String[] args) {
        initialize(true); // או false
    }
}
