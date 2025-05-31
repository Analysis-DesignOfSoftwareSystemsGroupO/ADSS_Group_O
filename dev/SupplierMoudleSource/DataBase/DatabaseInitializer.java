package SupplierMoudleSource.DataBase;

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

    private static final String SCHEMA_SQL_FILE = "dev/SupplierMoudleSource/DataBase/projectData.sql";// path to your .sql file
    private static final String DROP_DATA_SQL_FILE = "dev/SupplierMoudleSource/DataBase/dropTables.sql";
    public static void createSupplierTables() {
        try {
            // Read the entire SQL file as a string
            String sql = new String(Files.readAllBytes(Paths.get(SCHEMA_SQL_FILE)));

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
    public static void dropAllTables() {
        try {
            String sql = new String(Files.readAllBytes(Paths.get(DROP_DATA_SQL_FILE)));

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("✅ All tables dropped successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Failed to drop tables.");
        }
    }catch (Exception e){
            e.printStackTrace();
            System.err.println("❌ Failed to drop tables.");
        }
    }

}
