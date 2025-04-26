package HR_Mudol.domain;

/**
 * Represents a system user, wrapping an employee and their access level.
 */
public class User {
    private AbstractEmployee user; // Reference to the actual employee object
    private Level level;           // The access level (HR manager, Shift manager, etc.)

    /**
     * Constructs a new user with the specified employee and access level.
     *
     * @param user The employee associated with the user.
     * @param level The access level of the user.
     * @throws NullPointerException if user or level is null.
     */
    public User(AbstractEmployee user, Level level) {
        if (user == null || level == null) {
            throw new NullPointerException("User and level cannot be null");
        }
        this.user = user;
        this.level = level;
    }

    /**
     * Sets the access level for this user.
     * Can only be done by an HR manager.
     *
     * @param caller The user requesting the level change.
     * @param level The new access level to assign.
     * @throws SecurityException if caller is not a manager.
     * @throws NullPointerException if level is null.
     */
    public void setLevel(User caller, Level level) {
        if (!caller.isManager()) {
            throw new SecurityException("Access denied");
        }
        if (level == null) {
            throw new NullPointerException("Level cannot be null");
        }
        this.level = level;
    }

    /**
     * Returns the access level of this user.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Returns the underlying employee associated with this user.
     */
    public AbstractEmployee getUser() {
        return user;
    }

    /**
     * Returns true if the user is an HR manager.
     */
    public boolean isManager() {
        return level == Level.HRManager;
    }

    /**
     * Returns true if the user is a shift manager.
     */
    public boolean isShiftManager() {
        return level == Level.shiftManager;
    }

    /**
     * Returns true if the given employee is the same as the user’s employee.
     *
     * @param employee The employee to compare.
     */
    public boolean isSameEmployee(AbstractEmployee employee) {
        return this.user != null && employee != null && this.user.getEmpId() == employee.getEmpId();
    }
}
