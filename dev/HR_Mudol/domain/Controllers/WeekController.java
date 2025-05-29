package HR_Mudol.domain.Controllers;

import HR_Mudol.domain.Objects.*;
import HR_Mudol.domain.Status;

import java.util.*;


/**
 * The WeekManager class handles the management of shifts and roles within a given week.
 * This includes assigning employees to shifts, managing roles, and ensuring that all shifts
 * are filled appropriately.
 */
public class WeekController implements IWeekController {

    private IShiftController dependency;

    private Branch curBranch;


    /**
     * Constructor for WeekManager.
     *
     * @param dependency The IShiftManager dependency used for shift management operations.
     * @param curBranch The current branch being managed.
     */
    public WeekController(IShiftController dependency, Branch curBranch) {
        this.dependency = dependency;
        this.curBranch=curBranch;
    }

    /**
     * Creates a new week with shifts, empty at first.
     *
     * @param caller The user who is requesting the creation of a new week.
     * @return A new Week object.
     */
    @Override
    public Week createNewWeek(User caller) {
        Week newWeek = new Week(); //only on RAM

        return newWeek;
    }

    /**
     * Manages the roles for each shift within the week, ensuring the correct roles are assigned.
     * Only managers can execute this operation.
     *
     * @param caller The user who is attempting to manage the roles.
     * @param week The week for which roles are being managed.
     * @throws SecurityException if the caller is not a manager.
     * @throws IllegalArgumentException if there are no roles or employees in the system.
     */
    @Override
    public void manageTheWeekRelevantRoles(User caller, Week week) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
        if (this.curBranch.getRoleRepo().getAll().size() <= 1)
        {
            throw new IllegalArgumentException("No roles at the system - first add roles.");
        }
        if (this.curBranch.getEmployeeRepo().getAll().isEmpty())
        {
            throw new IllegalArgumentException("No employees at the system - first add them.");
        }

