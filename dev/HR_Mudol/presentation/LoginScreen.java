package HR_Mudol.presentation;

import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.domain.Controllers.IEmployeeController;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class LoginScreen {

    private final IEmployeeController employeeController;
    private final List<BranchDTO> allBranches;

    public LoginScreen(List<BranchDTO> branches, IEmployeeController employeeController) {
        this.allBranches = branches;
        this.employeeController = employeeController;
    }

    public void start() throws SQLException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Enter employee ID: ");
            String idInput = scanner.nextLine();
            int id;

            try {
                id = Integer.parseInt(idInput);
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Please enter numbers only.\n");
                continue;
            }

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // חיפוש העובד המתאים לפי סיסמה
            EmployeeDTO matched = null;
            BranchDTO userBranch = null;

            for (BranchDTO branch : allBranches) {
                for (EmployeeDTO e : branch.getEmployees()) {
                    if (e.getEmployeeId() == id && e.getPassword().equals(password)) {
                        matched = e;
                        userBranch = branch;
                        break;
                    }
                }
                if (matched != null) break;
            }

            if (matched == null) {
                System.out.println("Invalid credentials. Please try again.\n");
                continue;
            }

            BranchDTO selectedBranch = selectBranch(scanner, allBranches);
            if (selectedBranch == null) {
                System.out.println("Invalid branch selection.");
                continue;
            }

            // שליפת רמת גישה אמיתית מהמערכת
            String level = employeeController.getUserLevel(matched.getEmployeeId());
            UserDTO userDTO = new UserDTO(matched.getEmployeeId(), level);

            launchMenuForUser(userDTO, matched, selectedBranch);
        }
    }

    private BranchDTO selectBranch(Scanner scanner, List<BranchDTO> userBranches) {
        System.out.println("Select your branch:");
        for (int i = 0; i < userBranches.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, userBranches.get(i).getName());
        }

        System.out.print("Enter choice: ");
        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index >= 0 && index < userBranches.size()) {
                return userBranches.get(index);
            }
        } catch (NumberFormatException ignored) {}
        return null;
    }

    private void launchMenuForUser(UserDTO userDTO, EmployeeDTO employeeDTO, BranchDTO curBranch) throws SQLException {
        if (userDTO.getLevel().equalsIgnoreCase("HRManager")) {
            HRManagerMenu menu = new HRManagerMenu();
            if (menu.start(userDTO, employeeDTO, curBranch)) return;
        } else if (userDTO.getLevel().equalsIgnoreCase("shiftManager")) {
            ShiftManagerMenu menu = new ShiftManagerMenu();
            if (menu.start(userDTO, employeeDTO, curBranch)) return;
        } else {
            EmployeeMenu menu = new EmployeeMenu(employeeController);
            if (menu.start(userDTO, employeeDTO, curBranch)) return;
        }
    }
}
