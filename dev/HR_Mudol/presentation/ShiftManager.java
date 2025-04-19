package HR_Mudol.presentation;

import HR_Mudol.domain.*;
import java.util.LinkedList;
import java.util.List;
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

        dependency.printAllRoles(caller);
        System.out.print("Enter role number to add: ");
        Scanner scanner = new Scanner(System.in);
        int roleNumber = scanner.nextInt();
        scanner.nextLine();
        boolean done=false;

        while (!done) {

            Role role = dependency.getRoleByNumber(roleNumber);
            shift.addNecessaryRoles(caller, role);
            System.out.print(role.getDescription() +"was added to the shift \n");

            System.out.print("If you done write D, else write N \n");
            String isDone = scanner.nextLine();
            if (isDone == "D") {
                done = true;
                break;
            }
            dependency.printAllRoles(caller);
            System.out.print("Enter role number to add: ");
            roleNumber = scanner.nextInt();

        }

    }

    @Override
    public void printShift(User caller, Shift shift) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }

        System.out.println(shift.toString());
    }
}
