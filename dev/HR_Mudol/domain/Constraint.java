package HR_Mudol.domain;

public class Constraint {
    private String explanation;
    private DayOfWeek day;
    private ShiftType type;

    public Constraint(String explanation,DayOfWeek day, ShiftType type){
        this.explanation=explanation;
        this.day=day;
        this.type=type;
    }

    public String getExplanation() {
        return explanation;
    }

    //just by the employee himself
    public void setExplanation(User caller,Employee employee, String explanation) {
        if (!caller.isSameEmployee(employee)) {
            throw new SecurityException("Access denied");
        }
        this.explanation = explanation;
    }

    public DayOfWeek getDay() {
        return day;
    }

    //just by the employee himself
    public void setDay(User caller,Employee employee,DayOfWeek day) {
        if (!caller.isSameEmployee(employee)) {
            throw new SecurityException("Access denied");
        }
        this.day = day;
    }

    public ShiftType getType() {
        return type;
    }

    //just by the employee himself
    public void setType(User caller,Employee employee,ShiftType type) {
        if (!caller.isSameEmployee(employee)) {
            throw new SecurityException("Access denied");
        }
        this.type = type;
    }

    @Override
    public String toString() {
        return day + " " + type + " — " + explanation;
    }
}
