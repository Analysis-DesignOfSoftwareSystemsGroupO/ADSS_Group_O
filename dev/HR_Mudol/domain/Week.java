package HR_Mudol.domain;
import java.time.*;
import java.util.LinkedList;
import java.util.List;
import java.time.temporal.TemporalAdjusters;

public class Week {

    private List<Shift> shifts; // The shifts of the week

    private LocalDateTime constraintDeadline;

    public Week() {
        this.shifts = new LinkedList<>();

        //creates the shifts of the week
        Shift newShift;
        this.constraintDeadline = LocalDate.now().with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.THURSDAY)).atTime(12, 0);
        WeekDay day= WeekDay.SUNDAY;
        ShiftType type=ShiftType.MORNING;
        for (int i=1; i<12; i++){

            if (i<=2){
                day= WeekDay.SUNDAY;
            } else if (i<=4) {
                day= WeekDay.MONDAY;
            } else if (i<=6) {
                day= WeekDay.TUESDAY;
            } else if (i<=8) {
                day= WeekDay.WEDNESDAY;
            } else if (i<=10) {
                day= WeekDay.THURSDAY;
            }
            else{
                day= WeekDay.FRIDAY;
            }
            //create the right type
            if (i%2==0){
                type=ShiftType.EVENING;
            }
            else {
                type=ShiftType.MORNING;
            }
            newShift= new Shift(day,type);
            shifts.addLast(newShift);
        }
    }
    // בנאי נוסף לצורך בדיקות – מאפשר לקבוע את הדדליין ידנית
    public Week(LocalDateTime customDeadline) {
        this(); // קרא לבנאי הרגיל ליצירת המשמרות
        this.constraintDeadline = customDeadline;
    }

    public void addShift(User caller, Shift shift) {
        boolean isHR = caller.isManager();
        boolean isSelfAssigned = shift.getEmployees().contains(caller.getUser());

        if (!isHR && !isSelfAssigned) {
            throw new SecurityException("Only HR or employees assigned to the shift can add it.");
        }

        this.shifts.add(shift);
    }



    public List<Shift> getShifts() {
        return shifts;
    }

    public Shift getAShift(WeekDay day,ShiftType type){
        for (Shift s:this.shifts){
            if (s.getDay()==day&&s.getType()==type){
                return s;
            }
        }
        return null;
    }

    public boolean isConstraintSubmissionOpen() {
        return LocalDateTime.now().isBefore(constraintDeadline);
    }

    public LocalDateTime getConstraintDeadline() {
        return constraintDeadline;
    }

    public void removeShift(Shift shift){
        this.shifts.remove(shift);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Shifts:\n");
        for (Shift shift : shifts) {
            sb.append("- ").append(shift.toString()).append("\n");
        }
        return sb.toString();
    }
}
