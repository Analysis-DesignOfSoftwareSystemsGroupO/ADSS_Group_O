package HR_Mudol.DTO;

import java.util.List;

public class BranchDTO {
    private int branchID;
    private String name;
    private List<EmployeeDTO> employees;
    private List<RoleDTO> roles;
    private List<WeekDTO> weeks;

    public BranchDTO(int branchID, String name, List<EmployeeDTO> employees, List<RoleDTO> roles, List<WeekDTO> weeks) {
        this.branchID = branchID;
        this.name = name;
        this.employees = employees;
        this.roles = roles;
        this.weeks = weeks;
    }

    public int getBranchID() {
        return branchID;
    }

    public void setBranchID(int branchID) {
        this.branchID = branchID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }

    public List<WeekDTO> getWeeks() {
        return weeks;
    }

    public void setWeeks(List<WeekDTO> weeks) {
        this.weeks = weeks;
    }
}
