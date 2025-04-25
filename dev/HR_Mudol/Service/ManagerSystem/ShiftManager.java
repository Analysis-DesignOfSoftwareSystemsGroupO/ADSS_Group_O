package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.*;

import java.util.List;
import java.util.Scanner;

public class ShiftManager implements IShiftManager {

    private IRoleManager dependency;

    public ShiftManager(IRoleManager dependency) {
        this.dependency = dependency;
    }

    @Override
    public void assignEmployeeToShift(User caller, Shift shift, Employee employee, Role role) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }

        shift.addEmployee(caller, employee,role);
        System.out.println(employee.getEmpName() +
                " assigned to shift " + shift.getDay() + " - " + shift.getType() + ".");
    }

    @Override
    public void removeEmployeeFromShift(User caller, Shift shift) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }
        if (shift==null){
            System.out.println("Shift doesn't exist.");
            return;
        }
        List<Employee> employees = shift.getEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees assigned to this shift.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an employee to remove by their index:");

        int index = 1;
        for (Employee e : employees) {
            System.out.println(index + ". " + e.getEmpName());
            index++;
        }

        String input = scanner.nextLine().trim();
        int chosenIndex;

        try {
            chosenIndex = Integer.parseInt(input);
            if (chosenIndex < 1 || chosenIndex > employees.size()) {
                System.out.println("Invalid index. Please enter a number between 1 and " + employees.size());
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return;
        }

        Employee employeeToRemove = employees.get(chosenIndex - 1);
        shift.removeEmployee(caller, employeeToRemove);

        System.out.println(employeeToRemove.getEmpName() +
                " was removed from shift " + shift.getDay() + " - " + shift.getType() + ".");
    }

    @Override
    public void removeRoleFromShift(User caller, Shift shift) {

        Scanner scanner = new Scanner(System.in);
        List<Role> relevantRoles = shift.getNecessaryRoles();

        if (relevantRoles.isEmpty()) {
            System.out.println("There are no roles assigned to this shift.");
            return;
        }

        Role role = null;

        // הדפסת רק את התפקידים ששייכים למשמרת
        while (role == null) {
            System.out.println("\nChoose a role to remove from shift " + shift.getDay() + " - " + shift.getType());
            printRolesListForShift(caller, relevantRoles);  // מדפיס רק את התפקידים ששייכים למשמרת
            System.out.print("Enter role ID (or type 'exit' to cancel): ");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Operation cancelled.");
                return;
            }

            try {
                int roleId = Integer.parseInt(input); // בחרת לפי ID של תפקיד
                role = findRoleById(relevantRoles, roleId);

                if (role == null) {
                    System.out.println("This role is not assigned to the shift. Please try again.");
                    role = null;  // make sure it keeps looping
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid role ID.");
            }
        }

        // הורדת התפקיד
        shift.removeRole(caller, role);
        System.out.println("Role \"" + role.getDescription() + "\" was removed from the shift.");
    }

    // הדפסת רשימת התפקידים ששייכים למשמרת לפי ID
    private void printRolesListForShift(User caller, List<Role> roles) {
        System.out.println("Roles assigned to this shift:");
        for (Role role : roles) {
            System.out.println("Role ID: " + role.getRoleNumber() + " - " + role.getDescription());
        }
    }
    // פונקציה שמחזירה את התפקיד לפי ה-ID
    private Role findRoleById(List<Role> roles, int roleId) {
        for (Role role : roles) {
            if (role.getRoleNumber() == roleId) {
                return role;
            }
        }
        return null;
    }

    @Override
    public void chooseRelevantRoleForShift(User caller, Shift shift) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }

        // תמיד מוסיפים אחראי משמרת (תפקיד 1)
        shift.addNecessaryRoles(caller, dependency.getRoleByNumber(1));

        Scanner scanner = new Scanner(System.in);
        boolean done = false;

        while (!done) {
            printRolesList(caller,dependency.getAllRoles(caller));

            int roleNumber = -1;
            while (true) {
                System.out.print("Enter relevant role number to add to the shift " +
                        shift.getDay() + " - " + shift.getType() + ": ");
                String input = scanner.nextLine();

                try {
                    roleNumber = Integer.parseInt(input);
                    Role role = dependency.getRoleByNumber(roleNumber);

                    if (role == null) {
                        System.out.println("Role number does not exist. Please try again.");
                    } else if (role.getRoleNumber() == 1) {
                        System.out.println("Shift Manager already added. Please choose another role.");
                    } else {
                        shift.addNecessaryRoles(caller, role);
                        System.out.println(role.getDescription() + " was added to the shift.");
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            while (true) {
                System.out.print("If you're done write D, else write N: ");
                String isDone = scanner.nextLine().trim();

                if (isDone.equalsIgnoreCase("D")) {
                    done = true;
                    break;
                } else if (isDone.equalsIgnoreCase("N")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'D' or 'N'.");
                }
            }
        }
    }

    @Override
    public void printShift(User caller, Shift shift) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }

        System.out.println(shift.toString());
    }

    @Override
    public void addEmployeeToShift(User caller, Shift shift, Employee employee, Role role) {
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }
        shift.addEmployee(caller, employee, role);
    }

    private void printRolesList(User caller, List<Role> roles) {
        for (Role r : roles) {
            if (r.getRoleNumber() != 1) { // דילוג על Shift Manager, כי כבר הוסף אוטומטית
                System.out.println(r.getRoleNumber() + " - " + r.getDescription());
            }
        }
    }
}
