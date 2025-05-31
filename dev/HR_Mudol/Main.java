package HR_Mudol;

import HR_Mudol.DAO.*;
import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Controllers.EmployeeController;
import HR_Mudol.domain.Level;
import HR_Mudol.domain.Objects.*;
import HR_Mudol.domain.repository.*;
import HR_Mudol.presentation.LoginScreen;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            List<BranchDTO> allBranches;

            System.out.println("=== Welcome to the Workforce System ===");
            System.out.println("1. Load data from database");
            System.out.println("2. Start with a fresh (empty) system");
            System.out.print("Choose option [1/2]: ");
            String choice = scanner.nextLine().trim();

            // DAO instantiation
            IEmployeeDAO employeeDAO = new EmployeeDAOImpl();
            IConstraintDAO constraintDAO = new ConstraintDAOImpl();
            IUserDAO userDAO = new UserDAOImpl();
            IRoleDAO roleDAO = new RoleDAOImpl();
            IShiftDAO shiftDAO = new ShiftDAOImpl();

            EmployeeRepository empRepo = new EmployeeRepository(employeeDAO, constraintDAO);
            RoleRepository roleRepo = new RoleRepository((RoleDAOImpl) roleDAO);
            WeekRepository weekRepo = new WeekRepository((ShiftDAOImpl) shiftDAO);
            UserRepository userRepo = new UserRepository(userDAO, empRepo);

            BranchDAOImpl branchDAO = new BranchDAOImpl(employeeDAO, roleDAO);
            BranchRepository branchRepo = new BranchRepository(branchDAO);

            DTOToDomainMapper mapper;

            if (choice.equals("1")) {
                allBranches = branchRepo.getAll();
                if (allBranches.isEmpty()) {
                    System.out.println("⚠ No branches found in the database.");
                    return;
                }
            } else if (choice.equals("2")) {
                Branch newBranch = new Branch("center", "Main Branch");
                addAdminUserIfNeeded(newBranch);
                addDefaultRoles(newBranch);

                mapper = new DTOToDomainMapper(userRepo, empRepo, roleRepo, weekRepo);
                BranchDTO dto = mapper.toDTO(newBranch);
                allBranches = new ArrayList<>();
                allBranches.add(dto);
                branchRepo.add(dto);
            } else {
                System.out.println("Invalid option.");
                return;
            }

            mapper = new DTOToDomainMapper(userRepo, empRepo, roleRepo, weekRepo);
            Branch primaryBranch = mapper.fromDTO(allBranches.get(0));

            EmployeeController employeeController = new EmployeeController(
                    primaryBranch.getEmployeeRepo(),
                    primaryBranch.getConstraintRepo(),
                    mapper
            );

            LoginScreen login = new LoginScreen(allBranches, employeeController);
            login.start();

        } catch (SQLException e) {
            System.out.println("❌ System error: " + e.getMessage());
        }
    }

    private static void addAdminUserIfNeeded(Branch branch) throws SQLException {
        int adminId = 999999999;
        if (!branch.getUserRepo().exists(adminId)) {
            Employee admin = new Employee("System Admin", adminId, "admin123",
                    "IL0000000000", 20000, LocalDate.now(), 2, 2, 10, 10);
            User adminUser = new User(admin, Level.HRManager);
            branch.getEmployeeRepo().addFromDTO(admin);
            branch.getUserRepo().add(adminUser);
            System.out.println("✅ Admin user created with ID: " + adminId + ", password: admin123");
        }
    }

    private static void addDefaultRoles(Branch branch) {
        String[] roles = {"Cashier", "Driver", "Technician", "Warehouse", "Cleaner"};
        for (String name : roles) {
            branch.getRoleRepo().add(new Role(name));
        }
    }
}
