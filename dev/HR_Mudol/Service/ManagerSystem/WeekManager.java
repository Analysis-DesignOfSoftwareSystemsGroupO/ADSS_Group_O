package HR_Mudol.Service.ManagerSystem;
import HR_Mudol.domain.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class WeekManager implements IWeekManager {

    private IShiftManager dependency;

    public WeekManager(IShiftManager dependency) {
        this.dependency = dependency;
    }

    @Override
    public Week createNewWeek(User caller) {
        Week newWeek = new Week();

        return newWeek;
    }

    //choose relevant role for each shift
    public void manageTheWeekRelevantRoles(User caller, Week week) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied.");
        }

        for (Shift shift : week.getShifts()) {
            dependency.chooseRelevantRoleForShift(caller, shift);
        }
    }

    //Assigning employees to shifts
    @Override
    public void assigningEmployToShifts(User caller, Week week) {
        Scanner scanner = new Scanner(System.in);

        for (Shift shift : week.getShifts()) {
            System.out.print("For the shift " + shift.getDay() + " - " + shift.getType() + ", ");

            if (shift.getNecessaryRoles().isEmpty()) {
                System.out.println("First you have to assign roles to weekly shifts.");
                break;
            }

            for (Role role : shift.getNecessaryRoles()) {
                List<Employee> candidates = role.getRelevantEmployees(caller);
                if (candidates.isEmpty()) {
                    System.out.println("No employees are available for role: " + role.getDescription());
                    continue;
                }

                System.out.println("You should find an employee for the role - " + role.getDescription());
                printRelevantEmp(caller, role);

                int index = 0;
                boolean assigned = false;
                Set<Integer> triedIndexes = new HashSet<>();

                while (!assigned && triedIndexes.size() < candidates.size()) {
                    System.out.println("\n(Choose the employee by their number in the list)");
                    String input = scanner.nextLine().trim();

                    try {
                        index = Integer.parseInt(input);
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

                        Constraint constraint = employee.searchingForRelevantconstraint(caller, shift.getDay(), shift.getType());

                        if (constraint == null) {
                            System.out.println("The employee *can* work this shift.");
                            System.out.print("Do you want to choose them? (Y/N): ");
                            String choosing = scanner.nextLine().trim();

                            if (choosing.equalsIgnoreCase("Y")) {

                                if (!shift.getEmployees().contains(employee)) {
                                    dependency.assignEmployeeToShift(caller, shift, employee);
                                    assigned = true;
                                    System.out.println(employee.getEmpName() + " was assigned to the shift.");
                                } else {
                                    System.out.println(employee.getEmpName() + " was already assigned to  other role at that shift, try someone else.");
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

                if (!assigned) {
                    System.out.println("No suitable employee was found for the role: " + role.getDescription());
                }

                System.out.println("Next role.\n");
            }

            if (shift.getNecessaryRoles().size() == shift.getEmployees().size()) {
                shift.updateStatus(caller, Status.Full);
            } else {
                shift.updateStatus(caller, Status.Problem);
            }

            System.out.println("Finish with that shift.\n");
        }
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

        System.out.print("Choose day (Capital letters only)\n");
        String day = scanner.nextLine();

        System.out.print("Choose type (Capital letters only)\n");
        String type = scanner.nextLine();

        for (Shift shift : week.getShifts()) {
            if (shift.getType().name().equals(type) && shift.getDay().name().equals(day)) {
                week.removeShift(shift);
                System.out.println(shift + " was deleted");
                break;
            }
        }
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

}
