package dev.HR_Mudol.Service.ManagerSystem;

import HR_Mudol.Service.ManagerSystem.ShiftManager;
import HR_Mudol.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class ShiftManagerTest {

    private ShiftManager shiftManager;
    private Shift shift;
    private Employee employee;
    private User managerUser;
    private RoleManagerMock roleManagerMock;

    @BeforeEach
    public void setUp() {
        // מגדירים את סביבת הבדיקה
        roleManagerMock = new RoleManagerMock(); // Mock שלך
        shiftManager = new ShiftManager(roleManagerMock);

        shift = new Shift(WeekDay.MONDAY, ShiftType.MORNING); // שיפט לבדיקה
        employee = new Employee("John Doe", 123456789, "pass", "bank", 5000,
                java.time.LocalDate.now(), 5, 5, 10, 10);

        managerUser = new User(employee, Level.HRManager); // יוזר עם הרשאות מנהל
    }

    @Test
    public void testAssignEmployeeToShift_Success() {
        Role role = new Role("Cashier"); // יוצרים תפקיד דמה

        shiftManager.assignEmployeeToShift(managerUser, shift, employee, role);

        assertTrue(shift.getEmployees().contains(employee));
    }

    @Test
    public void testAssignEmployeeToShift_AccessDenied() {
        User regularUser = new User(employee, Level.regularEmp);
        Role role = new Role("Cashier");

        assertThrows(SecurityException.class, () -> {
            shiftManager.assignEmployeeToShift(regularUser, shift, employee, role);
        });
    }

    @Test
    public void testRemoveEmployeeFromShift_Success() {
        shift.addEmployee(managerUser, employee, new Role("Cashier"));

        String input = "1\n"; // נבחר את העובד הראשון ברשימה
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in); // ננתב את הקלט

        shiftManager.removeEmployeeFromShift(managerUser, shift);

        assertFalse(shift.getEmployees().contains(employee));
    }

    @Test
    public void testRemoveRoleFromShift_Success() {
        Role cashier = new Role("Cashier");
        shift.addNecessaryRoles(managerUser, cashier);

        // מעבירים קלט תקין: מזהה של Cashier ואז "exit" כדי לצאת
        String input = cashier.getRoleNumber() + "\n"; // זה מחליף את "8"
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        shiftManager.removeRoleFromShift(managerUser, shift);

        assertFalse(shift.getNecessaryRoles().contains(cashier));
    }


    @Test
    public void testChooseRelevantRoleForShift_Success() {
        Role cashier = new Role("Cashier");
        roleManagerMock.addMockRole(cashier); // הוספת תפקיד למוק

        String input = cashier.getRoleNumber() + "\nD\n"; // בוחר את התפקיד ואז יוצא
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        shiftManager.chooseRelevantRoleForShift(managerUser, shift);

        assertFalse(shift.getNecessaryRoles().isEmpty());
    }


    @Test
    public void testPrintShift_Success() {
        assertDoesNotThrow(() -> shiftManager.printShift(managerUser, shift));
    }
}
