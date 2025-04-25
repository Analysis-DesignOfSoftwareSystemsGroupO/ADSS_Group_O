package HR_Mudol.Service.ManagerSystem;
import HR_Mudol.domain.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class WeekManager implements IWeekManager {

    private IShiftManager dependency;
    private Branch curBranch;

    public WeekManager(IShiftManager dependency,Branch curBranch ) {
        this.dependency = dependency;
        this.curBranch=curBranch;
    }

    @Override
    public Week createNewWeek(User caller) {
        Week newWeek = new Week();

        return newWeek;
    }

    //choose relevant role for each shift
    @Override
    public void manageTheWeekRelevantRoles(User caller, Week week) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
        if (this.curBranch.getRoles().size()<=1) {
            throw new IllegalArgumentException("No roles at the system - first add roles.");
        }
        if (this.curBranch.getEmployees().isEmpty()) {
            throw new IllegalArgumentException("No employees at the system - first add them.");
        }

        for (Shift shift : week.getShifts()) {

             dependency.chooseRelevantRoleForShift(caller, shift);
        }
    }

    @Override
    public void addARoleToShift(User caller, Week week){
        dependency.chooseRelevantRoleForShift(caller,findShift(week));
    }

    //Assigning employees to shifts
    @Override
    public void assigningEmployToShifts(User caller, Week week) {
        for (Shift shift : week.getShifts()) {
            System.out.print("For the shift " + shift.getDay() + " - " + shift.getType() + ", ");

            if (shift.getNecessaryRoles().isEmpty()) {
                System.out.println("First you have to assign roles to weekly shifts.");
                break;
            }

            for (Role role : shift.getNecessaryRoles()) {
                Employee chosen = chooseEmployeeForRole(caller, week, shift, role);
                if (chosen != null) {
                    dependency.assignEmployeeToShift(caller, shift, chosen, role);
                    System.out.println(chosen.getEmpName() + " assigned to " + role.getDescription() + " in this shift.");
                } else {
                    System.out.println("No suitable employee found for role: " + role.getDescription());
                }
                System.out.println("Next role.\n");
            }

            if (shift.getNecessaryRoles().size() == shift.getEmployees().size()) {
                shift.updateStatus(caller, Status.Full);
            } else {
                shift.updateStatus(caller, Status.Problem);
            }

            System.out.println("Finished with that shift.\n");
        }
    }


    private Employee chooseEmployeeForRole(User caller, Week week, Shift shift, Role role) {
        Scanner scanner = new Scanner(System.in);
        List<Employee> candidates = role.getRelevantEmployees(caller);
        if (candidates.isEmpty()) {
            System.out.println("No employees are available for role: " + role.getDescription());
            return null;
        }

        System.out.println("You should find an employee for the role - " + role.getDescription());
        printRelevantEmp(caller, role);

        Set<Integer> triedIndexes = new HashSet<>();

        while (triedIndexes.size() < candidates.size()) {
            System.out.println("\n(Choose the employee by their number in the list)");
            String input = scanner.nextLine().trim();

            try {
                int index = Integer.parseInt(input);
                if (index < 1 || index > candidates.size()) {
                    System.out.println("Invalid number. Please choose between 1 and " + candidates.size());
                    continue;
                }

                if (triedIndexes.contains(index)) {
                    System.out.println("You already tried this employee. Choose someone else.");
                    continue;
                }

                Employee employee = candidates.get(index - 1);
                triedIndexes.add(index);

                Constraint constraint = employee.searchingForRelevantconstraint(caller, week, shift.getDay(), shift.getType());

                if (constraint == null) {
                    System.out.println("The employee can work this shift.");
                    System.out.print("Do you want to choose them? (Y/N): ");
                    String choosing = scanner.nextLine().trim();

                    if (choosing.equalsIgnoreCase("Y")) {
                        if (!shift.getEmployees().contains(employee)) {
                            return employee;
                        } else {
                            System.out.println(employee.getEmpName() + " is already assigned to another role in this shift.");
                        }
                    } else {
                        System.out.println("Employee skipped. Try another.");
                    }
                } else {
                    System.out.println(employee.getEmpName() + " can't work this shift because: " + constraint.getExplanation());
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        return null;
    }


    private void printRelevantEmp(User caller, Role role) {
        int index = 1;
        for (Employee emp : role.getRelevantEmployees(caller)) {
            System.out.println(index + ". " + emp.getEmpName());
            index++;

        }
    }

    //Remove a shift - for holidays case use
    @Override
    public void cancelShift(User caller, Week week) {

        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
        Scanner scanner = new Scanner(System.in);

        Shift shift=findShift(week);
        week.removeShift(shift);
        System.out.println(shift + " was deleted");

    }

    @Override
    public List<Shift> getShiftsForEmployee(Employee employee, Week curWeek) {
        List<Shift> result = new ArrayList<>();

        for (Shift shift : curWeek.getShifts()) {
                if (shift.getEmployees().contains(employee)) {
                    result.add(shift);
                }
        }

        return result;
    }


    @Override
    public void printWeek(Week week) {
        System.out.println(week);
    }

    @Override
    public int hasUnassignedRoles(Week week) {
        int count = 0;
        for (Shift shift : week.getShifts()) {
            if (shift.getStatus()==Status.Problem||shift.getStatus()==Status.Empty) count++;
        }
        return count;
    }

    @Override
    public void removeEmployeeFromShift(User caller,Week week){
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
        dependency.removeEmployeeFromShift(caller,findShift(week)); //if the shift null it will print msg
    }

    @Override
    public void removeRoleFromShift(User caller,Week week) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
        dependency.removeRoleFromShift(caller,findShift(week));
    }

    @Override
    public void addEmployeeToShift(User caller,Week week){
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
        //choose a shift
        Shift shift = findShift(week);
        if (shift == null) {
            System.out.println("Shift not found.");
            return;
        }

        //choose a role - בהכרח כאלו שעדיין לא אויישו
        List<Role> relevantRole=shift.getNotOccupiedRoles();

        if (relevantRole == null || relevantRole.isEmpty()) {
            System.out.println("All roles are already assigned in this shift.");
            return;
        }
        // לבחור תפקיד מתוך הרשימה
        printRolesList(caller, relevantRole);

        Scanner scanner = new Scanner(System.in);
        Role selectedRole = null;

        while (selectedRole == null) {
            System.out.print("Choose role number: ");
            String input = scanner.nextLine().trim();
            try {
                int roleNum = Integer.parseInt(input);
                if (roleNum < 1 || roleNum > relevantRole.size()) {
                    System.out.println("Invalid number. Choose between 1 and " + relevantRole.size());
                    continue;
                }
                selectedRole = relevantRole.get(roleNum - 1);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        //choose an emp
        Employee chosen = chooseEmployeeForRole(caller, week, shift, selectedRole);
        dependency.addEmployeeToShift(caller,shift,chosen,selectedRole);
    }
    private void printRolesList(User caller, List<Role> roles) {
        for (Role r : roles) {
            if (r.getRoleNumber() != 1) { // דילוג על Shift Manager, כי כבר הוסף אוטומטית
                System.out.println(r.getRoleNumber() + " - " + r.getDescription());
            }
        }
    }

    private Shift findShift(Week week){
        Scanner scanner = new Scanner(System.in);

        System.out.print("Choose day (Capital letters only)\n");
        String day = scanner.nextLine();

        System.out.print("Choose type (Capital letters only)\n");
        String type = scanner.nextLine();

        Shift choosenShift=null;
        for (Shift shift : week.getShifts()) {
            if (shift.getType().name().equals(type) && shift.getDay().name().equals(day)) {
                choosenShift=shift;
                break;
            }
        }
        return choosenShift;
    }
}
