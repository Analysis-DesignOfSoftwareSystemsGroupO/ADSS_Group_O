package HR_Mudol;

import HR_Mudol.DAO.*;
import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DataBase.DatabaseInitializer;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Level;
import HR_Mudol.domain.Objects.Branch;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.User;
import HR_Mudol.domain.repository.*;
import HR_Mudol.Service.EmployeeService.EmployeeService;
import HR_Mudol.Service.ManagerService.HRService;
import HR_Mudol.presentation.LoginScreen;

import java.time.LocalDate;
import java.util.*;

public class HR_Main {

    public static void HR_main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            BranchDTO primaryBranchDTO;
            int branchID;

            System.out.println("=== Welcome to the Workforce System ===");
            System.out.println("1. Load data from database");
            System.out.println("2. Start with a fresh (empty) system");
            System.out.print("Choose option [1/2]: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                DatabaseInitializer.initialize(true);
            } else if (choice.equals("2")) {
                DatabaseInitializer.initialize(false);
            } else {
                System.out.println("Invalid option.");
                return;
            }

            // DAO instantiation
            IEmployeeDAO employeeDAO = new EmployeeDAOImpl();
            IConstraintDAO constraintDAO = new ConstraintDAOImpl();
            IUserDAO userDAO = new UserDAOImpl();
            IRoleDAO roleDAO = new RoleDAOImpl();
            IShiftDAO shiftDAO = new ShiftDAOImpl();
            IBranchDAO branchDAO = new BranchDAOImpl();

            // Repository for loading branches
            BranchRepository branchRepo = new BranchRepository(branchDAO);
            Branch selectedBranch;

            if (choice.equals("1")) {
                Collection<Branch> branches = branchRepo.getAllBranches();
                if (branches.isEmpty()) {
                    System.out.println("⚠ No branches found in the database.");
                    return;
                }

                List<Branch> branchList = new ArrayList<>(branches);

                // ✅ הוספת אדמין לכל הסניפים
                addAdminUserIfNeeded(branchList);

                System.out.println("\nAvailable Branches:");
                for (int i = 0; i < branchList.size(); i++) {
                    System.out.printf("%d. %s\n", i + 1, branchList.get(i).getName());
                }

                selectedBranch = null;
                User user = null;

                while (true) {
                    System.out.print("Select your branch by number: ");
                    String input = scanner.nextLine().trim();

                    try {
                        int branchIndex = Integer.parseInt(input) - 1;
                        if (branchIndex < 0 || branchIndex >= branchList.size()) {
                            System.out.println("Invalid branch selection.");
                            continue;
                        }

                        selectedBranch = branchList.get(branchIndex);

                        System.out.print("Enter your employee ID: ");
                        int userId = Integer.parseInt(scanner.nextLine().trim());

                        if (employeeDAO.isEmployeeInBranch(userId, selectedBranch.getBranchID())) {
                            user = selectedBranch.getUserRepo().getByEmployeeId(userId);
                            break;
                        } else {
                            System.out.println("❌ You are not associated with this branch. Please choose again.");
                        }

                    } catch (Exception e) {
                        System.out.println("Invalid input. Please try again.");
                    }
                }

            } else {
                selectedBranch = new Branch("center", "Main Branch");
                addAdminUserIfNeeded(Collections.singletonList(selectedBranch));
                BranchDTO newBranchDTO = DTOToDomainMapper.toDTO(selectedBranch);
                branchRepo.add(newBranchDTO);
            }

            // Extract branchID
            branchID = selectedBranch.getBranchID();
            primaryBranchDTO = DTOToDomainMapper.toDTO(selectedBranch);

            // Repositories with branchID
            EmployeeRepository empRepo = new EmployeeRepository(employeeDAO, constraintDAO, branchID);
            RoleRepository roleRepo = new RoleRepository((RoleDAOImpl) roleDAO);
            WeekRepository weekRepo = new WeekRepository((ShiftDAOImpl) shiftDAO);
            UserRepository userRepo = new UserRepository(userDAO, empRepo);

            // Mapper
            DTOToDomainMapper mapper = new DTOToDomainMapper(userRepo, empRepo, roleRepo, weekRepo);

            // Service Layer
            EmployeeService employeeService = new EmployeeService(selectedBranch);
            HRService hrService = new HRService(selectedBranch);

            // Presentation Layer
            List<BranchDTO> allDTOs = new ArrayList<>();
            allDTOs.add(primaryBranchDTO);

            LoginScreen login = new LoginScreen(allDTOs);
            login.start();

        } catch (Exception ex) {
            System.out.println("❌ Initialization failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void addAdminUserIfNeeded(Collection<Branch> branches) throws Exception {
        int adminId = 999999999;

        for (Branch branch : branches) {
            if (!branch.getUserRepo().exists(adminId)) {
                Employee admin = new Employee("System Admin", adminId, "admin123",
                        "IL0000000000", 20000, LocalDate.now(), 2, 2, 10, 10);
                User adminUser = new User(admin, Level.HRManager);
                branch.getEmployeeRepo().addFromDTO(admin);
                branch.getUserRepo().add(adminUser);
                System.out.println("✅ Admin user added to branch: " + branch.getName());
            }
        }
    }

}
