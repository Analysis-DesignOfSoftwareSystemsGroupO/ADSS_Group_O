package HR_Mudol.presentation;

import HR_Mudol.domain.Employee;
import HR_Mudol.domain.Shift;
import HR_Mudol.domain.User;

public interface IShiftManager {

    //Assign employee to shift
    void assignEmployeeToShift(User caller, Shift shift, Employee employee);

    //Remove employee from shift
    void removeEmployeeFromShift(User caller, Shift shift, Employee employee);

    //choosing relevantRole to a shift
    void chooseRelevantRoleForShift(User caller,Shift shift);

    //Printing method
    void printShift(User caller, Shift shift);
}
