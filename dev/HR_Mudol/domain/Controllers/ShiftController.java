package HR_Mudol.domain.Controllers;

import HR_Mudol.DTO.*;
import HR_Mudol.domain.Objects.*;
import HR_Mudol.domain.ShiftType;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * ShiftManager class manages shift operations:
 * Assign employees to shifts, remove employees, add/remove roles, and print shifts.
 */
public class ShiftController implements IShiftController {

    private Branch curBranch;
    private IRoleController dependency; // Dependency for accessing role management
    private DTOToDomainMapper mapper;

    /**
     * Constructor for ShiftManager.
     * @param dependency The role manager dependency used for role-related operations.
     */
    public ShiftController(BranchDTO Branch, IRoleController dependency) throws SQLException {
        this.curBranch=DTOToDomainMapper.fromDTO(Branch);
        this.dependency = dependency;
        DTOToDomainMapper.initialize(
                curBranch.getUserRepo(),
                curBranch.getEmployeeRepo(),
                curBranch.getRoleRepo(),
                curBranch.getWeekRepo()
        );

        //this.mapper=new DTOToDomainMapper(curBranch.getUserRepo(),curBranch.getEmployeeRepo(),curBranch.getRoleRepo(),curBranch.getWeekRepo());
    }
    @Override
    public void close() {
        try {
            curBranch.close();
        } catch (Exception e) {
            System.out.println("❌ Failed to close branch resources: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void assignEmployeeToShift(UserDTO theCaller, ShiftDTO theShift, EmployeeDTO theEmployee, RoleDTO theRole) throws SQLException {

        User caller=mapper.fromDTO(theCaller);
        Shift shift=mapper.fromDTO(theShift);
        Employee employee=mapper.fromDTO(theEmployee);
        Role role=mapper.fromDTO(theRole);

        // Authorization check
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }

        // save at RAM
        shift.addEmployee(employee, role);

        //save at the DB
        curBranch.getWeekRepo().insertEmployeeToShift(curBranch.getBranchID(),employee.getEmpId(), shift.getShiftID(), role.getRoleNumber());
    }


    @Override
    public void removeEmployeeFromShift(UserDTO theCaller, ShiftDTO theShift) throws SQLException {

        User caller=mapper.fromDTO(theCaller);
        Shift shift=mapper.fromDTO(theShift);

        if (!caller.isManager() && !caller.isShiftManager()) {
            System.out.println("Access denied. Only shift managers can remove employees from shifts.");
            return;
        }

        if (!isShiftManagerOfShift(caller, shift)) {
            System.out.println("Access denied. You are not the shift manager of this shift.");
            return;
        }

        // Authorization check
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }

        // Check if shift exists
        if (shift == null) {
            System.out.println("Shift doesn't exist.");
            return;
        }


        List<Employee> employees = shift.getEmployees();
        // Check if no employees assigned
        if (employees.isEmpty()) {
            System.out.println("No employees assigned to this shift.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose an employee to remove by their index:");

        // Print list of employees with indexes
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

        // Remove selected employee - RAM
        Employee employeeToRemove = employees.get(chosenIndex - 1);
        shift.removeEmployee(caller, employeeToRemove);

        //Remove from DB
        curBranch.getWeekRepo().removeEmployeeFromShift(employeeToRemove.getEmpNum(),shift.getShiftID());

        System.out.println(employeeToRemove.getEmpName() +
                " was removed from shift " + shift.getDay() + " - " + shift.getType() + ".");
    }



    @Override
    public void removeRoleFromShift(UserDTO theCaller, ShiftDTO theShift) throws SQLException {
        User caller=mapper.fromDTO(theCaller);
        Shift shift=mapper.fromDTO(theShift);

        Scanner scanner = new Scanner(System.in);
        List<Role> relevantRoles = shift.getNecessaryRoles();

        // Check if there are no roles assigned
        if (relevantRoles.isEmpty()) {
            System.out.println("There are no roles assigned to this shift.");
            return;
        }

        Role role = null;

        // Loop until a valid role is selected
        while (role == null) {
            System.out.println("\nChoose a role to remove from shift " + shift.getDay() + " - " + shift.getType());
            printRolesListForShift(relevantRoles);
            System.out.print("Enter role ID (or type 'exit' to cancel): ");

            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                System.out.println("Operation cancelled.");
                return;
            }

            try {
                int roleId = Integer.parseInt(input);
                role = findRoleById(relevantRoles, roleId);

                if (role == null) {
                    System.out.println("This role is not assigned to the shift. Please try again.");
                    role = null; // Stay in the loop
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid role ID.");
            }
        }

        // Remove the selected role from RAM
        shift.removeRole(caller, role);
        System.out.println("Role \"" + role.getDescription() + "\" was removed from the shift.");

        // 2. עדכון RequiredRoles בטבלת DB
        curBranch.getWeekRepo().removeRoleFromShift(curBranch.getBranchID(), shift.getShiftID(), role.getRoleNumber());

        System.out.println("Role \"" + role.getDescription() + "\" was removed from the shift.");
    }


    /**
     * Prints a list of roles assigned to a shift.
     * @param roles The list of roles assigned to the shift.
     */
    private void printRolesListForShift(List<Role> roles) {
        // Print roles assigned to the shift
        System.out.println("Roles assigned to this shift:");
        for (Role role : roles) {
            System.out.println("Role ID: " + role.getRoleNumber() + " - " + role.getDescription());
        }
    }

    /**
     * Finds a role by its ID from a list of roles.
     * @param roles The list of roles to search in.
     * @param roleId The ID of the role to find.
     * @return The role with the matching ID, or null if not found.
     */
    private Role findRoleById(List<Role> roles, int roleId) {
        // Find a role by its ID
        for (Role role : roles) {
            if (role.getRoleNumber() == roleId) {
                return role;
            }
        }
        return null;
    }


    @Override
    public void chooseRelevantRoleForShift(UserDTO theCaller, ShiftDTO theShift) throws SQLException {
        /// todo dont let driver to add
        User caller=mapper.fromDTO(theCaller);
        Shift shift=mapper.fromDTO(theShift);

        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }

        System.out.println("For Shift "+theShift.getDay()+" at "+theShift.getType());

        // Add Shift Manager automatically (only once)
        Role shiftManager = dependency.getRoleByNumber(1);
        shift.addNecessaryRoles(shiftManager);
        curBranch.getWeekRepo().addOrUpdateRequiredRole(
                curBranch.getBranchID(), shift.getDay(),shift.getType(), 1, 1
        )
        ;

        Scanner scanner = new Scanner(System.in);
        boolean done = false;

        while (!done) {
            printRolesList(dependency.getAllRoles(theCaller));

            int roleNumber = -1;
            while (true) {
                System.out.print("Enter role number to add (other than 1): ");
                String input = scanner.nextLine();

                try {
                    roleNumber = Integer.parseInt(input);
                    Role role = dependency.getRoleByNumber(roleNumber);

                    if (role == null) {
                        System.out.println("Role number does not exist. Please try again.");
                    } else if (roleNumber == 1) {
                        System.out.println("Shift Manager was automatically added. Please choose another role.");
                    } else {
                        // Ask for the amount:
                        int count = -1;
                        while (count < 1) {
                            System.out.print("Enter number of employees required for this role: ");
                            try {
                                count = Integer.parseInt(scanner.nextLine().trim());
                                if (count < 1) {
                                    System.out.println("Please enter a positive number.");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Invalid input. Please enter a number.");
                            }
                        }

                        // Add to RAM
                        for (int i = 0; i < count; i++) {
                            shift.addNecessaryRoles(role);
                        }

                        // Add to DB
                        curBranch.getWeekRepo().addOrUpdateRequiredRole(
                                curBranch.getBranchID(), shift.getDay(),shift.getType(), roleNumber, count
                        );

                        System.out.println(count + " x " + role.getDescription() + " added to the shift.");
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

        List <RoleDTO> roleDTOList=new ArrayList<>();
        for (Role r : shift.getNecessaryRoles()) {
            roleDTOList.add(DTOToDomainMapper.toDTO(r));
        }
        theShift.setNecessaryRoles(roleDTOList);
    }



    @Override
    public void printShift(UserDTO theCaller, ShiftDTO theShift) throws SQLException {
        User caller=mapper.fromDTO(theCaller);
        Shift shift=mapper.fromDTO(theShift);
        // Authorization check
        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }

        System.out.println(shift.toString());
    }


    /**
     * Helper -Prints the list of roles available for the caller to choose from.
     * @param roles The list of available roles.
     */
    private void printRolesList(List<Role> roles) {
        for (Role r : roles) {
            System.out.println(r.getRoleNumber() + " - " + r.getDescription());

        }
    }

    private boolean isShiftManagerOfShift(User caller, Shift shift) {
        return shift.getShiftManager() != null &&
                caller.getUser().getEmpId() == shift.getShiftManager().getEmpId();
    }

    @Override
    public List<ShiftDTO> getAllShiftDTOs() {
        List<ShiftDTO> result = new ArrayList<>();

        for (Week week : curBranch.getWeekRepo().getAll()) {
            for (Shift shift : week.getShifts()) {
                result.add(mapper.toDTO(shift));
            }
        }

        return result;
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesAsDTOs() throws SQLException {
        List<EmployeeDTO> result = new ArrayList<>();
        for (Employee e : curBranch.getEmployeeRepo().getAll()) {
            result.add(DTOToDomainMapper.toDTO(e));
        }
        return result;
    }

    @Override
    public void addRoleToShiftIfNeeded(UserDTO callerDTO, ShiftDTO shiftDTO, RoleDTO roleDTO, int requiredAmount) throws SQLException {
        User caller = mapper.fromDTO(callerDTO);
        Shift shift = mapper.fromDTO(shiftDTO);
        Role role = mapper.fromDTO(roleDTO);

        if (!caller.isManager() && !caller.isShiftManager()) {
            throw new SecurityException("Access denied.");
        }

        long currentCount = shift.getNecessaryRoles().stream()
                .filter(r -> r.getRoleNumber() == role.getRoleNumber())
                .count();

        int toAdd = requiredAmount - (int) currentCount;
        if (toAdd <= 0) {
            return; // כבר יש מספיק תפקידים כאלה
        }

        // הוספה לזיכרון
        for (int i = 0; i < toAdd; i++) {
            shift.addNecessaryRoles(role);
        }

        // הוספה ל-DB
        curBranch.getWeekRepo().addOrUpdateRequiredRole(
                curBranch.getBranchID(),
                shift.getDay(),
                shift.getType(),
                role.getRoleNumber(),
                (int) currentCount + toAdd
        );


        System.out.printf("✅ %d x '%s' added to shift [%s %s].%n", toAdd, role.getDescription(), shift.getDay(), shift.getType());
    }






}
