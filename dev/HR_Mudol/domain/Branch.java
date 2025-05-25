package HR_Mudol.domain;

import HR_Mudol.domain.repository.*;

/**
 * Represents a Branch in the company, managing employees, users, weeks, and roles.
 */
public class Branch {

    // Static counter for tracking number of created branches
    static int counter = 0;

    // Branch ID assigned at creation
    private int branchID;

    // Repositories
    private EmployeeRepository employeeRepo;
    private RoleRepository roleRepo;
    private UserRepository userRepo;
    private WeekRepository weekRepo;

    /**
     * Constructs an empty Branch with initialized repositories.
     */
    public Branch() {
        this.branchID = counter++;
        this.employeeRepo = new EmployeeRepository();
        this.roleRepo = new RoleRepository();
        this.userRepo = new UserRepository();
        this.weekRepo = new WeekRepository();

        Role shiftManager = new Role("Shift Manager");
        roleRepo.add(shiftManager);

        weekRepo.add(new Week());
    }

    public Branch(int branchID) {
        this.branchID = branchID;
        this.employeeRepo = new EmployeeRepository();
        this.roleRepo = new RoleRepository();
        this.userRepo = new UserRepository();
        this.weekRepo = new WeekRepository();
    }


    public int getBranchID() {
        return branchID;
    }

    public EmployeeRepository getEmployeeRepo() {
        return employeeRepo;
    }

    public RoleRepository getRoleRepo() {
        return roleRepo;
    }

    public UserRepository getUserRepo() {
        return userRepo;
    }

    public WeekRepository getWeekRepo() {
        return weekRepo;
    }
}
