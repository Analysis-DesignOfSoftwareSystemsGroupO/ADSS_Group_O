package HR_Mudol.DTO;

public class RoleDTO {
    private int roleNumber;
    private String description;

    public RoleDTO(int roleNumber, String description) {
        this.roleNumber = roleNumber;
        this.description = description;
    }

    public int getRoleNumber() {
        return roleNumber;
    }

    public String getDescription() {
        return description;
    }
}
