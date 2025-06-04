package HRModule.integration;

import HR_Mudol.DAO.EmployeeDAOImpl;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DataBase.PostgresConnection;
import HR_Mudol.DataBase.DatabaseInitializer;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeFlowTest {

    private static Connection conn;
    private static EmployeeDAOImpl employeeDAO;
    private static final int TEST_EMP_ID = 9998;

    @BeforeAll
    public static void setup() throws Exception {
        // יטען את test_schema.sql ויבנה את בסיס הנתונים לטסטים
        DatabaseInitializer.initializeForTests();
        conn = PostgresConnection.getConnection();
        employeeDAO = new EmployeeDAOImpl();
    }


    @Test
    @Order(1)
    public void testInsertEmployee() {
        EmployeeDTO emp = new EmployeeDTO(TEST_EMP_ID, "Test User", "password", "IL00", 5500,
                LocalDate.of(2023, 1, 1), 2, 2, 10, 5);

        employeeDAO.insert(emp, 101);

        EmployeeDTO fromDb = employeeDAO.getById(TEST_EMP_ID);
        assertNotNull(fromDb);
        assertEquals("Test User", fromDb.getFullName());
        assertEquals(5500, fromDb.getSalary());
    }

    @Test
    @Order(2)
    public void testUpdateBankAccount() throws SQLException {
        employeeDAO.updateBankAccount(TEST_EMP_ID, "IL99");
        EmployeeDTO updated = employeeDAO.getById(TEST_EMP_ID);
        assertEquals("IL99", updated.getBankAccount());
    }

    @Test
    @Order(3)
    public void testExists() {
        assertTrue(employeeDAO.exists(TEST_EMP_ID));
    }

    @Test
    @Order(4)
    public void testArchiveEmployee() throws SQLException {
        // ניקוי מראש מארכיון ומ־users
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE userID = ?")) {
            stmt.setInt(1, TEST_EMP_ID);
            stmt.executeUpdate();
        }

        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM archived_employees WHERE empID = ?")) {
            stmt.setInt(1, TEST_EMP_ID);
            stmt.executeUpdate();
        }

        employeeDAO.archive(TEST_EMP_ID);

        assertNull(employeeDAO.getById(TEST_EMP_ID), "Employee should no longer exist in active Employees table");

        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM archived_employees WHERE empID = ?")) {
            stmt.setInt(1, TEST_EMP_ID);
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next(), "Employee should exist in archive");
            }
        }
    }

    @AfterAll
    public static void tearDown() throws Exception {
        PostgresConnection.closeConnection();
    }
}
