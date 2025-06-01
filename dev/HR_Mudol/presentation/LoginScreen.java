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

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class LoginScreen {

    private final BranchDTO branchDTO;
    private final User user;

    public LoginScreen(BranchDTO branchDTO, User user) {
        this.branchDTO = branchDTO;
        this.user = user;
    }

    public void start() throws SQLException {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (!user.getUser().getEmpPassword().equals(password)) {
            System.out.println("❌ Incorrect password.");
            return;
        }

        if (!(user.getUser() instanceof Employee emp)) {
            System.out.println("❌ Not a valid employee.");
            return;
        }

        EmployeeDTO matched = DTOToDomainMapper.toDTO(emp);
        String level = user.getLevel().name();
        UserDTO userDTO = new UserDTO(matched.getEmployeeId(), level);

        launchMenuForUser(userDTO, matched, branchDTO);

    }

    private void launchMenuForUser(UserDTO userDTO, EmployeeDTO employeeDTO, BranchDTO curBranch) throws SQLException {
        if (userDTO.getLevel().equalsIgnoreCase("HRManager")) {
            HRService hrService = new HRService(curBranch);
            HRManagerMenu menu = new HRManagerMenu(hrService);
            if (menu.start(userDTO, employeeDTO, curBranch)) return;
        } else if (userDTO.getLevel().equalsIgnoreCase("shiftManager")) {
            HRService hrService = new HRService(curBranch);
            ShiftManagerService shiftService = new ShiftManagerService(branchDTO, hrService.getRoleController());
            ShiftManagerMenu menu = new ShiftManagerMenu();
            if (menu.start(userDTO, employeeDTO, curBranch)) return;
        } else {
            EmployeeService employeeService = new EmployeeService(curBranch);
            EmployeeMenu menu = new EmployeeMenu(employeeService);
            if (menu.start(userDTO, employeeDTO, curBranch)) return;
        }
    }
}
