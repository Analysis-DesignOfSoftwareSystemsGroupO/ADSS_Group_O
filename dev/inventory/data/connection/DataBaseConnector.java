package inventory.data.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static SupplierMoudleSource.DataBase.Config.Db_URL;
import static SupplierMoudleSource.DataBase.Config.Password;

public class DataBaseConnector {
    private static final String DB_URL = Db_URL;
    private static final String USER = "postgres";
    private static final String PASSWORD = Password;

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

}
