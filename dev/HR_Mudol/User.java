package HR_Mudol;

public class User {
    private AbstractEmployee user;
    private Level level; // e.g., "Shift Manager", "HR manager"

    public User(AbstractEmployee user,  Level level) {
        this.user = user;
        this.level = level;
    }

    public boolean isManager() {
        return level == Level.HRManager;
    }

    public boolean isShiftManager() {
        return level == Level.shiftManager;
    }

    //is the user the same person as the employee whose data is being accessed?
    public boolean isSameEmployee(AbstractEmployee employee) {
        return this.user != null && this.user.equals(employee);
    }


}

