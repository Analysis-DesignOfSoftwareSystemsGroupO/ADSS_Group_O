package HR_Mudol.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class WeekDTO {
    private LocalDateTime constraintDeadline;
    private List<ShiftDTO> shifts;

    public WeekDTO(LocalDateTime constraintDeadline, List<ShiftDTO> shifts) {
        this.constraintDeadline = constraintDeadline;
        this.shifts = shifts;
    }

    public LocalDateTime getConstraintDeadline() {
        return constraintDeadline;
    }

    public List<ShiftDTO> getShifts() {
        return shifts;
    }

    public void setConstraintDeadline(LocalDateTime constraintDeadline) {
        this.constraintDeadline = constraintDeadline;
    }

    public void setShifts(List<ShiftDTO> shifts) {
        this.shifts = shifts;
    }

    public boolean isConstraintSubmissionOpen() {
        return LocalDateTime.now().isBefore(constraintDeadline);
    }



}
