package DataLayer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataBaseConnectionTest {
    @Test
    public void testConnection() throws Exception{
        DataBase.getConnection();
    }
}