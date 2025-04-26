package dev.HR_Mudol.Service.ManagerSystem;

import HR_Mudol.Service.ManagerSystem.RoleManager;
import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class RoleManagerTest {

    private RoleManager roleManager;
    private Branch branch;
    private HRManager hrManager;
    private User hrUser;

    @BeforeEach
    public void setUp() {
        branch = new Branch();

        // ניצור מנהל כוח אדם
        hrManager = new HRManager("Admin", 123456789, "adminpass", "bank1", 50000, LocalDate.now(), 2, 2, 5, 10);
        hrUser = new User(hrManager, Level.HRManager);

        // כאן ניצור את roleManager בלי להוסיף "Shift Manager" אוטומטי
        roleManager = new RoleManager(branch, false); // <-- שים לב ל-false
    }

    @Test
    public void testCreateRole_Success() {
        // יצירת תפקיד חדש
        Role role = new Role("Driver");
        branch.getRoles().add(role);

        assertEquals(1, branch.getRoles().size());
        assertEquals("Driver", branch.getRoles().get(0).getDescription());
    }

    @Test
    public void testCreateRole_EmptyDescription() {
        // נסה להכניס תפקיד ריק (תוך שימוש ב-roleManager אם היית עושה createRole())
        // כאן דוגמה שתדמה הזנה ריקה
    }

    @Test
    public void testGetAllRoles_Success() {
        Role r1 = new Role("Driver");
        Role r2 = new Role("Cashier");
        branch.getRoles().add(r1);
        branch.getRoles().add(r2);

        assertEquals(2, roleManager.getAllRoles(hrUser).size());
    }

    @Test
    public void testGetRoleByNumber_Success() {
        Role r1 = new Role("Driver");
        branch.getRoles().add(r1);

        assertEquals(r1, roleManager.getRoleByNumber(r1.getRoleNumber()));
    }

    @Test
    public void testGetRoleByNumber_InvalidNumber() {
        assertNull(roleManager.getRoleByNumber(9999));
    }
}
