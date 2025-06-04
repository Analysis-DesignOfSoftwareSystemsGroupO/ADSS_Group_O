package HRModule.domain;

import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class RoleTest {

    private Role role;
    private Employee emp1;
    private Employee emp2;

    @BeforeEach
    public void setUp() {
        role = new Role(1, "Cashier");

        emp1 = new Employee("Alice", 100000001L, "pass1", "IL01", 5000,
                LocalDate.of(2022, 1, 1), 3, 2, 10, 5);

        emp2 = new Employee("Bob", 100000002L, "pass2", "IL02", 6000,
                LocalDate.of(2023, 5, 15), 4, 2, 12, 6);
    }

    @Test
    public void testAddEmployee() {
        role.addNewEmployee(emp1);
        assertTrue(role.getRelevantEmployees().contains(emp1));
    }

    @Test
    public void testRemoveEmployee() {
        role.addNewEmployee(emp1);
        role.removeEmployee(emp1);
        assertFalse(role.getRelevantEmployees().contains(emp1));
    }

    @Test
    public void testSetAndGetDescription() {
        role.setDescription("Updated");
        assertEquals("Updated", role.getDescription());
    }

    @Test
    public void testToStringWithNoEmployees() {
        Role emptyRole = new Role(2, "Manager");
        String output = emptyRole.toString();
        assertTrue(output.contains("No employees assigned"));
    }

    @Test
    public void testToStringWithEmployees() {
        role.addNewEmployee(emp1);
        role.addNewEmployee(emp2);
        String output = role.toString();
        assertTrue(output.contains("Alice"));
        assertTrue(output.contains("Bob"));
    }

    @Test
    public void testEquals() {
        Role sameDescRole = new Role("Cashier");
        Role differentRole = new Role("Manager");
        assertEquals(role, sameDescRole);
        assertNotEquals(role, differentRole);
    }
}
