package HR_Mudol.DTO;

import java.util.Objects;

public class UserDTO {
    private int userId;     // ID of the AbstractEmployee
    private String level;   // Level name (e.g., "HR_MANAGER", "SHIFT_MANAGER")

    public UserDTO(int userId, String level) {
        this.userId = userId;
        this.level = level;
    }

    public int getUserId() { return userId; }

    public String getLevel() { return level; }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setLevel(String level) {
        this.level = level;
    }



    public boolean isShiftManager() {
        return "SHIFT_MANAGER".equalsIgnoreCase(level);
    }

    public boolean isManager() {
        return "HR_MANAGER".equalsIgnoreCase(level);
    }

    public boolean isTransportManager() {
        return "TRANSPORT_MANAGER".equalsIgnoreCase(level);
    }

    public boolean isRegularEmployee() {
        return "REGULAREMP".equalsIgnoreCase(level); // או השם התקני לפי Enum שלך
    }

}
