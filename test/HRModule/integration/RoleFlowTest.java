package HRModule.integration;

import HR_Mudol.DAO.EmployeeDAOImpl;
import HR_Mudol.DAO.RoleDAOImpl;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.RoleDTO;
import HR_Mudol.DataBase.DatabaseInitializer;
import HR_Mudol.DataBase.PostgresConnection;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoleFlowTest {

    private static Connection conn;
    private static RoleDAOImpl roleDAO;
    private static EmployeeDAOImpl employeeDAO;

    private static final long EMP_ID = 888888888;
    private static final RoleDTO role = new RoleDTO("Inventory Clerk");

    @BeforeAll
    public static void setup() throws Exception {
        DatabaseInitializer.initializeForTests();
        conn = PostgresConnection.getConnection();
        roleDAO = new RoleDAOImpl();
        employeeDAO = new EmployeeDAOImpl();

        EmployeeDTO emp = new EmployeeDTO(EMP_ID, "Role Tester", "pwd", "ACCT-1", 4000,
                LocalDate.of(2023, 3, 1), 1, 1, 8, 4);
        employeeDAO.insert(emp, 101);
    }

    @Test
    @Order(1)
    public void testInsertRole() throws SQLException {
        roleDAO.insert(role);
        assertTrue(role.getRoleNumber() > 0, "Role number must be set after insert");
    }

    @Test
    @Order(2)
    public void testAssignEmployeeToRole() {
        roleDAO.assignEmployeeToRole(EMP_ID, role.getRoleNumber());

        RoleDTO fromDb = roleDAO.getByNumber(role.getRoleNumber());
        assertNotNull(fromDb);
        assertTrue(fromDb.getRelevantEmployees()
                .stream()
                .anyMatch(e -> e.getEmployeeId() == EMP_ID), "Employee should be assigned to role");
    }

    @Test
    @Order(3)
    public void testRemoveEmployeeFromRole() {
        roleDAO.removeEmployeeFromRole(EMP_ID, role.getRoleNumber());

        RoleDTO fromDb = roleDAO.getByNumber(role.getRoleNumber());
        assertNotNull(fromDb);
        assertFalse(fromDb.getRelevantEmployees()
                .stream()
                .anyMatch(e -> e.getEmployeeId() == EMP_ID), "Employee should no longer be assigned");
    }

    @Test
    @Order(4)
    public void testDeleteRole() throws SQLException {
        roleDAO.delete(role.getRoleNumber());
        assertNull(roleDAO.getByNumber(role.getRoleNumber()), "Role should be deleted from DB");
    }

    @AfterAll
    public static void tearDown() throws Exception {
        if (conn != null && !conn.isClosed()) {
            // הסרה מהארכיון אם נדרש
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM archived_employees WHERE empID = ?")) {
                stmt.setLong(1, EMP_ID);
                stmt.executeUpdate();
            }
            conn.close();
        }
        PostgresConnection.closeConnection();
    }
}
