package HR_Mudol.DTO;

import HR_Mudol.domain.Objects.Role;

public class RoleMapper {
    public static RoleDTO toDTO(Role role) {
        return new RoleDTO(role.getRoleNumber(), role.getDescription());
    }

    public static Role fromDTO(RoleDTO dto) {
        return new Role(dto.getDescription(), dto.getRoleNumber());
    }
}
