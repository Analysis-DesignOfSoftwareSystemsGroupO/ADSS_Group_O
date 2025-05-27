package HR_Mudol.DTO;

import HR_Mudol.domain.Objects.Constraint;
import HR_Mudol.domain.ShiftType;
import HR_Mudol.domain.WeekDay;

public class ConstraintMapper {

    public static ConstraintDTO toDTO(Constraint constraint, int empID) {
        return new ConstraintDTO(
                empID,
                constraint.getExplanation(),
                constraint.getDay().name(),
                constraint.getType().name()
        );
    }

    public static Constraint fromDTO(ConstraintDTO dto) {
        return new Constraint(
                dto.getExplanation(),
                WeekDay.valueOf(dto.getDay()),
                ShiftType.valueOf(dto.getType())
        );
    }
}
