package HR_Mudol.DTO;

public class UserDTO {
    private int userId;
    private String level;

    public UserDTO(int userId, String level) {
        this.userId = userId;
        this.level = level;
    }

    public int getUserId() { return userId; }
    public String getLevel() { return level; }
}
