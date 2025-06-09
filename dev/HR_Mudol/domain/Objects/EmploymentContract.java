package HR_Mudol.domain.Objects;

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
    public int getMinDayShift(Employee employee) {

        return minDayShift;
    }

    /**
     * Updates the minimum number of day shifts required per week.
     * Accessible only by the employee himself or a manager.
     */
    public void setMinDayShift(int minDayShift) {

        this.minDayShift = minDayShift;
    }

    /**
     * Updates the minimum number of evening shifts required per week.
     * Accessible only by the employee himself or a manager.
     */
    public void setMinEveninigShift(int minEveninigShift) {

        this.minEveninigShift = minEveninigShift;
    }

    /**
     * Updates the number of remaining sick days.
     * Accessible only by the employee himself or a manager.
     */
    public void setSickDays(int sickDays) {

        this.sickDays = sickDays;
    }

    /**
     * Updates the number of remaining vacation days.
     * Accessible only by the employee himself or a manager.
     */
    public void setDaysOff(int daysOff) {

        this.daysOff = daysOff;
    }

    /**
     * Returns the minimum number of evening shifts required per week.
     * Accessible only by the employee himself or a manager.
     */
    public int getMinEveninigShift( Employee employee) {

        return minEveninigShift;
    }

    /**
     * Returns the number of remaining sick days.
     * Accessible only by the employee himself or a manager.
     */
    public int getSickDays(Employee employee) {

        return sickDays;
    }

    /**
     * Returns the number of remaining vacation days.
     * Accessible only by the employee himself or a manager.
     */
    public int getDaysOff(Employee employee) {

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
