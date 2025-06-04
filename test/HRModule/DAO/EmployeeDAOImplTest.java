package HRModule.DAO;

import HR_Mudol.DAO.EmployeeDAOImpl;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DataBase.DatabaseInitializer;
import HR_Mudol.DataBase.PostgresConnection;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeDAOImplTest {

    private static Connection conn;
    private static EmployeeDAOImpl dao;

    @BeforeAll
    public static void setUpClass() throws Exception {
        // מריץ את הסכמה לטסטים
        DatabaseInitializer.initializeForTests();
        conn = PostgresConnection.getConnection();
        dao = new EmployeeDAOImpl();
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
        PostgresConnection.closeConnection();
    }

    @Test
    @Order(1)
    public void testInsertAndGetById() {
        EmployeeDTO emp = new EmployeeDTO(
                400000001, "Alice", "pass", "IL58", 5000,
                LocalDate.of(2024, 1, 1), 2, 2, 10, 5
        );

        dao.insert(emp, 101);
        EmployeeDTO fromDb = dao.getById(400000001);

        assertNotNull(fromDb);
        assertEquals("Alice", fromDb.getFullName());
        assertEquals(5000, fromDb.getSalary());
    }

    @Test
    @Order(2)
    public void testUpdateAndBankAccount() throws SQLException {
        EmployeeDTO emp = new EmployeeDTO(
                400000002, "Bob", "pass", "IL00", 4000,
                LocalDate.of(2023, 6, 1), 1, 1, 5, 2
        );

        dao.insert(emp, 101);
        dao.updateBankAccount(400000002, "NEW-IL00");

        EmployeeDTO updated = dao.getById(400000002);
        assertNotNull(updated);  // הוספה חשובה
        assertEquals("NEW-IL00", updated.getBankAccount());
    }


    @Test
    @Order(3)
    public void testExists() {
        EmployeeDTO emp = new EmployeeDTO(
                400000003, "Dana", "123", "ACCT", 6000,
                LocalDate.now(), 2, 2, 8, 4
        );

        dao.insert(emp, 101);
        assertTrue(dao.exists(400000003));
        assertFalse(dao.exists(888888888));
    }

    @Test
    @Order(4)
    public void testArchiveAndDelete() throws SQLException {
        EmployeeDTO emp = new EmployeeDTO(
                400000004, "Eli", "123", "BANK", 7000,
                LocalDate.now(), 1, 1, 6, 2
        );

        dao.insert(emp, 101);  // במקום 1 -> 101 כמו ב-sql

        // מנקה מראש אם קיים בארכיון
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM Archived_Employees WHERE empID = ?")) {
            stmt.setLong(1, 400000004);  // סוג הנתון המדויק
            stmt.executeUpdate();
        }

        dao.archive(400000004);

        assertNull(dao.getById(400000004));

        try (PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Archived_Employees WHERE empID = ?")) {
            stmt.setLong(1, 400000004);
            try (ResultSet rs = stmt.executeQuery()) {
                assertTrue(rs.next());
            }
        }
    }

}
