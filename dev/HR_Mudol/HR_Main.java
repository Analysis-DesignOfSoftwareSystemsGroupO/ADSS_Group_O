package HR_Mudol;

import HR_Mudol.DTO.*;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.repository.BranchRepository;
import HR_Mudol.Service.ManagerService.HRService;
import HR_Mudol.presentation.LoginScreen;

import java.util.*;

public class HR_Main {

    public static void HR_main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            /*
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
            */


            // Branch repository
            BranchRepository branchRepo = new BranchRepository();
            List<BranchDTO> branches = branchRepo.getAllBranches();

            boolean keepRunning = true;

            while (keepRunning) {
                System.out.println("\nAvailable Branches:");
                for (int i = 0; i < branches.size(); i++) {
                    System.out.printf("%d. %s\n", i+1 , branches.get(i).getName());
                }

                BranchDTO selectedBranch;
                UserDTO user;

                while (true) {
                    try {
                        System.out.print("Select your branch by number: ");
                        int branchIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;

                        if (branchIndex < 0 || branchIndex >= branches.size()) {
                            System.out.println("Invalid branch selection.");
                            continue;
                        }

                        selectedBranch = branches.get(branchIndex);

                        System.out.print("Enter your employee ID: ");
                        int empId = Integer.parseInt(scanner.nextLine().trim());

                        HRService hrService = new HRService(selectedBranch);
                        if (!hrService.isEmployeeInBranch(empId, selectedBranch.getBranchID())) {
                            System.out.println("❌ You are not associated with this branch. Please try again.");
                            continue;
                        }

                        user = hrService.getUserById(empId);
                        if (user == null) {
                            System.out.println("❌ User not found.");
                            continue;
                        }

                        break;

                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }

                LoginScreen login = new LoginScreen(selectedBranch, DTOToDomainMapper.fromDTO(user));
                login.start();

                System.out.print("🔄 Do you want to log in again? [y/n]: ");
                String again = scanner.nextLine().trim().toLowerCase();
                if (!again.equals("y")) {
                    keepRunning = false;
                }
            }

            System.out.println("👋 Exiting the Workforce System. Goodbye!");

        } catch (Exception ex) {
            System.out.println("❌ Initialization failed: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
