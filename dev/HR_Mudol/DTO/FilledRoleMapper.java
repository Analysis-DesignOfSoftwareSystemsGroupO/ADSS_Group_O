package HR_Mudol.DTO;

import HR_Mudol.domain.*;
import HR_Mudol.domain.repository.EmployeeRepository;
import HR_Mudol.domain.repository.RoleRepository;

public class FilledRoleMapper {

    public static FilledRoleDTO toDTO(FilledRole role, int shiftId) {
        return new FilledRoleDTO(
                shiftId,
                role.getEmployee().getEmpId(),
                role.getRole().getRoleNumber()
        );
    }

    public static FilledRole fromDTO(FilledRoleDTO dto, EmployeeRepository employeeRepo, RoleRepository roleRepo) {
        Employee employee = employeeRepo.getById(dto.getEmployeeId());
        Role role = roleRepo.getById(dto.getRoleId());
        return new FilledRole(employee, role);
    }
}
