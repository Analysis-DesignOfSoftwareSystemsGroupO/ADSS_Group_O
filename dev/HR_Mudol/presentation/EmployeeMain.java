package HR_Mudol.presentation;

import HR_Mudol.Service.EmployeeSystem.EmployeeSystem;
import HR_Mudol.domain.Employee;
import HR_Mudol.domain.Level;
import HR_Mudol.domain.User;
import HR_Mudol.domain.Week;

import java.time.LocalDate;
import java.util.Scanner;

public class EmployeeMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Create an employee and wrap with User
        Employee emp = new Employee("Dana", 123456789, "pass", "123456", 5000, LocalDate.now(), 1, 1, 0, 0);
        User user = new User(emp, Level.regularEmp);

        // Simulate current week's shifts
        Week week = new Week();

        // HR Manager to perform assignment
        Employee dummyHR = new Employee("HR", 999999999, "admin", "0000", 10000, LocalDate.now(), 0, 0, 0, 0);
        User hrUser = new User(dummyHR, Level.HRManager);
        // Launch employee menu
//        EmployeeMenu menu = new EmployeeMenu();
//        menu.start(user, emp, week);



        // Assign Dana to first shift in the week
        week.getShifts().get(0).addEmployee(hrUser, emp);

        // Create system handler
        EmployeeSystem system = new EmployeeSystem();

        // Demonstrate viewMyShifts
        system.viewMyShifts(user, emp, week);

        // Demonstrate submitConstraint
        system.submitConstraint(user, emp, week);
        // Demonstrate updateConstraint (with week to check shift assignment)
        system.updateConstraint(user, emp, week);

    }
}
