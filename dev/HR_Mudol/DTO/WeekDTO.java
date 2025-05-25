package HR_Mudol.DTO;
import java.time.LocalDateTime;

public class WeekDTO {
    private LocalDateTime constraintDeadline;

    public WeekDTO(LocalDateTime constraintDeadline) {
        this.constraintDeadline = constraintDeadline;
    }

    public LocalDateTime getConstraintDeadline() { return constraintDeadline; }
}
