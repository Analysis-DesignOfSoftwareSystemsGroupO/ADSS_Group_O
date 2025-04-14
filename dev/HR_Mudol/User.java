package HR_Mudol;

public class User {
    private AbstractEmployee user;
    private String level; // e.g., "EMPLOYEE", "MANAGER"

    public User(AbstractEmployee user, String level) {
        this.user = user;
        this.level = level;
    }

    public boolean isManager() {
        return "HR manager".equalsIgnoreCase(level);
    }

    //is the user the same person as the employee whose data is being accessed?
    public boolean isSameEmployee(AbstractEmployee employee) {
        return this.user != null && this.user.equals(employee);
    }

    public boolean isShiftManager() {
        return "Shift Manager".equalsIgnoreCase(level);
    }

}

