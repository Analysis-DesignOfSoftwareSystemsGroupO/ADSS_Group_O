package HR_Mudol.domain;

import java.util.LinkedList;
import java.util.List;

public class Week {

    private List<Shift> shifts; // The shifts of the week

    public Week() {
        this.shifts = new LinkedList<>();

        //creates the shifts of the week
        Shift newShift;
        DayOfWeek day=DayOfWeek.SUNDAY;
        ShiftType type=ShiftType.MORNING;
        for (int i=1; i<12; i++){

            if (i<=2){
                day=DayOfWeek.SUNDAY;
            } else if (i<=4) {
                day=DayOfWeek.MONDAY;
            } else if (i<=6) {
                day=DayOfWeek.TUESDAY;
            } else if (i<=8) {
                day=DayOfWeek.WEDNESDAY;
            } else if (i<=10) {
                day=DayOfWeek.THURSDAY;
            }
            else{
                day=DayOfWeek.FRIDAY;
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

    public List<Shift> getShifts() {
        return shifts;
    }

    public void removeShift(Shift shift){
        this.shifts.remove(shift);
    }

    @Override
    public String toString() {
       return shifts.toString();
    }
}
