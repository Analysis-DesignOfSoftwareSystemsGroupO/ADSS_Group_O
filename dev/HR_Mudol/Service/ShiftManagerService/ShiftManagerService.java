package HR_Mudol.Service.ShiftManagerService;

import HR_Mudol.DTO.*;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Controllers.ShiftController;
import HR_Mudol.domain.Controllers.IRoleController;
import HR_Mudol.domain.WeekDay;
import HR_Mudol.domain.ShiftType;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ShiftManagerService implements IShiftManagerService {

    private BranchDTO branchDTO;
    private final ShiftController shiftController;
    private final Scanner scanner = new Scanner(System.in);

    public ShiftManagerService(BranchDTO branchDTO, IRoleController roleController) throws SQLException {
        this.branchDTO=branchDTO;
        this.shiftController = new ShiftController(branchDTO, roleController);
    }

    @Override
    public void close() {
       shiftController.close();
    }

    @Override
    public void removeEmployeeFromShift(UserDTO theCaller) {
        ShiftDTO shiftDTO = chooseShiftDTO();
        if (shiftDTO == null) return;
        try {
            shiftController.removeEmployeeFromShift(theCaller, shiftDTO);
        } catch (SQLException e) {
            System.out.println("Error removing employee from shift: " + e.getMessage());
        }
    }

    @Override
    public void addEmployeeToShift(UserDTO theCaller) throws SQLException {
        if (!theCaller.getLevel().equals("HRManager") && !theCaller.getLevel().equals("shiftManager")) {
            System.out.println("Access denied. Only shift managers can add employees to shifts.");
            return;
        }

        ShiftDTO shiftDTO = chooseShiftDTO();
        if (shiftDTO == null) return;

        if (shiftDTO.getShiftManagerId() != theCaller.getUserId()) {
            System.out.println("Access denied. You are not the shift manager of this shift.");
            return;
        }

        List<EmployeeDTO> employeeDTOs = shiftController.getAllEmployeesAsDTOs();
        for (int i = 0; i < employeeDTOs.size(); i++) {
            EmployeeDTO e = employeeDTOs.get(i);
            System.out.println((i + 1) + ". " + e.getFullName() + " (ID: " + e.getEmployeeId() + ")");
        }

        System.out.print("Select employee to add: ");
        String empInput = scanner.nextLine().trim();
        int empIndex;

        try {
            empIndex = Integer.parseInt(empInput) - 1;
            if (empIndex < 0 || empIndex >= employeeDTOs.size()) {
                System.out.println("Invalid selection.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        EmployeeDTO toAdd = employeeDTOs.get(empIndex);

        if (shiftDTO.getNecessaryRoles().isEmpty()) {
            System.out.println("All roles are already assigned in this shift.");
            return;
        }

        RoleDTO roleDTO = chooseRoleDTOFromList(shiftDTO.getNecessaryRoles());
        if (roleDTO == null) return;

        try {
            shiftController.assignEmployeeToShift(theCaller, shiftDTO, toAdd, roleDTO);
        } catch (SQLException e) {
            System.out.println("Error assigning employee to shift: " + e.getMessage());
        }
    }

    @Override
    public void transferCancellationCard(UserDTO theCaller) {
        if (!theCaller.getLevel().equals("SHIFT_MANAGER")) {
            System.out.println("Access denied. Only shift managers can transfer the cancellation card.");
            return;
        }

        System.out.println("Item canceled.");
    }

    private ShiftDTO chooseShiftDTO() {
        WeekDay day = chooseDay();
        ShiftType type = chooseShiftType();

        List<ShiftDTO> shiftDTOs = shiftController.getAllShiftDTOs();
        for (ShiftDTO dto : shiftDTOs) {
            if (dto.getDay().equals(day.name()) && dto.getType().equals(type.name())) {
                return dto;
            }
        }
        return null;
    }

    private WeekDay chooseDay() {
        System.out.println("Select day of the week:");
        WeekDay[] days = WeekDay.values();
        for (int i = 0; i < days.length; i++) {
            System.out.println((i + 1) + ". " + days[i]);
        }

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= days.length) {
                return days[choice - 1];
            }
        } catch (NumberFormatException ignored) {}
        System.out.println("Invalid day selection.");
        return null;
    }

    private ShiftType chooseShiftType() {
        System.out.println("Select shift type:");
        System.out.println("1. Morning");
        System.out.println("2. Evening");
        String input = scanner.nextLine().trim();
        return switch (input) {
            case "1" -> ShiftType.MORNING;
            case "2" -> ShiftType.EVENING;
            default -> {
                System.out.println("Invalid shift type.");
                yield null;
            }
        };
    }

    private RoleDTO chooseRoleDTOFromList(List<RoleDTO> roles) {
        System.out.println("Available roles for this shift:");
        for (int i = 0; i < roles.size(); i++) {
            System.out.println((i + 1) + ". " + roles.get(i).getDescription());
        }

        try {
            int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (index >= 0 && index < roles.size()) {
                return roles.get(index);
            }
        } catch (NumberFormatException ignored) {}

        System.out.println("Invalid role number.");
        return null;
    }
}
