package HR_Mudol.domain.repository;

import HR_Mudol.DAO.*;
import HR_Mudol.DTO.*;
import HR_Mudol.domain.Objects.*;
import java.util.stream.Collectors;

import java.util.LinkedList;
import java.util.List;

public class RoleRepository {
    private final List<Role> roles = new LinkedList<>();
    private final RoleDAOImpl roleDAO;

    public RoleRepository(RoleDAOImpl roleDAO) {
        this.roleDAO = roleDAO;
    }

    public void add(Role role) {
        roles.add(role);
        RoleDTO dto = new RoleDTO(role.getRoleNumber(), role.getDescription());
        roleDAO.insert(dto);
    }

    public void updateDescription(Role role, String newDescription) {
        role.setDescription(newDescription);  // update in memory

        RoleDTO dto = new RoleDTO(role.getRoleNumber(), newDescription);
        roleDAO.updateDescription(dto);       // update in DB
    }

    public void assignEmployeeToRole(Employee employee, Role role) {
        //RAM
        role.addNewEmployee(employee);

        //DB
        roleDAO.assignEmployeeToRole(employee.getEmpId(), role.getRoleNumber());
    }

    public void removeEmployeeFromAllRoles(Employee employee) {

        for (Role role : roles) {
            if (role.getRelevantEmployees().contains(employee)) {
                role.removeEmployee(employee);  // removes from memory
                roleDAO.removeEmployeeFromRole(employee.getEmpId(), role.getRoleNumber()); // removes from DB
            }
        }
    }

    public void removeEmployeeFromRole(Employee employee, Role role) {
        if (role.getRelevantEmployees().contains(employee)) {
            roleDAO.removeEmployeeFromRole(employee.getEmpId(), role.getRoleNumber()); // DB
        }
    }

    public List<Employee> getAllRelevantEmployees() {
        List<EmployeeDTO> dtos = roleDAO.getAllEmployeeDTOsWithRoles();
        return dtos.stream()
                .map(dto -> new Employee(
                        dto.getFullName(),
                        dto.getEmployeeId(),
                        dto.getPassword(),
                        dto.getBankAccount(),
                        dto.getSalary(),
                        dto.getStartDate(),
                        dto.getMinDayShift(),
                        dto.getMinEveningShift(),
                        dto.getSickDays(),
                        dto.getDaysOff()
                )).collect(Collectors.toList());
    }

    public List<Role> getAllRoles() {
        List<RoleDTO> dtos = roleDAO.getAll();  // ← שליפה מה־DB
        return dtos.stream()
                .map(dto -> new Role( dto.getDescription()))
                .collect(Collectors.toList());
    }

    public Role getRoleByNumber(int roleNumber) {
        // קודם לבדוק בזיכרון
        for (Role r : roles) {
            if (r.getRoleNumber() == roleNumber)
                return r;
        }

        // אם לא נמצא בזיכרון – שלוף מה־DB
        RoleDTO dto = roleDAO.getByNumber(roleNumber);
        if (dto == null) return null;

        Role newRole = new Role(dto.getDescription());
        roles.add(newRole); // הוספה לזיכרון
        return newRole;
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



}
