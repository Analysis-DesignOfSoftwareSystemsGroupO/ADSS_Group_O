package dev.HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.RoleController;
import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RoleManager functionality.
 */
public class RoleControllerTest {

    private TestBranch branch;
    private RoleController roleController;
    private User hrUser;

    @BeforeEach
    public void setUp() {
        branch = new TestBranch();
        roleController = new RoleController(branch);
        branch.getRoles().clear(); // Important: clear default "Shift Manager" role after creating RoleManager

        // Create HR manager for permissions
        HRManager hrManager = new HRManager("Test HR", 111111111, "admin", "bank", 10000,
                java.time.LocalDate.now(), 2, 2, 5, 5);
        hrUser = new User(hrManager, Level.HRManager);
        branch.getEmployees().add(hrManager);
        branch.getUsers().add(hrUser);
    }

    /**
     * Test creating a role successfully.
     */
    @Test
    public void testCreateRole_Success() {
        String input = "Cook\n";
        roleController.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        roleController.createRole(hrUser);

        assertEquals(1, branch.getRoles().size());
        assertEquals("Cook", branch.getRoles().get(0).getDescription());
    }

    /**
     * Test creating a role with a duplicate name.
     */
    @Test
    public void testCreateRole_DuplicateRole() {
        branch.getRoles().add(new Role("Cook"));

        String input = "Cook\n";
        roleController.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        roleController.createRole(hrUser);

        assertEquals(1, branch.getRoles().size());
    }

    /**
     * Test creating a role with whitespace input.
     */
    @Test
    public void testCreateRole_WhitespaceInput() {
        String input = "   \n";
        roleController.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        roleController.createRole(hrUser);

        assertEquals(0, branch.getRoles().size());
    }

    /**
     * Test creating a role with empty description.
     */
    @Test
    public void testCreateRole_EmptyDescription() {
        String input = "\n";
        roleController.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        roleController.createRole(hrUser);

        assertEquals(0, branch.getRoles().size());
    }

    /**
     * Test retrieving all roles.
     */
    @Test
    public void testGetAllRoles_Success() {
        branch.getRoles().add(new Role("Cook"));

        assertEquals(1, roleController.getAllRoles(hrUser).size());
    }

    /**
     * Test updating the description of an existing role successfully.
     */
    @Test
    public void testUpdateRoleDescription_Success() {
        Role cookRole = new Role("Cook");
        branch.getRoles().add(cookRole);

        String input = cookRole.getRoleNumber() + "\nNewCook\n";
        roleController.setScanner(new Scanner(new ByteArrayInputStream(input.getBytes())));

        roleController.updateRoleDescription(hrUser);

        assertEquals("NewCook", cookRole.getDescription());
    }
}
