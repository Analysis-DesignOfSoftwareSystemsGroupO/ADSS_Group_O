package HR_Mudol.DTO;

import java.util.List;

public class BranchDTO {
    private int branchID;
    private String name;
    private String district;

    private List<EmployeeDTO> employees;
    private List<RoleDTO> roles;
    private List<WeekDTO> weeks;

    public BranchDTO(int branchID, String name, String district,
                     List<EmployeeDTO> employees,
                     List<RoleDTO> roles,
                     List<WeekDTO> weeks) {
        this.branchID = branchID;
        this.name = name;
        this.district = district;
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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    public WeekDTO getCurrentWeekDTO() {
        if (weeks == null || weeks.isEmpty())
            return null;
        return weeks.get(weeks.size() - 1);
    }
}
