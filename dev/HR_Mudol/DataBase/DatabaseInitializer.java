    package HR_Mudol.DataBase;

    import java.io.FileNotFoundException;
    import java.io.InputStream;
    import java.nio.charset.StandardCharsets;
    import java.nio.file.Files;
    import java.nio.file.Paths;
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.Statement;

    public class DatabaseInitializer {

        private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
        private static final String USER = "postgres";
        private static final String PASSWORD = "Sansa1234";

        public static void initialize(boolean loadFromDatabase) {
            if (loadFromDatabase) {
                executeSQLFile("sql/schema_with_data.sql");
                executeSQLFile("sql/TRS_schema.sql");

            } else {
                System.out.println("⚠️ Initializing fresh system...");
                executeSQLFile("sql/schema_only.sql");
                executeSQLFile("sql/TRS_schema.sql");
            }
        }
        public static void initializeForTests() {
            System.out.println("🧪 Initializing test DB from test_schema.sql...");
            executeSQLFile("dev/sql/test_schema.sql");
        }
        private static void executeSQLFile(String resourcePath) {
            try (InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath)) {
                if (inputStream == null) {
                    throw new FileNotFoundException("Resource not found: " + resourcePath);
                }

                String sql = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

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

                    System.out.println("✅ SQL script executed successfully: " + resourcePath);
                }

            } catch (Exception e) {
                System.err.println("❌ Failed to execute SQL script: " + resourcePath);
                e.printStackTrace();
            }
        }

    }
