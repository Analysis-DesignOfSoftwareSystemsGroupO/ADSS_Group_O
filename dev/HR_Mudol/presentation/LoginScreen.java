package HR_Mudol.presentation;

import HR_Mudol.DTO.BranchDTO;
import HR_Mudol.DTO.EmployeeDTO;
import HR_Mudol.DTO.UserDTO;
import HR_Mudol.Service.EmployeeService.EmployeeService;
import HR_Mudol.Service.ManagerService.HRService;
import HR_Mudol.Service.ShiftManagerService.ShiftManagerService;
import HR_Mudol.domain.Objects.Branch;
import HR_Mudol.domain.Objects.User;
import HR_Mudol.domain.Objects.Employee;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import TransportModule.transport_module.ITransportController;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class LoginScreen {

    private final List<BranchDTO> allBranches;

    public LoginScreen(List<BranchDTO> branches) {
        this.allBranches = branches;
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

            BranchDTO selectedBranch = selectBranch(scanner, allBranches);
            if (selectedBranch == null) {
                System.out.println("Invalid branch selection.");
                continue;
            }

            Branch curBranch = DTOToDomainMapper.fromDTO(selectedBranch);
            EmployeeService employeeService = new EmployeeService(curBranch);
            User user = curBranch.getUserRepo().getByCredentials(id, password);

            if (user == null || !(user.getUser() instanceof Employee emp)) {
                System.out.println("Invalid credentials. Please try again.\n");
                continue;
            }

            EmployeeDTO matched = DTOToDomainMapper.toDTO(emp);
            String level = user.getLevel().name();
            UserDTO userDTO = new UserDTO(matched.getEmployeeId(), level);

            launchMenuForUser(userDTO, matched, selectedBranch, curBranch);
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

    private void launchMenuForUser(UserDTO userDTO, EmployeeDTO employeeDTO, BranchDTO curBranchDTO, Branch curBranch) throws SQLException {
        ITransportController transportController = new TransportController(); // ← לפי השם שהגדרת למימוש
        if (userDTO.getLevel().equalsIgnoreCase("HRManager")) {
            HRService hrService = new HRService(curBranch,transportController);
            HRManagerMenu menu = new HRManagerMenu(hrService);
            if (menu.start(userDTO, employeeDTO, curBranchDTO)) return;
        } else if (userDTO.getLevel().equalsIgnoreCase("shiftManager")) {
            HRService hrService = new HRService(curBranch,transportController);
            ShiftManagerService shiftService = new ShiftManagerService(DTOToDomainMapper.toDTO(curBranch), hrService.getRoleController(),transportController);
            ShiftManagerMenu menu = new ShiftManagerMenu();
            if (menu.start(userDTO, employeeDTO, curBranchDTO)) return;
        } else {
            EmployeeService employeeService = new EmployeeService(curBranch);
            EmployeeMenu menu = new EmployeeMenu(employeeService);
            if (menu.start(userDTO, employeeDTO, curBranchDTO)) return;
        }
    }
}
