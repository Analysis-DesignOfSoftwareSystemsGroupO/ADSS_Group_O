package HR_Mudol;

public interface IWeekManager {

    //It creates instances of new shifts (Sunday - Friday)
    void createNewWeek(User caller);

    //Canceling shift at the current week
    void cancelShift(User caller, int weekId, Shift shift);

    //Get previousW week
    Week getPreviousWeek(User caller, int weekId);

    //Printing method
    void printWeek(User caller, int weekId);


}