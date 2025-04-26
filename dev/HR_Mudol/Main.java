package HR_Mudol;

import HR_Mudol.domain.*;
import HR_Mudol.presentation.LoginScreen;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Create a new branch
        Branch curBranch = new Branch();

        // Create HR Manager
        Employee hrManager = new Employee(
                "Rami levi",
                111111111,
                "admin",
                "IL1234567890",
                15000,
                LocalDate.now(),
                3, 3, 10, 10
        );
        User hrManagerUser = new User(hrManager, Level.HRManager);
        curBranch.getEmployees().add(hrManager);
        curBranch.getUsers().add(hrManagerUser);

        // Create 9 regular employees
        for (int i = 0; i <= 15; i++) {
            Employee employee = new Employee(
                    "Employee" + (i + 1),
                    200000000 + i,
                    "pass" + (i + 1),
                    "IL987654321" + i,
                    8000 + i * 100,
                    LocalDate.now().minusDays(i * 10),
                    2, 2, 5, 5
            );
            User user = new User(employee, Level.regularEmp);
            curBranch.getEmployees().add(employee);
            curBranch.getUsers().add(user);
        }

        // Create 1 Shift Manager
        Employee shiftManager = new Employee(
                "Yossi cohen",
                222222222,
                "shiftadmin",
                "IL1234598760",
                10000,
                LocalDate.now().minusMonths(2),
                3, 3, 7, 7
        );
        User shiftManagerUser = new User(shiftManager, Level.shiftManager);

        curBranch.getEmployees().add(shiftManager);
        curBranch.getUsers().add(shiftManagerUser);
        shiftManager.getRelevantRoles(hrManagerUser).add(curBranch.getRoles().get(0));
        // Create 10 roles with real English names
        String[] roleNames = {
                "Cashier", "Driver", "Warehouse Worker", "Receptionist", "Loader",
                "Cleaner", "Customer Service Representative", "Technician", "Maintenance Worker"
        };

        for (String roleName : roleNames) {
            Role role = new Role(roleName);
            curBranch.getRoles().add(role);
        }

        // Launch login screen
        LoginScreen login = new LoginScreen();
        login.start(curBranch);
    }
}
