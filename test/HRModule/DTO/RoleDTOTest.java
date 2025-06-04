package HRModule.DTO;

import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.RoleDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoleDTOTest {

    @Test
    public void testConstructorWithAllFields() {
        List<EmployeeDTO> employees = new ArrayList<>();
        employees.add(new EmployeeDTO(100000001, "Alice", "pass", "IL001", 5000,
                LocalDate.of(2024, 1, 1), 2, 2, 5, 3));

        RoleDTO role = new RoleDTO(1, "Cashier", employees);

        assertEquals(1, role.getRoleNumber());
        assertEquals("Cashier", role.getDescription());
        assertEquals(1, role.getRelevantEmployees().size());
        assertEquals("Alice", role.getRelevantEmployees().get(0).getFullName());
    }

    @Test
    public void testConstructorWithDescriptionOnly() {
        RoleDTO role = new RoleDTO("Stocker");

        assertEquals("Stocker", role.getDescription());
        assertEquals(0, role.getRoleNumber()); // ברירת מחדל
        assertNull(role.getRelevantEmployees()); // עדיין לא הוגדר
    }

    @Test
    public void testSettersAndGetters() {
        RoleDTO role = new RoleDTO("Manager");

        role.setRoleNumber(99);
        assertEquals(99, role.getRoleNumber());

        List<EmployeeDTO> list = new ArrayList<>();
        role.setRelevantEmployees(list);
        assertSame(list, role.getRelevantEmployees());
    }

    @Test
    public void testEmptyEmployeeList() {
        RoleDTO role = new RoleDTO(10, "Cleaner", new ArrayList<>());

        assertNotNull(role.getRelevantEmployees());
        assertEquals(0, role.getRelevantEmployees().size());
    }

    @Test
    public void testNullSafeDescription() {
        RoleDTO role = new RoleDTO(null);
        assertNull(role.getDescription());
    }
}
