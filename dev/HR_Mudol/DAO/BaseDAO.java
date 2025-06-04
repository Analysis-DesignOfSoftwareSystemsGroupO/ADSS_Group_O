package HR_Mudol.DAO;

import HR_Mudol.DataBase.PostgresConnection;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO implements AutoCloseable {
    protected Connection conn;

    public BaseDAO() throws SQLException {
        this.conn = PostgresConnection.getConnection();
    }

    @Override
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();  // סוגר את החיבור
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Failed to close DB connection: " + e.getMessage());
        }
    }
}
