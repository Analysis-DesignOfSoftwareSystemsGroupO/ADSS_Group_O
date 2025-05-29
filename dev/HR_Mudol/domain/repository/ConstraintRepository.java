package HR_Mudol.domain.repository;

import HR_Mudol.DAO.*;
import HR_Mudol.DTO.*;
import HR_Mudol.domain.Objects.*;
import HR_Mudol.domain.WeekDay;
import HR_Mudol.domain.ShiftType;
import java.util.HashMap;
import java.util.Map;

public class ConstraintRepository {
    private final IConstraintDAO constraintDAO;
    private final Map<String, Constraint> constraintCache = new HashMap<>();

    public ConstraintRepository(IConstraintDAO constraintDAO) {
        this.constraintDAO = constraintDAO;
    }

    public Constraint getConstraint(int empId, WeekDay day, ShiftType type) {
        String key = buildKey(empId, day, type);

        // RAM
        if (constraintCache.containsKey(key)) {
            return constraintCache.get(key);
        }

        // else - DB
        ConstraintDTO dto = constraintDAO.getConstraint(empId, day, type);
        if (dto == null) return null;

        Constraint constraint = fromDTO(dto);
        constraintCache.put(key, constraint);
        return constraint;
    }


    private Constraint fromDTO(ConstraintDTO dto) {
        return new Constraint(
                dto.getExplanation(),
                WeekDay.valueOf(dto.getDay().toUpperCase()),
                ShiftType.valueOf(dto.getType().toUpperCase())
        );
    }
    private String buildKey(int empId, WeekDay day, ShiftType type) {
        return empId + "_" + day.name() + "_" + type.name();
    }
}
