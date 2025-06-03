package HR_Mudol.domain.repository;

import HR_Mudol.DAO.*;
import HR_Mudol.DTO.*;
import HR_Mudol.domain.Controllers.DTOToDomainMapper;
import HR_Mudol.domain.Objects.*;
import com.sun.jdi.connect.spi.Connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

import java.util.LinkedList;
import java.util.List;

public class RoleRepository {
    private final List<Role> roles = new LinkedList<>();
    private final RoleDAOImpl roleDAO;

    public RoleRepository(RoleDAOImpl roleDAO) {
        this.roleDAO = roleDAO;
    }

    public void add(Role role) throws SQLException {
        roles.add(role);
        RoleDTO dto = toDTO(role);
        roleDAO.insert(dto);
    }

    public void updateDescription(Role role, String newDescription) {
        role.setDescription(newDescription);  // update in memory

        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        for (Employee emp : role.getRelevantEmployees()) {
            employeeDTOs.add(DTOToDomainMapper.toDTO(emp));
        }

        RoleDTO dto = new RoleDTO(role.getRoleNumber(),newDescription,employeeDTOs);
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
            role.removeEmployee(employee); // remove from memory
            roleDAO.removeEmployeeFromRole(employee.getEmpId(), role.getRoleNumber()); // DB
        }
    }

    public List<Employee> getAllRelevantEmployees(Role role, Branch branch) {
        List<EmployeeDTO> dtos = roleDAO.getEmployeesForRole(role.getRoleNumber(), branch.getBranchID());

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
                ))
                .collect(Collectors.toList());
    }

    public List<Role> getAllRoles() throws SQLException {
        List<RoleDTO> dtos = roleDAO.getAll();  // ← שליפה מה־DB
        return dtos.stream()
                .map(dto -> new Role( dto.getRoleNumber(), dto.getDescription()))
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

    public List<Integer> getAllEmployeeIDsWithRoles() {

        return roleDAO.getAllEmployeeIDsWithRoles();
    }

    public List<Role> getAll() throws SQLException {
        roles.clear();
        for (RoleDTO dto : roleDAO.getAll()) {
            roles.add(DTOToDomainMapper.fromDTO(dto));
        }
        return new ArrayList<>(roles);
    }

    public void addFromDTO(RoleDTO dto) throws SQLException {
        Role r = this.fromDTO(dto);
        this.roleDAO.insert(dto);
        this.roles.add(r);
    }

    private Role fromDTO(RoleDTO dto) {
        return new Role(dto.getDescription());
    }

    private RoleDTO toDTO(Role role) {

        List<EmployeeDTO> employeeDTOs = new ArrayList<>();
        for (Employee emp : role.getRelevantEmployees()) {
            employeeDTOs.add(DTOToDomainMapper.toDTO(emp));
        }

        return new RoleDTO(role.getRoleNumber(),role.getDescription(),employeeDTOs);
    }



    public Role getRoleByDescription(String description) throws SQLException {
        for (Role r : roles) {
            if (r.getDescription().equalsIgnoreCase(description)) {
                return r;
            }
        }

        RoleDTO dto = roleDAO.getByDescription(description);
        if (dto == null) return null;

        Role newRole = new Role(dto.getRoleNumber(), dto.getDescription());
        roles.add(newRole);
        return newRole;
    }
    public void deleteByDescription(String description) throws SQLException {
        roles.removeIf(r -> r.getDescription().equalsIgnoreCase(description));
        roleDAO.deleteByDescription(description);
    }



}
