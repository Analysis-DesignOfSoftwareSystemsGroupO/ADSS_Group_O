package HR_Mudol.presentation.ManagerSystem;

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
    public void chooseRelevantRoleForShift(User caller, Shift shift){
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
        if (dependency.getAllRoles(caller).isEmpty()){
            throw new IllegalArgumentException("No roles at the system.");
        }

        printRolesList(caller);
        System.out.print("Enter relevant role number to add to te shift "+ shift.getDay() + " - "+ shift.getType()+ ":");
        Scanner scanner = new Scanner(System.in);
        int roleNumber = scanner.nextInt();

        boolean done = false;

        while (!done) {
            Role role = dependency.getRoleByNumber(roleNumber);
            shift.addNecessaryRoles(caller, role);
            System.out.print(role.getDescription() + " was added to the shift\n");

            while (true) {
                System.out.print("If you're done write D, else write N: ");
                String isDone = scanner.nextLine().trim();

                if (isDone.equalsIgnoreCase("D")) {
                    done = true;
                    break; // יוצא מהלולאת קלט וממשיך בלולאה הראשית
                } else if (isDone.equalsIgnoreCase("N")) {
                    break; // ממשיך להוספת תפקיד נוסף
                } else {
                    System.out.println("Invalid input. Please enter 'D' or 'N'.");
                }
            }

            if (!done) {
                printRolesList(caller);
                System.out.print("Enter role number to add: ");

                // ניקוי שאריות מה־buffer (אם צריך)
                while (!scanner.hasNextInt()) {
                    System.out.println("Please enter a valid number.");
                    scanner.nextLine();
                }
                roleNumber = scanner.nextInt();
                scanner.nextLine();
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
            System.out.print(r.getRoleNumber() +" - " + r.getDescription()+ "\n");
        }
    }
}

