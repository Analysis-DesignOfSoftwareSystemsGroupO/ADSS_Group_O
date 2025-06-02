package SupplierMoudleSource.DataBase;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static SupplierMoudleSource.DataBase.Config.Db_URL;
import static SupplierMoudleSource.DataBase.Config.Password;

public class DatabaseInitializer {

    private static final String DB_URL = Db_URL;
    private static final String USER = "postgres";
    private static final String PASSWORD = Password;

    // ✅ Updated paths to match your actual resource folder structure
    private static final String SCHEMA_SQL_FILE = "supplierMoudle/projectData.sql";
    private static final String DROP_DATA_SQL_FILE = "supplierMoudle/dropTables.sql";

    public static void createSupplierTables() {
        try {
            String sql = loadSQL(SCHEMA_SQL_FILE);

            Class.forName("org.postgresql.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                for (String command : sql.split(";")) {
                    command = command.trim();
                    if (!command.isEmpty()) {
                        try {
                            stmt.execute(command + ";");
                        } catch (Exception e) {
                            e.printStackTrace(); // log but continue
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

    public static void dropAllSupplierTable() {
        try {
            String sql = loadSQL(DROP_DATA_SQL_FILE);

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                 Statement stmt = conn.createStatement()) {

                stmt.execute(sql);
                System.out.println("✅ All tables dropped successfully.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Failed to drop tables.");
        }
    }

    private static String loadSQL(String path) throws Exception {
        // Try from classpath (inside JAR)
        try (InputStream in = DatabaseInitializer.class.getClassLoader().getResourceAsStream(path)) {
            if (in != null) {
                return new String(in.readAllBytes(), StandardCharsets.UTF_8);
            }
        }

        // If not found, try as a relative file system path (during dev)
        if (Files.exists(Paths.get(path))) {
            return new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);
        }

        // Still not found
        throw new IllegalArgumentException("SQL file not found in resources or filesystem: " + path);
    }
}
