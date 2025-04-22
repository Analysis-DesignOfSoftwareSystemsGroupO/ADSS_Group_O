package HR_Mudol.domain;

public class Constraint {
    private String explanation;
    private WeekDay day;
    private ShiftType type;

    public Constraint(String explanation, WeekDay day, ShiftType type){
        this.explanation=explanation;
        this.day=day;
        this.type=type;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(User caller, Employee owner, String explanation) {
        if (!caller.isSameEmployee(owner) && !caller.isManager()) {
            throw new SecurityException("Access denied.");
        }
        this.explanation = explanation;
    }


    public WeekDay getDay() {
        return day;
    }

    //just by the employee himself
    public void setDay(User caller, Employee employee, WeekDay day) {
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
