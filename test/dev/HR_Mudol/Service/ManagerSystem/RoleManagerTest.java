package dev.HR_Mudol.Service.ManagerSystem;

import HR_Mudol.Service.ManagerSystem.EmployeeManager;
import HR_Mudol.Service.ManagerSystem.RoleManager;
import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class RoleManagerTest {

    private Branch branch;
    private RoleManager roleManager;
    private User hrUser;

    @BeforeEach
    public void setUp() {
        // Initialize branch and roleManager
        branch = new Branch();
        roleManager = new RoleManager(branch);

        // Find existing HR manager user
        hrUser = branch.getUsers().stream()
                .filter(u -> u.getLevel() == Level.HRManager)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("HRManager user not found"));

        // Inject a fake EmployeeManager using branch data directly
        roleManager.setEmployeeManager(new EmployeeManager(branch) {
            @Override
            public Employee getEmployeeById(User caller, int id) {
                return branch.getEmployees().stream()
                        .filter(e -> e.getEmpId() == id)
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Employee not found"));
            }

            @Override
            public List<User> getAllUsers(User caller) {
                return branch.getUsers();
            }

            @Override
            public void addEmployee(User caller) {
                // Not required for current tests
            }
        });
    }

    @Test
    // Test creating a new role successfully
    public void testCreateRole_Success() {
        String input = "Cashier\n";
        roleManager.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        roleManager.createRole(hrUser);

        assertEquals(2, branch.getRoles().size()); // Shift Manager + Cashier
        assertEquals("Cashier", branch.getRoles().get(1).getDescription());
    }

    @Test
    // Test creating a role with a duplicate name
    public void testCreateRole_DuplicateRole() {
        branch.getRoles().add(new Role("Cashier"));

        String input = "Cashier\n";
        roleManager.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        roleManager.createRole(hrUser);

        assertEquals(2, branch.getRoles().size()); // Only Shift Manager + one Cashier
    }

    @Test
    // Test creating a role with an empty description
    public void testCreateRole_EmptyDescription() {
        String input = "\n";
        roleManager.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        roleManager.createRole(hrUser);

        assertEquals(1, branch.getRoles().size()); // Only Shift Manager should exist
    }

    @Test
    // Test creating a role with whitespace input
    public void testCreateRole_WhitespaceInput() {
        String input = "   \n"; // Only spaces
        roleManager.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        roleManager.createRole(hrUser);

        assertEquals(1, branch.getRoles().size()); // Only Shift Manager should exist
    }

    @Test
    // Test updating an existing role's description successfully
    public void testUpdateRoleDescription_Success() {
        Role role = new Role("Old Description");
        branch.getRoles().add(role);

        String input = role.getRoleNumber() + "\nNew Description\n";
        roleManager.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        roleManager.updateRoleDescription(hrUser);

        assertEquals("New Description", role.getDescription());
    }

    @Test
    // Test assigning an existing employee to "Shift Manager" role successfully
    public void testAssignEmployeeToShiftManager_Success() {
        Employee employee = branch.getEmployees().stream()
                .filter(e -> e.getEmpId() == 123456789)
                .findFirst()
                .orElseThrow();

        String input = "123456789\n";
        roleManager.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        roleManager.assignEmployeeToShiftManager(hrUser);

        Role shiftManagerRole = branch.getRoles().stream()
                .filter(r -> r.getDescription().equalsIgnoreCase("Shift Manager"))
                .findFirst()
                .orElseThrow();

        assertTrue(shiftManagerRole.getRelevantEmployees(hrUser).contains(employee));
    }

    @Test
    // Test assigning an employee to Shift Manager role with invalid ID
    public void testAssignEmployeeToShiftManager_InvalidId() {
        // Test invalid input when assigning to Shift Manager (no valid ID entered)
        String invalidInput = "invalid\n"; // not a number
        roleManager.setScanner(new Scanner(new ByteArrayInputStream(invalidInput.getBytes())));

        assertThrows(IllegalArgumentException.class, () -> {
            roleManager.assignEmployeeToShiftManager(hrUser);
        });
    }


    @Test
    // Test retrieving all roles
    public void testGetAllRoles_Success() {
        List<Role> roles = roleManager.getAllRoles(hrUser);
        assertEquals(1, roles.size()); // Only Shift Manager initially
    }

    @Test
    // Test getting a role by valid role number
    public void testGetRoleByNumber_Success() {
        Role shiftManager = branch.getRoles().stream()
                .filter(r -> r.getDescription().equalsIgnoreCase("Shift Manager"))
                .findFirst()
                .orElseThrow();

        int roleNum = shiftManager.getRoleNumber();
        Role found = roleManager.getRoleByNumber(roleNum);

        assertNotNull(found);
        assertEquals("Shift Manager", found.getDescription());
    }

    @Test
    // Test getting a role by an invalid role number (should return null)
    public void testGetRoleByNumber_InvalidNumber() {
        Role found = roleManager.getRoleByNumber(9999); // Non-existing role
        assertNull(found);
    }
}
