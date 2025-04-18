package HR_Mudol.presentation;

import HR_Mudol.domain.Employee;
import HR_Mudol.domain.User;

public interface IShiftManager {

    //Assign employee to shift
    void assignEmployeeToShift(User caller, int shiftId, Employee employee);

    //Remove employee from shift
    void removeEmployeeFromShift(User caller, int shiftId, Employee employee);

    //Printing method
    void printShift(User caller, int shiftId);
}
