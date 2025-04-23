package HR_Mudol.Service.ManagerSystem;

import HR_Mudol.domain.Employee;
import HR_Mudol.domain.Shift;
import HR_Mudol.domain.User;
import HR_Mudol.domain.Week;

import java.util.List;

public interface IWeekManager {

    //It creates instances of new shifts (Sunday - Friday)
    Week createNewWeek(User caller);

    //Canceling shift at the current week
    void cancelShift(User caller,Week week);

    //choose relevant roles for each shift at the week
    void manageTheWeekRelevantRoles (User caller, Week week);

    //Assigning employees to shifts
    void assigningEmployToShifts (User caller,Week week);

    //Printing method
    void printWeek(Week week);

    List<Shift> getShiftsForEmployee(Employee employee, Week curWeek);

    int hasUnassignedRoles(Week week);

}