package HR_Mudol;

import java.util.LinkedList;
import java.util.List;

public class Week {

    private List<Shift> shifts; // The shifts of the week

    public Week() {
        this.shifts = new LinkedList<>();
    }

    // Getter
    public List<Shift> getShifts() {
        return shifts;
    }

    //Add a shift
    public void addShift(Shift shift) {
        if (shift != null && !shifts.contains(shift)) {
            shifts.add(shift);
        }
    }

    //Remove a shift
    public void removeShift(Shift shift) {
        shifts.remove(shift);
    }

    @Override
    public String toString() {
       return shifts.toString();
    }
}
