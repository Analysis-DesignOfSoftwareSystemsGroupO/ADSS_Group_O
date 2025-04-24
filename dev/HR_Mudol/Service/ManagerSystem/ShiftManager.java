package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.*;

import java.util.Scanner;

public class ShiftManager implements IShiftManager {

    private IRoleManager dependency;

    public ShiftManager(IRoleManager dependency){
        this.dependency = dependency;
    }

    @Override
    public void assignEmployeeToShift(User caller, Shift shift, Employee employee) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }

        shift.addEmployee(caller,employee);
        System.out.println("Employee " + employee.getEmpName() + " assigned to shift " + shift.getDay()+ " - "+ shift.getType() +".");
    }

    @Override
    public void removeEmployeeFromShift(User caller, Shift shift, Employee employee) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }

        shift.removeEmployee(caller,employee);
        System.out.println("Employee " + employee.getEmpName() + " removed from shift " + shift.getDay()+ " - "+ shift.getType() +".");
    }

    @Override
    public void chooseRelevantRoleForShift(User caller, Shift shift) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }

        // always add shift manager
        shift.addNecessaryRoles(caller, dependency.getRoleByNumber(1));

        Scanner scanner = new Scanner(System.in);
        boolean done = false;

        while (!done) {
            printRolesList(caller);
            int roleNumber = -1;

            // קלט מספרי תקין
            while (true) {
                System.out.print("Enter relevant role number to add to the shift " + shift.getDay() + " - " + shift.getType() + ": ");
                String input = scanner.nextLine();
                try {
                    roleNumber = Integer.parseInt(input);
                    Role role = dependency.getRoleByNumber(roleNumber);
                    if (role == null) {
                        System.out.println("Role number does not exist. Please try again.");
                    } else if (role.getRoleNumber() == 1) {
                        System.out.println("Shift Manager is already added. Please choose a different role.");
                    } else {
                        shift.addNecessaryRoles(caller, role);
                        System.out.println(role.getDescription() + " was added to the shift.");
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                }
            }

            // קלט לסיום
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
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }

        System.out.println(shift.toString());
    }

    private void printRolesList(User caller){

        for (Role r :dependency.getAllRoles(caller) ) {
            if (r.getRoleNumber()!=1) //לדלג על המנהל משמרת כי צריך רק אחמש אחד
            { System.out.print(r.getRoleNumber() +" - " + r.getDescription()+ "\n");}
        }
    }
}

