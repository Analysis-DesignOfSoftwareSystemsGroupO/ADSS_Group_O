package HR_Mudol.domain.repository;

import HR_Mudol.DTO.RoleDTO;
import HR_Mudol.DTO.RoleMapper;
import HR_Mudol.domain.Role;

import java.util.LinkedList;
import java.util.List;

public class RoleRepository {
    private final List<Role> roles = new LinkedList<>();

    public void add(Role role) {
        roles.add(role);
    }

    public List<Role> getAll() {
        return new LinkedList<>(roles);
    }

    public void clear() {
        roles.clear();
    }
    public void addFromDTO(RoleDTO dto) {
        Role r = RoleMapper.fromDTO(dto);
        roles.add(r);
    }
    public Role getById(int roleId) {
        for (Role role : roles) {
            if (role.getRoleNumber() == roleId) {
                return role;
            }
        }
        throw new IllegalArgumentException("Role with ID " + roleId + " not found.");
    }

}
