package HR_Mudol.domain;

public class Constraint {
    private String explanation;
    private WeekDay day;
    private ShiftType type;

    /**
     * Constructs a new Constraint object with explanation, day, and shift type.
     *
     * @param explanation The explanation for the constraint (must not be null).
     * @param day The day the constraint applies to (must not be null).
     * @param type The shift type the constraint applies to (must not be null).
     * @throws NullPointerException if any of the parameters are null.
     */
    public Constraint(String explanation, WeekDay day, ShiftType type) {
        if (explanation == null) {
            throw new NullPointerException("Explanation cannot be null.");
        }
        if (day == null) {
            throw new NullPointerException("Day cannot be null.");
        }
        if (type == null) {
            throw new NullPointerException("Shift type cannot be null.");
        }
        this.explanation = explanation;
        this.day = day;
        this.type = type;
    }

    public String getExplanation() {
        return explanation;
    }

    /**
     * Sets a new explanation for the constraint.
     * Only the employee himself or a manager can update it.
     *
     * @param caller The user attempting to set the explanation.
     * @param owner The employee who owns the constraint.
     * @param explanation The new explanation (must not be null).
     * @throws SecurityException if the caller is not authorized.
     * @throws NullPointerException if explanation is null.
     */
    public void setExplanation(User caller, Employee owner, String explanation) {
        if (!caller.isSameEmployee(owner) && !caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
        if (explanation == null) {
            throw new NullPointerException("Explanation cannot be null.");
        }
        this.explanation = explanation;
    }

    public WeekDay getDay() {
        return day;
    }

    /**
     * Sets a new day for the constraint.
     * Only the employee himself can update it.
     *
     * @param caller The user attempting to set the day.
     * @param employee The employee who owns the constraint.
     * @param day The new day (must not be null).
     * @throws SecurityException if the caller is not authorized.
     * @throws NullPointerException if day is null.
     */
    public void setDay(User caller, Employee employee, WeekDay day) {
        if (!caller.isSameEmployee(employee)) {
            throw new SecurityException("Access denied.");
        }
        if (day == null) {
            throw new NullPointerException("Day cannot be null.");
        }
        this.day = day;
    }

    public ShiftType getType() {
        return type;
    }

    /**
     * Sets a new shift type for the constraint.
     * Only the employee himself can update it.
     *
     * @param caller The user attempting to set the shift type.
     * @param employee The employee who owns the constraint.
     * @param type The new shift type (must not be null).
     * @throws SecurityException if the caller is not authorized.
     * @throws NullPointerException if type is null.
     */
    public void setType(User caller, Employee employee, ShiftType type) {
        if (!caller.isSameEmployee(employee)) {
            throw new SecurityException("Access denied.");
        }
        if (type == null) {
            throw new NullPointerException("Shift type cannot be null.");
        }
        this.type = type;
    }

    @Override
    public String toString() {
        return day + " " + type + " — " + explanation;
    }
}
