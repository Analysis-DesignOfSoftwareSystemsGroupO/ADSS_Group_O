package HR_Mudol;

import HR_Mudol.DAO.BranchDAOImpl;
import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Controllers.EmployeeController;
import HR_Mudol.domain.Level;
import HR_Mudol.domain.Objects.Branch;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.Role;
import HR_Mudol.domain.Objects.User;
import HR_Mudol.domain.repository.BranchRepository;
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

            BranchRepository branchRepo = new BranchRepository(new BranchDAOImpl());

            if (choice.equals("1")) {
                // Load from DB
                allBranches = branchRepo.getAll();
                if (allBranches.isEmpty()) {
                    System.out.println("⚠ No branches found in the database.");
                    return;
                }
            } else if (choice.equals("2")) {
                // Start fresh
                Branch newBranch = new Branch("center", "Main Branch");
                addAdminUserIfNeeded(newBranch); // <- הוספת אדמין
                addDefaultRoles(newBranch);
                allBranches = new ArrayList<>();
                allBranches.add(newBranch);

                // אפשר לשקול לשמור אותו גם ל-DB כאן, אם תרצי לשמר את זה
                branchRepo.add(newBranch);
            } else {
                System.out.println("Invalid option.");
                return;
            }

            // נשתמש ב־branch הראשון לצורך יצירת ה־controller
            Branch primaryBranch = allBranches.get(0);

            DTOToDomainMapper mapper = new DTOToDomainMapper(
                    primaryBranch.getUserRepo(),
                    primaryBranch.getEmployeeRepo(),
                    primaryBranch.getRoleRepo(),
                    primaryBranch.getWeekRepo()
            );

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

    // הוספת יוזר admin אם לא קיים כבר
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

    // יצירת תפקידים בסיסיים (אופציונלי)
    private static void addDefaultRoles(Branch branch) {
        String[] roles = {"Cashier", "Driver", "Technician", "Warehouse", "Cleaner"};
        for (String name : roles) {
            branch.getRoleRepo().add(new Role(name));
        }
    }
}
