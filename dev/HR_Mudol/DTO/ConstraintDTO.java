package HR_Mudol.DTO;

/**
 * Data Transfer Object representing a Constraint for an employee's availability.
 */
public class ConstraintDTO {
    private long empID;
    private String explanation;
    private String day;   // e.g., "SUNDAY"
    private String type;  // e.g., "MORNING"

    public ConstraintDTO(long ID, String explanation, String day, String type) {
        this.empID=ID;
        this.explanation = explanation;
        this.day = day;
        this.type = type;
    }

    public long getEmpID() {
        return empID;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getDay() {
        return day;
    }

    public String getType() {
        return type;
    }

}
