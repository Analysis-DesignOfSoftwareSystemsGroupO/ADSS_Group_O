package HR_Mudol.domain.Objects;
import HR_Mudol.DAO.*;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.domain.repository.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Represents a Branch in the company, managing employees, users, weeks, and roles.
 */
public class Branch {

    // Static counter for tracking number of created branches
    static int counter = 0;

    // Branch ID assigned at creation
    private int branchID;
    private String name;
    private String district;


    // Repositories
    private EmployeeRepository employeeRepo;
    private RoleRepository roleRepo;
    private UserRepository userRepo;
    private WeekRepository weekRepo;
    private ConstraintRepository constraintRepository;

    //DAO
    private EmployeeDAOImpl employeeDAO;
    private RoleDAOImpl roleDAO;
    private ShiftDAOImpl shiftDAO;
    private UserDAOImpl userDAO;
    private ConstraintDAOImpl constraintDAO;

    /**
     * Constructs an empty Branch with initialized repositories.
     */
    public Branch(String district,String name) throws SQLException {
        this.branchID = counter++;

        this.employeeDAO=new EmployeeDAOImpl();
        this.roleDAO=new RoleDAOImpl();
        this.shiftDAO=new ShiftDAOImpl();
        this.userDAO=new UserDAOImpl();
        this.constraintDAO=new ConstraintDAOImpl();

        this.employeeRepo = new EmployeeRepository(employeeDAO,constraintDAO,branchID);
        this.roleRepo = new RoleRepository(roleDAO);
        this.userRepo = new UserRepository(userDAO,employeeRepo);
        this.weekRepo = new WeekRepository(shiftDAO);
        this.constraintRepository= new ConstraintRepository(constraintDAO);

        this.name=name;
        this.district=district;
        weekRepo.add(new Week());
    }

    public int getBranchID() {
        return this.branchID;
    }

    public String getDistrict(){
        return this.district;
    }

    public void setBranchID(int ID){
        this.branchID =ID;
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

    public String getName() {
        return name;
    }

    public void close() throws Exception {
        if (employeeDAO != null) employeeDAO.close();
        if (roleDAO != null) roleDAO.close();
        if (shiftDAO != null) shiftDAO.close();
        if (userDAO != null) userDAO.close();
        if (constraintDAO != null) constraintDAO.close();
    }

}
