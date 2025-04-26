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
                "HR Manager",
                111111111,
                "admin",
                "IL1234567890",
                15000,
                LocalDate.now(),
                3, 3, 10, 10
        );
        User hrManagerUser = new User(hrManager, Level.HRManager); // true = isManager
        curBranch.getEmployees().add(hrManager);
        curBranch.getUsers().add(hrManagerUser);

        // Create 10 regular employees
        for (int i = 0; i < 10; i++) {
            Employee employee = new Employee(
                    "Employee" + (i + 1),
                    200000000 + i, // ID 200000000, 200000001, etc.
                    "password" + (i + 1),
                    "IL987654321" + i,
                    8000 + i * 100,
                    LocalDate.now().minusDays(i * 10),
                    2, 2, 5, 5
            );
            User user = new User(employee, Level.regularEmp); // false = regular employee
            curBranch.getEmployees().add(employee);
            curBranch.getUsers().add(user);
        }

        // Create 10 roles
        for (int i = 0; i < 10; i++) {
            Role role = new Role("Role" + (i + 1));
            curBranch.getRoles().add(role);
        }

        // Launch login screen
        LoginScreen login = new LoginScreen();
        login.start(curBranch);
    }
}
