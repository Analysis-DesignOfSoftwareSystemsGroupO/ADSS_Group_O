package DataLayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;

public class DataBase {
    private static final Logger log = LogManager.getLogger(DataBase.class);
    private static final String DB_URL = "jdbc:log4jdbc:postgresql://10.100.102.28:5432/postgres?user=postgres&password=1234&connectTimeout=10&sslmode=prefer";
    private static Connection conn;

    static {
        try{
            Class.forName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy"); // initialize logger query
            conn = DriverManager.getConnection(DB_URL);
            log.info("Connecting to SQL Server... ");
            log.info("Connected to SQL {}", DB_URL);
        } catch (ClassNotFoundException e) {
            log.error("ClassNotFoundError, connection Failed");
            throw new RuntimeException(e);
        } catch (SQLException e) {
            log.error("Connection Failed with SQLexception ");
            throw new RuntimeException(e);
        }
    }

    private DataBase() {}

    public static Connection getConnection() throws SQLException {
        return conn;
    }


}
