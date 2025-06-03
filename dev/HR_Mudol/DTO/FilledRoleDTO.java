package HR_Mudol.DTO;

public class FilledRoleDTO {
    private int shiftId;
    private long employeeId;
    private int roleId;

    public FilledRoleDTO(int shiftId, long employeeId, int roleId) {
        this.shiftId = shiftId;
        this.employeeId = employeeId;
        this.roleId = roleId;
    }

    public int getShiftId() { return shiftId; }
    public long getEmployeeId() { return employeeId; }
    public int getRoleId() { return roleId; }
}
