package HR_Mudol.DTO;

import java.util.ArrayList;
import java.util.List;

public class RoleDTO {
    private int roleNumber;
    private String description;
    private List<EmployeeDTO> relevantEmployees;

    public RoleDTO(int roleNumber, String description, List<EmployeeDTO> relevantEmployees) {
        this.roleNumber = roleNumber;
        this.description = description;
        this.relevantEmployees = relevantEmployees;
    }
    public RoleDTO(int roleNumber, String description) {
        this.roleNumber = roleNumber;
        this.description = description;
        this.relevantEmployees = new ArrayList<>();
    }

    public int getRoleNumber() {
        return roleNumber;
    }

    public String getDescription() {
        return description;
    }

    public List<EmployeeDTO> getRelevantEmployees() {
        return relevantEmployees;
    }

    public void setRelevantEmployees(List<EmployeeDTO> relevantEmployees) {
        this.relevantEmployees = relevantEmployees;
    }
}
