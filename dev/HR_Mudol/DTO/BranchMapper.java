package HR_Mudol.DTO;

import HR_Mudol.DTO.*;
import HR_Mudol.domain.Branch;
import HR_Mudol.domain.repository.*;

import java.util.List;
import java.util.stream.Collectors;

public class BranchMapper {

    public static BranchDTO toDTO(Branch branch) {
        return new BranchDTO(
                branch.getBranchID(),
                branch.getEmployeeRepo().getAll().stream().map(EmployeeMapper::toDTO).collect(Collectors.toList()),
                branch.getRoleRepo().getAll().stream().map(RoleMapper::toDTO).collect(Collectors.toList()),
                List.of()
        );
    }

    public static Branch fromDTO(BranchDTO dto) {
        Branch b = new Branch(dto.getBranchID());
        for (EmployeeDTO e : dto.getEmployees()) {
            b.getEmployeeRepo().addFromDTO(e);
        }
        for (RoleDTO r : dto.getRoles()) {
            b.getRoleRepo().addFromDTO(r);
        }
        return b;
    }
}
