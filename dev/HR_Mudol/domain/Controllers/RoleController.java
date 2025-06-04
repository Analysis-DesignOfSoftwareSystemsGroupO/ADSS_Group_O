package HR_Mudol.domain.Controllers;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.RoleDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.domain.Objects.Branch;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.Role;
import HR_Mudol.domain.Objects.User;
import TransportModule.DTO.DriverDto;
import TransportModule.transport_module.DriverControllerDomain;


/**
 * Manages roles within a branch: creation, assignment, removal, and description updates.
 * Ensures security by validating manager privileges.
 */
public class RoleController implements IRoleController {

    private Branch curBranch;
    private Scanner scanner;
    private IEmployeeController employeeManager;
    private DTOToDomainMapper mapper;

    public RoleController(BranchDTO Branch) throws SQLException {
        this.curBranch = DTOToDomainMapper.fromDTO(Branch);
        this.scanner = new Scanner(System.in);
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
    public void setEmployeeManager(IEmployeeController employeeManager) {
        this.employeeManager = employeeManager;
    }

    @Override
    public void createRole(UserDTO theCaller) throws SQLException {
        User caller=mapper.fromDTO(theCaller);
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        System.out.print("Enter role description: ");
        String description = scanner.nextLine().trim();


        if (description.isEmpty()) {
            System.out.println("Role description cannot be empty.");
            return;
        }

        for (Role role : curBranch.getRoleRepo().getAll()) {
            if (role.getDescription().equalsIgnoreCase(description)) {
                System.out.println("Role already exists.");
                return;
            }
        }

        //Create domain object - RAM
        RoleDTO newRole = new RoleDTO(description);

        //Add to DB
        curBranch.getRoleRepo().addFromDTO(newRole); // internally converts to DTO and calls DAO


        System.out.println("Role created successfully.");
    }

    public void createRolebydescription(UserDTO theCaller,String str) throws SQLException {
        //Create domain object - RAM
        RoleDTO newRole = new RoleDTO(str);


        //Add to DB
        curBranch.getRoleRepo().addFromDTO(newRole); // internally converts to DTO and calls DAO


    }

        @Override
    public void updateRoleDescription(UserDTO theCaller) throws SQLException {
        User caller=mapper.fromDTO(theCaller);
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        int roleNumber = getIntInput("Enter role number to update: ");
        Role role = getRoleByNumber(roleNumber);
        if (role == null) {
            System.out.println("Role not found.");
            return;
        }

        System.out.print("Enter new description: ");
        String newDesc = scanner.nextLine().trim();

        if (newDesc.isEmpty()) {
            System.out.println("Description cannot be empty.");
            return;
        }

        try {
            curBranch.getRoleRepo().updateDescription(role, newDesc);
            System.out.println("Role updated.");
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void assignEmployeeToRole(UserDTO theCaller) throws SQLException {
        User caller = mapper.fromDTO(theCaller);

        if (!caller.isManager()) throw new SecurityException("Access denied.");
        // הדפסת כל ת"ז של העובדים
        List<Employee> allEmployees = curBranch.getEmployeeRepo().getAll();
        System.out.println("Employee IDs:");
        for (Employee e : allEmployees) {
            System.out.println("- " + e.getEmpId());
        }

        long empId = getIntInput("Enter employee ID : ");
        Employee employee = DTOToDomainMapper.fromDTO(employeeManager.getEmployeeById(theCaller, empId));
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        // הדפסת כל התפקידים לפי תיאור
        List<Role> allRoles = curBranch.getRoleRepo().getAllRoles();
        System.out.println("Available roles:");
        for (Role r : allRoles) {
            System.out.println("- " + r.getDescription());
        }

        String roleDesc = getStringInput("Enter role description to assign: ");
        Role chosenRole = curBranch.getRoleRepo().getRoleByDescription(roleDesc);

        if (chosenRole == null) {
            System.out.println("Role not found.");
            return;
        }

        List<String> driverList = new ArrayList<>();
        if (chosenRole.getDescription().toLowerCase().contains("driver")) {

            for (Role r : employee.getRelevantRoles()) {
                if (r.getDescription().toLowerCase().contains("driver")) {
                    System.out.println("this employee already have a driver in his role.");
                    return;
                }
                else{
                    driverList.add(chosenRole.getDescription());
                }
            }
        }



        DriverDto dto = new DriverDto(Integer.toString((int)employee.getEmpId()),driverList);
        DriverControllerDomain driverControllerDomain;
        try {
            driverControllerDomain = new DriverControllerDomain();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }


        try {
            driverControllerDomain.addDriverFromDto(dto);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }


        curBranch.getRoleRepo().assignEmployeeToRole(employee, chosenRole); // updates RAM and DB
        curBranch.getEmployeeRepo().getById(empId).addNewRole(caller,chosenRole);
        System.out.println("Employee assigned to role.");
    }


    @Override
    public void assignEmployeeToShiftManager(UserDTO theCaller) throws SQLException {
        User caller = mapper.fromDTO(theCaller);
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        int empId = getIntInput("Enter employee ID to promote to Shift Manager: ");
        Employee employee = DTOToDomainMapper.fromDTO(employeeManager.getEmployeeById(theCaller, empId));
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        Role shiftManager = curBranch.getRoleRepo().getRoleByDescription("Shift Manager");
        if (shiftManager == null) {
            System.out.println("Shift Manager role not found.");
            return;
        }

        curBranch.getRoleRepo().assignEmployeeToRole(employee, shiftManager); // updates DB
        shiftManager.addNewEmployee(employee); // updates RAM role side
        curBranch.getEmployeeRepo().getById(empId).addNewRole(caller, shiftManager); // updates RAM employee side

        System.out.println("Employee assigned as Shift Manager.");
    }


    @Override
    public void removeEmployeeFromALLRoles(UserDTO theCaller) throws SQLException {
        User caller=mapper.fromDTO(theCaller);
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        int empId = getIntInput("Enter employee ID: ");
        Employee employee = DTOToDomainMapper.fromDTO(employeeManager.getEmployeeById(theCaller, empId));
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        // Remove the employee from all roles using the repository (memory + DB)
        curBranch.getRoleRepo().removeEmployeeFromAllRoles(employee);

        System.out.println("Employee removed from all roles.");
    }

    @Override
    public void removeEmployeeFromRole(UserDTO theCaller, int roleId, EmployeeDTO employee) throws SQLException {
        User caller = mapper.fromDTO(theCaller);
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        Role role = getRoleByNumber(roleId);
        if (role == null) {
            System.out.println("Role not found.");
            return;
        }

        try {
            role.removeEmployee(mapper.fromDTO(employee)); // RAM
            curBranch.getRoleRepo().removeEmployeeFromRole(mapper.fromDTO(employee), role); // DB
            System.out.println("Employee removed from role: " + role.getDescription());
        } catch (SecurityException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    @Override
    public List<Employee> getRelevantEmployees(UserDTO theCaller) throws SQLException {

        User caller=mapper.fromDTO(theCaller);
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        return curBranch.getRoleRepo().getAllRelevantEmployees();
    }

     */

    @Override
    public List<Role> getAllRoles(UserDTO theCaller) throws SQLException {
        User caller=mapper.fromDTO(theCaller);
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        return curBranch.getRoleRepo().getAllRoles();
    }

    @Override
    public void printAllRoles(UserDTO theCaller) throws SQLException {
        List<Role> roles = curBranch.getRoleRepo().getAllRoles();
        if (roles.isEmpty()) {
            System.out.println("No roles found.");
            return;
        }
        System.out.println("Available Roles:");
        for (Role role : roles) {
            System.out.println( "Role Num: " + role.getRoleNumber() + " - Description: " + role.getDescription());
            for (Employee e : curBranch.getEmployeeRepo().getAll()){
                for (Role r : e.getRelevantRoles()){
                    if (role.equals(r)&& !roles.isEmpty() ){
                        System.out.println("  - ID: " + e.getEmpId() + ", Name: " + e.getEmpName());
                    }
                }
            }
        }
    }




    @Override
    public Role getRoleByNumber(int roleNumber) {
        return curBranch.getRoleRepo().getRoleByNumber(roleNumber);
    }

    @Override
    public int countEmployeesWithoutRoles(UserDTO theCaller, List<EmployeeDTO> employeeList) throws SQLException {
        User caller=mapper.fromDTO(theCaller);
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        List<Integer> empIDsWithRoles = curBranch.getRoleRepo().getAllEmployeeIDsWithRoles();

        int count = 0;
        for (EmployeeDTO emp : employeeList) {
            if (!empIDsWithRoles.contains(emp.getEmployeeId())) {
                count++;
            }
        }
        return count;
    }

    private int getIntInput(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid number.");
            return getIntInput(prompt);
        }
    }

    @Override
    public void deleteRole(UserDTO caller, String description) throws SQLException {
        User user = mapper.fromDTO(caller);
        if (!user.isManager())
            throw new SecurityException("Only managers can delete roles.");

        Role role = curBranch.getRoleRepo().getRoleByDescription(description);
        if (role == null) {
            System.out.println("❌ Role '" + description + "' not found.");
            return;
        }

        if (!role.getRelevantEmployees().isEmpty()) {
            System.out.println("⚠️ Cannot delete role '" + description + "': Employees are still assigned to it.");
            return;
        }

        curBranch.getRoleRepo().deleteByDescription(description);
        System.out.println("✅ Role '" + description + "' deleted successfully.");
    }
    private String getStringInput(String message) {
        System.out.print(message);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }



    public void removeEmployeeFromRoleInteractive(UserDTO theCaller, Scanner sc) throws SQLException {
        User caller = mapper.fromDTO(theCaller);
        boolean is_driver = false;
        if (!caller.isManager()) throw new SecurityException("Access denied.");

        List<Role> roles = getAllRoles(theCaller);
        if (roles.isEmpty()) {
            System.out.println("No roles found.");
            return;
        }

        System.out.println("Available Roles:");
        for (Role role : roles) {
            System.out.println( curBranch.getRoleRepo().getRoleByDescription(role.getDescription()).getRoleNumber() + " " + "- Description: " + role.getDescription());
        }

        System.out.print("Enter role description: ");
        String roleDesc = sc.nextLine().trim();

        Role selectedRole = curBranch.getRoleRepo().getRoleByDescription(roleDesc);

        if (selectedRole == null) {
            System.out.println("Role not found.");
            return;
        }
        if (selectedRole.getDescription().toLowerCase().contains("driver")) {
            is_driver = true;
        }
//        this.printAllRoles(theCaller);
        for (Role role : roles) {
            if (roleDesc.equals(role.getDescription())){
            System.out.println( "Role Num: " + role.getRoleNumber() + " - Description: " + role.getDescription());
            for (Employee e : curBranch.getEmployeeRepo().getAll()){
                for (Role r : e.getRelevantRoles()){
                    if (role.equals(r)&& !roles.isEmpty() ){
                        System.out.println("  - ID: " + e.getEmpId() + ", Name: " + e.getEmpName());
                    }
                }
            }
            }
        }


        System.out.print("Enter employee ID to remove: ");
        long empId = Long.parseLong(sc.nextLine());

        Employee employee = curBranch.getEmployeeRepo().getById(empId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }

        if(is_driver) {
            DriverControllerDomain driverControllerDomain;
            try {
                driverControllerDomain = new DriverControllerDomain();
                driverControllerDomain.deleteDriverById(Integer.toString((int)employee.getEmpId()));
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
        }




        try {
            selectedRole.removeEmployee(employee); // RAM
            curBranch.getRoleRepo().removeEmployeeFromRole(employee, selectedRole); // DB
            employee.removeRole(caller,selectedRole);
            System.out.println("Employee removed from role: " + selectedRole.getDescription());
        } catch (SecurityException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }





}
