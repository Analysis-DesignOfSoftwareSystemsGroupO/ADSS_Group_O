package HR_Mudol;

import HR_Mudol.DAO.*;
import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DataBase.DatabaseInitializer;
import HR_Mudol.DataBase.PostgresConnection;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Level;
import HR_Mudol.domain.Objects.Branch;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Objects.User;
import HR_Mudol.domain.repository.BranchRepository;
import HR_Mudol.presentation.LoginScreen;

import java.time.LocalDate;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("=== Welcome to the Workforce System ===");
            System.out.println("1. Load data from database");
            System.out.println("2. Start with a fresh (empty) system");
            System.out.print("Choose option [1/2]: ");
            String choice = scanner.nextLine().trim();

            boolean loadFromDatabase = choice.equals("1");
            if (!loadFromDatabase && !choice.equals("2")) {
                System.out.println("Invalid option.");
                return;
            }

            DatabaseInitializer.initialize(loadFromDatabase);

            // DAO and Repositories
            IBranchDAO branchDAO = new BranchDAOImpl();
            BranchRepository branchRepo = new BranchRepository(branchDAO);
            Branch selectedBranch;
            User user = null;

            if (loadFromDatabase) {
                List<Branch> branches = new ArrayList<>(branchRepo.getAllBranches());
                if (branches.isEmpty()) {
                    System.out.println("⚠ No branches found in the database.");
                    return;
                }

                addAdminUserIfNeeded(branches);

                System.out.println("\nAvailable Branches:");
                for (int i = 0; i < branches.size(); i++) {
                    System.out.printf("%d. %s\n", i + 1, branches.get(i).getName());
                }

                while (true) {
                    System.out.print("Select your branch by number: ");
                    try {
                        int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
                        if (index < 0 || index >= branches.size()) {
                            System.out.println("Invalid branch selection.");
                            continue;
                        }

                        selectedBranch = branches.get(index);

                        System.out.print("Enter your employee ID: ");
                        int empId = Integer.parseInt(scanner.nextLine().trim());

                        if (!new EmployeeDAOImpl().isEmployeeInBranch(empId, selectedBranch.getBranchID())) {
                            System.out.println("❌ You are not associated with this branch. Please try again.");
                            continue;
                        }

                        user = selectedBranch.getUserRepo().getByEmployeeId(empId);
                        break;

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

            } else {
                selectedBranch = new Branch("center", "Main Branch");
                addAdminUserIfNeeded(Collections.singletonList(selectedBranch));
                branchRepo.add(DTOToDomainMapper.toDTO(selectedBranch));
            }

            BranchDTO selectedBranchDTO = DTOToDomainMapper.toDTO(selectedBranch);
            LoginScreen login = new LoginScreen(selectedBranchDTO, user);
            login.start();
            PostgresConnection.closeConnection();


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
