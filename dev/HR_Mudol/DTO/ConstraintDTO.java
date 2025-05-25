package HR_Mudol.DTO;

public class ConstraintDTO {
    private int empID;
    private String explanation;
    private String day;   // e.g., "SUNDAY"
    private String type;  // e.g., "MORNING"

    public ConstraintDTO(int empID, String explanation, String day, String type) {
        this.empID = empID;
        this.explanation = explanation;
        this.day = day;
        this.type = type;
    }

    public int getEmpID() { return empID; }
    public String getExplanation() { return explanation; }
    public String getDay() { return day; }
    public String getType() { return type; }
}
