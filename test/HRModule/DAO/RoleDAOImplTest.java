package HRModule.DAO;

import HR_Mudol.DAO.EmployeeDAOImpl;
import HR_Mudol.DAO.RoleDAOImpl;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.RoleDTO;
import HR_Mudol.DataBase.DatabaseInitializer;
import org.junit.jupiter.api.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoleDAOImplTest {

    private static RoleDAOImpl dao;
    private static EmployeeDAOImpl employeeDAO;
    private static final RoleDTO cashierRole = new RoleDTO("Cashier");
    private static final long employeeId = 100000001;

    @BeforeAll
    public static void setup() throws SQLException {
        DatabaseInitializer.initializeForTests();
        dao = new RoleDAOImpl();
        employeeDAO = new EmployeeDAOImpl();

        // ודא שהעובד קיים לפני שיוך לתפקיד
        EmployeeDTO testEmployee = new EmployeeDTO(
                employeeId,
                "Test Employee",
                "testpass",
                "IL123456789",
                5500,
                LocalDate.of(2024, 1, 1),
                2, 2, 5, 3
        );
        dao.deleteByDescription("Cashier");
        employeeDAO.insert(testEmployee, 101); // ודא שסניף 101 קיים בבסיס הנתונים
    }

    @Test
    @Order(1)
    public void testInsertRole() throws SQLException {

        dao.insert(cashierRole);
        // עכשיו אמור להיווצר תפקיד חדש עם ID תקף
        assertTrue(cashierRole.getRoleNumber() > 0);
    }


    @Test
    @Order(2)
    public void testAssignEmployeeToRole() throws SQLException {
        // ודא שה־roleNumber מאוכלס
        if (cashierRole.getRoleNumber() == 0) {
            RoleDTO fromDb = dao.getByDescription("Cashier");
            assertNotNull(fromDb, "Cashier role not found in DB");
            cashierRole.setRoleNumber(fromDb.getRoleNumber());
        }

        dao.assignEmployeeToRole(employeeId, cashierRole.getRoleNumber());

        RoleDTO roleFromDb = dao.getByNumber(cashierRole.getRoleNumber());
        assertNotNull(roleFromDb);

        boolean contains = roleFromDb.getRelevantEmployees()
                .stream()
                .anyMatch(e -> e.getEmployeeId() == employeeId);
        assertTrue(contains);
    }


    @Test
    @Order(3)
    public void testRemoveEmployeeFromRole() {
        dao.removeEmployeeFromRole(employeeId, cashierRole.getRoleNumber());

        RoleDTO role = dao.getByNumber(cashierRole.getRoleNumber());
        assertNotNull(role);

        boolean exists = role.getRelevantEmployees()
                .stream()
                .anyMatch(e -> e.getEmployeeId() == employeeId);
        assertFalse(exists);
    }

    @Test
    @Order(4)
    public void testGetAllRoles() throws SQLException {
        List<RoleDTO> roles = dao.getAll();
        assertFalse(roles.isEmpty());
    }

    @Test
    @Order(5)
    public void testGetByDescription() throws SQLException {
        // נוודא שהתפקיד Lead Cashier קיים לפני השליפה
        dao.insert(new RoleDTO("Lead Cashier"));

        RoleDTO found = dao.getByDescription("Lead Cashier");
        assertNotNull(found);
        assertEquals("Lead Cashier", found.getDescription());
    }

    @Test
    @Order(6)
    public void testDeleteByDescription() throws SQLException {
        dao.deleteByDescription("Lead Cashier");
        assertNull(dao.getByDescription("Lead Cashier"));
    }

    @AfterAll
    public static void cleanup() throws SQLException {
        dao.delete(cashierRole.getRoleNumber());
        // אופציונלי: הסר גם את העובד
        // employeeDAO.delete(employeeId);
    }
}
