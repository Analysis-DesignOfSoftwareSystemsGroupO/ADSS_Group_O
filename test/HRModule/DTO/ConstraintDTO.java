package HRModule.DTO;

import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.WeekDay;

public class ConstraintDTO {
    private String description;
    private WeekDay day;
    private ShiftType type;

    public ConstraintDTO(String description, WeekDay day, ShiftType type) {
        this.description = description;
        this.day = day;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public WeekDay getDay() {
        return day;
    }

    public ShiftType getType() {
        return type;
    }
}
