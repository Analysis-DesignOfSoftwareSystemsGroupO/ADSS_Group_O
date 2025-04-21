package HR_Mudol.presentation.ManagerSystem;

import HR_Mudol.domain.User;
import HR_Mudol.domain.Week;

public interface IWeekManager {

    //It creates instances of new shifts (Sunday - Friday)
    Week createNewWeek(User caller);

    //Canceling shift at the current week
    void cancelShift(User caller,Week week);

    //choose relevant roles for each shift at the week
    void manageTheWeekRelevantRoles (User caller,Week week);

    //Assigning employees to shifts
    void assigningEmployToShifts (User caller,Week week);

    //Printing method
    void printWeek(Week week);


}