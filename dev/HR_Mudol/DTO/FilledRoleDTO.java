package HR_Mudol.DTO;

public class FilledRoleDTO {
    private int shiftId;
    private int employeeId;
    private int roleId;

    public FilledRoleDTO(int shiftId, int employeeId, int roleId) {
        this.shiftId = shiftId;
        this.employeeId = employeeId;
        this.roleId = roleId;
    }

    public int getShiftId() { return shiftId; }
    public int getEmployeeId() { return employeeId; }
    public int getRoleId() { return roleId; }
}