        for (Shift shift : week.getShifts()) {

            dependency.chooseRelevantRoleForShift(caller, shift);
        }
    }

    /**
     * Adds a role to the selected shift in the week.
     *
     * @param caller The user who is adding the role.
     * @param week The week in which the shift is located.
     */
    @Override
    public void addARoleToShift(User caller, Week week){
        dependency.chooseRelevantRoleForShift(caller,findShift(week));
    }

    /**
     * Assigns employees to the shifts for the week. Each shift is checked for available roles,
     * and employees are assigned accordingly.
     *
     * @param caller The user who is assigning employees.
     * @param week The week in which the shifts and roles are to be filled.
     */
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

            Status newStatus = (shift.getNecessaryRoles().size() == shift.getEmployees().size())
                    ? Status.Full
                    : Status.Problem;

            // RAM
            shift.updateStatus(caller, newStatus);

            // DB
            curBranch.getWeekRepo().updateShiftStatus(shift.getShiftID(), newStatus);

            System.out.println("Finished with that shift.\n");
        }
    }

    /**
     * Chooses an employee for a specific role in a shift, considering availability and constraints.
     *
     * @param caller The user attempting to assign the employee.
     * @param week The week during which the shift is scheduled.
     * @param shift The shift for which the employee is being selected.
     * @param role The role to be assigned to the employee.
     * @return The chosen employee, or null if no employee is available.
     */
    private Employee chooseEmployeeForRole(User caller, Week week, Shift shift, Role role) {
        Scanner scanner = new Scanner(System.in);
        List<Employee> candidates = role.getRelevantEmployees();
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

                Constraint constraint = curBranch.getConstraintRepo()
                        .getConstraint(employee.getEmpId(), shift.getDay(), shift.getType());

                if (constraint == null) {
                    System.out.println("The employee can work this shift.");
                    System.out.print("Do you want to choose him/her? (Y/N): ");
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

    /**
     * Prints the list of relevant employees for a specific role.
     *
     * @param caller The user requesting the list of employees.
     * @param role The role for which employees are being listed.
     */
    private void printRelevantEmp(User caller, Role role) {
        int index = 1;
        for (Employee emp : role.getRelevantEmployees()) {
            System.out.println(index + ". " + emp.getEmpName());
            index++;

        }
    }

    /**
     * Cancels a shift for a holiday or other reason.
     *
     * @param caller The user attempting to cancel the shift.
     * @param week The week in which the shift is located.
     * @throws SecurityException if the caller is not a manager.
     */
    @Override
    public void cancelShift(User caller, Week week) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }

        Shift shift=findShift(week);

        week.removeShift(shift); //DB

        curBranch.getWeekRepo().deleteShift(shift.getShiftID());//DB

        System.out.println(shift + " was deleted");

    }

    /**
     * Retrieves the shifts for a specific employee in a given week.
     *
     * @param employee The employee whose shifts are being retrieved.
     * @param curWeek The week for which shifts are being retrieved.
     * @return A list of shifts assigned to the employee.
     */
    @Override
    public List<Shift> getShiftsForEmployee(Employee employee, Week curWeek) {
        List<Shift> result = new ArrayList<>();

        for (Shift shift : curWeek.getShifts()) {
            if (curBranch.getWeekRepo().isEmployeeAssignedToShift(employee.getEmpId(), shift.getShiftID())) {
                result.add(shift);
            }
        }

        return result;
    }


    /**
     * Prints out the details of the entire week, including shifts and assigned roles.
     *
     * @param week The week to be printed.
     */
    @Override
    public void printWeek(Week week) {
        System.out.println(week);
    }

    /**
     * Checks if there are any unassigned roles in the given week.
     *
     * @param week The week to check for unassigned roles.
     * @return The number of shifts with unassigned roles.
     */
    @Override
    public int hasUnassignedRoles(Week week) {
        int count = 0;

        for (Shift shift : week.getShifts()) {
            Status dbStatus = curBranch.getWeekRepo().getShiftStatus(shift.getShiftID());
            if (dbStatus == Status.Problem || dbStatus == Status.Empty) {
                count++;
            }
        }

        return count;
    }

    /**
     * Removes an employee from a shift.
     *
     * @param caller The user attempting to remove the employee.
     * @param week The week in which the shift is located.
     * @throws SecurityException if the caller is not a manager.
     */
    @Override
    public void removeEmployeeFromShift(User caller,Week week){
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
        dependency.removeEmployeeFromShift(caller,findShift(week)); //if the shift null it will print msg
    }


    /**
     * Removes a role from a shift.
     *
     * @param caller The user attempting to remove the role.
     * @param week The week in which the shift is located.
     * @throws SecurityException if the caller is not a manager.
     */
    @Override
    public void removeRoleFromShift(User caller,Week week) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
        dependency.removeRoleFromShift(caller,findShift(week));
    }

    /**
     * Adds an employee to a shift.
     *
     * @param caller The user attempting to add the employee.
     * @param week The week in which the shift is located.
     * @throws SecurityException if the caller is not a manager.
     */
    @Override
    public void addEmployeeToShift(User caller, Week week) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }

        // לבחור משמרת
        Shift shift = findShift(week);
        if (shift == null) {
            System.out.println("Shift not found.");
            return;
        }

        // לבחור תפקידים פנויים
        List<Role> relevantRole = shift.getNotOccupiedRoles();
        if (relevantRole == null || relevantRole.isEmpty()) {
            System.out.println("All roles are already assigned in this shift.");
            return;
        }

        // הדפסת רשימת התפקידים עם ה-ID
        printRolesList(caller, relevantRole);

        Scanner scanner = new Scanner(System.in);
        Role selectedRole = null;

        // בחירת תפקיד לפי ID
        while (selectedRole == null) {
            System.out.print("Choose role ID: ");
            String input = scanner.nextLine().trim();

            try {
                int roleId = Integer.parseInt(input);
                selectedRole = findRoleById(relevantRole, roleId);
                if (selectedRole == null) {
                    System.out.println("Invalid role ID. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid role ID.");
            }
        }

        // לבחור עובד לתפקיד
        Employee chosen = chooseEmployeeForRole(caller, week, shift, selectedRole);

        if (chosen == null) {
            System.out.println("No employee was selected for the role. Action cancelled.");
            return;
        }

        // מימוש ההשמה בפועל
        dependency.assignEmployeeToShift(caller, shift, chosen, selectedRole);
        System.out.println(chosen.getEmpName() + " assigned successfully to the shift.");
    }

        /**
     * Finds a role by its ID in a list of roles.
     *
     * @param roles The list of roles to search through.
     * @param roleId The ID of the role to find.
     * @return The role with the specified ID, or null if not found.
     */
    private Role findRoleById(List<Role> roles,int roleId) {
        for (Role role : roles) {
            if (role.getRoleNumber()==(roleId)) {
                return role;
            }
        }
        return null;
    }

    /**
     * Prints the list of roles with their IDs.
     *
     * @param caller The user requesting the list of roles.
     * @param roles The list of roles to print.
     */
    private void printRolesList(User caller, List<Role> roles) {
        for (Role r : roles) {

                System.out.println(r.getRoleNumber() + " - " + r.getDescription());

        }
    }

    /**
     * Finds a shift in the week based on the specified day and type.
     *
     * @param week The week to search in.
     * @return The matching shift, or null if not found.
     */
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
