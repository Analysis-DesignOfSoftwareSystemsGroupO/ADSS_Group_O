package HR_Mudol.domain;

/**
 * Represents an employment contract for an employee,
 * including minimum shift requirements and leave balances.
 */
public class EmploymentContract {

    // Minimum number of day shifts per week
    private int minDayShift;

    // Minimum number of evening shifts per week
    private int minEveninigShift;

    // Number of remaining sick days
    private int sickDays;

    // Number of remaining vacation days
    private int daysOff;

    // The employee who owns this contract
    private Employee owner;

    /**
     * Constructs a new EmploymentContract with the specified details.
     */
    public EmploymentContract(int minDayShift, int minEveninigShift, int sickDays, int daysOff, Employee employee) {
        this.minDayShift = minDayShift;
        this.minEveninigShift = minEveninigShift;
        this.sickDays = sickDays;
        this.daysOff = daysOff;
        this.owner = employee;
    }

    /**
     * Returns the employee who owns this contract.
     */
    public Employee getOwner() {
        return owner;
    }

    /**
     * Returns the minimum number of day shifts required per week.
     * Accessible only by the employee himself or a manager.
     */
    public int getMinDayShift(User caller, Employee employee) {
        if (!caller.isManager() && !caller.isSameEmployee(employee)) {
            throw new SecurityException("Access denied");
        }
        return minDayShift;
    }

    /**
     * Updates the minimum number of day shifts required per week.
     * Accessible only by the employee himself or a manager.
     */
    public void setMinDayShift(User caller, int minDayShift) {
        if (!caller.isManager() && !caller.isSameEmployee(this.getOwner())) {
            throw new SecurityException("Access denied");
        }
        this.minDayShift = minDayShift;
    }

    /**
     * Updates the minimum number of evening shifts required per week.
     * Accessible only by the employee himself or a manager.
     */
    public void setMinEveninigShift(User caller, int minEveninigShift) {
        if (!caller.isManager() && !caller.isSameEmployee(this.getOwner())) {
            throw new SecurityException("Access denied");
        }
        this.minEveninigShift = minEveninigShift;
    }

    /**
     * Updates the number of remaining sick days.
     * Accessible only by the employee himself or a manager.
     */
    public void setSickDays(User caller, int sickDays) {
        if (!caller.isManager() && !caller.isSameEmployee(this.getOwner())) {
            throw new SecurityException("Access denied");
        }
        this.sickDays = sickDays;
    }

    /**
     * Updates the number of remaining vacation days.
     * Accessible only by the employee himself or a manager.
     */
    public void setDaysOff(User caller, int daysOff) {
        if (!caller.isManager() && !caller.isSameEmployee(this.getOwner())) {
            throw new SecurityException("Access denied");
        }
        this.daysOff = daysOff;
    }

    /**
     * Returns the minimum number of evening shifts required per week.
     * Accessible only by the employee himself or a manager.
     */
    public int getMinEveninigShift(User caller, Employee employee) {
        if (!caller.isManager() && !caller.isSameEmployee(employee)) {
            throw new SecurityException("Access denied");
        }
        return minEveninigShift;
    }

    /**
     * Returns the number of remaining sick days.
     * Accessible only by the employee himself or a manager.
     */
    public int getSickDays(User caller, Employee employee) {
        if (!caller.isManager() && !caller.isSameEmployee(employee)) {
            throw new SecurityException("Access denied");
        }
        return sickDays;
    }

    /**
     * Returns the number of remaining vacation days.
     * Accessible only by the employee himself or a manager.
     */
    public int getDaysOff(User caller, Employee employee) {
        if (!caller.isManager() && !caller.isSameEmployee(employee)) {
            throw new SecurityException("Access denied");
        }
        return daysOff;
    }

    /**
     * Returns a string representation of the employment contract details.
     */
    @Override
    public String toString() {
        return "  Contract details:" +
                "\n  Minimum evening shifts per week: " + this.minEveninigShift +
                "\n  Minimum day shifts per week: " + this.minDayShift +
                "\n  Sick days remaining: " + this.sickDays +
                "\n  Vacation days remaining: " + this.daysOff;
    }
}
