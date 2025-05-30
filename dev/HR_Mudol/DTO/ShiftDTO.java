package HR_Mudol.DTO;

import java.util.ArrayList;
import java.util.List;

public class ShiftDTO {
    private int shiftID;
    private String day;
    private String type;
    private String status;
    private int shiftManagerId;
    private List<EmployeeDTO> employees;
    private List<RoleDTO> necessaryRoles;
    private List<FilledRoleDTO> filledRoles;

    public ShiftDTO(int shiftID, String day, String type, String status, int shiftManagerId,
                    List<EmployeeDTO> employees,
                    List<RoleDTO> necessaryRoles,
                    List<FilledRoleDTO> filledRoles) {
        this.shiftID = shiftID;
        this.day = day;
        this.type = type;
        this.status = status;
        this.shiftManagerId = shiftManagerId;
        this.employees = employees;
        this.necessaryRoles = necessaryRoles;
        this.filledRoles = filledRoles;
    }

    public int getShiftID() {
        return shiftID;
    }

    public String getDay() {
        return day;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public int getShiftManagerId() {
        return shiftManagerId;
    }

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    public List<RoleDTO> getNecessaryRoles() {
        return necessaryRoles;
    }

    public List<FilledRoleDTO> getFilledRoles() {
        return filledRoles;
    }

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }

    public void setNecessaryRoles(List<RoleDTO> necessaryRoles) {
        this.necessaryRoles = necessaryRoles;
    }

    public void setFilledRoles(List<FilledRoleDTO> filledRoles) {
        this.filledRoles = filledRoles;
    }
    public List<Integer> getEmployeeIds() {
        List<Integer> ids = new ArrayList<>();
        for (EmployeeDTO e : employees) {
            ids.add(e.getEmployeeId());
        }
        return ids;
    }

}
