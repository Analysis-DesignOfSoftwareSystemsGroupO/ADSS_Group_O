package HR_Mudol.domain.Objects;
import HR_Mudol.DAO.RoleDAOImpl;
import HR_Mudol.DAO.ShiftDAOImpl;
import HR_Mudol.domain.repository.*;

/**
 * Represents a Branch in the company, managing employees, users, weeks, and roles.
 */
public class Branch {

    // Static counter for tracking number of created branches
    static int counter = 0;

    // Branch ID assigned at creation
    private int branchID;
    private String district;
    private String name;

    // Repositories
    private EmployeeRepository employeeRepo;
    private RoleRepository roleRepo;
    private UserRepository userRepo;
    private WeekRepository weekRepo;
    private ConstraintRepository constraintRepository;

    /**
     * Constructs an empty Branch with initialized repositories.
     */
    public Branch(String district,String name) {
        this.branchID = counter++;
        this.employeeRepo = new EmployeeRepository();
        this.roleRepo = new RoleRepository(new RoleDAOImpl ());
        this.userRepo = new UserRepository();
        this.weekRepo = new WeekRepository(new ShiftDAOImpl);
        this.constraintRepository= new ConstraintRepository();

        this.name=name;
        this.district=district;

        Role shiftManager = new Role("Shift Manager");
        Role Driver = new Role("Driver");
        roleRepo.add(shiftManager);
        roleRepo.add(Driver);
        weekRepo.add(new Week());
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

    public ConstraintRepository getConstraintRepo() {return constraintRepository; }
}
